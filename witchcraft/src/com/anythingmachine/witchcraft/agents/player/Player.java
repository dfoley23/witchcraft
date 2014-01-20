package com.anythingmachine.witchcraft.agents.player;

import java.util.ArrayList;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.Agent;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Power.DuplicateSkin;
import com.anythingmachine.witchcraft.agents.player.Power.FlyingPower;
import com.anythingmachine.witchcraft.agents.player.Power.InvisiblePower;
import com.anythingmachine.witchcraft.agents.player.Power.MindControlPower;
import com.anythingmachine.witchcraft.agents.player.Power.Power;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Player extends Agent {
	private Cape cape;
	private StateMachine state;
	private Bone neck;
	private InputManager input;
	private ArrayList<Power> powers;
	private ArrayList<Sprite> powerUi;
	private int power;
	private NonPlayer npc;
	private Arrow arrow;
	private boolean shotArrow = false;
	private Bone arrowBone;
	private String currentSkin;
	private float uiFadein = -1f;

	public Player(RK4Integrator rk4) {
		this.curGroundSegment = 0;
		this.curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		this.body = new KinematicParticle(
				new Vector3(32f, WitchCraft.ground.findPointOnCurve(
						curGroundSegment, 32f).y, 0f), Util.GRAVITY * 3);
		WitchCraft.rk4System.addParticle(body);

		this.input = new InputManager();
		setupInput();
		setupAnimations("player");
		this.state.setState(State.IDLE);
		arrow = new Arrow(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
		arrowBone = state.animate.findBone("right hand");
		neck = state.animate.findBone("neck");

		cape = new Cape(3, 5, rk4);
		buildCollisionBody();
		setupPowers();
		setupTests();
		power = 0;
		type = EntityType.PLAYER;

	}

	/** public functions **/

	public void update(float dT) {
		float delta = Gdx.graphics.getDeltaTime();
		input.update(dT);
		if (state.test("dupeskin") && state.test("hitnpc")) {
			currentSkin = npc.getSkin().getName();
			state.animate.switchSkin(currentSkin);
			state.animate.bindPose();
			state.setTestVal("usingdupeskin", true);
			state.setTestVal("dupeskin", false);
			state.setTestVal("hitnpc", false);
		}
		// check if on ground
		checkGround();

		// handle walking input
		boolean moving = updateWalking(delta);

		state.animate.setFlipX(state.test("facingleft"));

		if (state.testANDtest("grounded", "hitplatform")) {
			body.setVel(body.getVel().x, 0, 0);
		}

		// handle user power input
		if (input.isNowNotThen("SwitchPower")
				|| input.isNowNotThen("SwitchPower1")
				|| input.isNowNotThen("SwitchPower2")) {
			power = (power + 1) >= powers.size() ? 0 : power + 1;
			uiFadein = 0f;
		}
		if (input.is("UsePower")) {
			powers.get(power).usePower(state, state.animate, body);
		} else {
			powers.get(power).updatePower(state, state.animate, dT);
		}
		int i = 0;
		for (Power p : powers) {
			if (i != power) {
				p.updatePower(state, state.animate, dT);
			}
			i++;
		}

		if (input.is("attack") && state.state.canAttack(state))
			handleAttacking();
		// update skeletal animation
		updateSkeleton(moving, delta);

		// rotate collision box when flying
		if (state.state.startingToFly(state)) {
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).scl(Util.PIXEL_TO_BOX),
					Util.HALF_PI);
		} else {
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).scl(Util.PIXEL_TO_BOX), 0);
		}
		// System.out.println(state.state);
	}

	public void draw(SpriteBatch batch, Matrix4 cam) {
		state.animate.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
		if (!state.test("usingdupeskin")) {
			if (state.test("invi")) {
				cape.draw(cam, 0.1f);
			} else {
				cape.draw(cam, 1f);
			}
		}
	}

	public Vector3 getPosPixels() {
		return body.getPos();
	}

	public int getPower() {
		return power;
	}

	public int getCurSegment() {
		return curGroundSegment;
	}

	public void correctHeight(float y) {
		body.setPos(body.getPos().x, y, 0);
	}

	public void incrementSegment() {
		this.curGroundSegment++;
	}

	public void decrementSegment() {
		this.curGroundSegment++;
	}

	public void drawUI(SpriteBatch batch) {
		if (uiFadein >= 0f) {
			Sprite sprite = powerUi.get(power);
			sprite.setPosition(
					WitchCraft.cam.camera.position.x - sprite.getWidth() * 0.5f,
					WitchCraft.cam.camera.position.y
							+ (WitchCraft.cam.camera.viewportHeight * 0.5f)
							- sprite.getHeight());
			powerUi.get(power).draw(batch, uiFadein);
			uiFadein += WitchCraft.dt * 0.5f;
			if (uiFadein >= 1f)
				uiFadein = -2f;
		} else if (uiFadein < 0 && uiFadein > -4) {
			Sprite sprite = powerUi.get(power);
			sprite.setPosition(
					WitchCraft.cam.camera.position.x - sprite.getWidth() * 0.5f,
					WitchCraft.cam.camera.position.y
							+ (WitchCraft.cam.camera.viewportHeight * 0.5f)
							- sprite.getHeight());
			powerUi.get(power).draw(batch, -(uiFadein + 1));
			uiFadein += WitchCraft.dt * 0.5f;
			if (uiFadein >= -1)
				uiFadein = -5f;
		}
	}
	
	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		Vector2 pos = body.getPos2D();
		Vector2 vel = body.getVel2D();
		switch (other.type) {
		case NONPLAYER:
			npc = (NonPlayer) other;
			state.setTestVal("hitnpc", true);
			break;
		case WALL:
			body.setPos(pos.x-(Math.signum(vel.x)*8), pos.y, 0);
			body.setVel(0, vel.y, 0);
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(state.test("facingleft"), pos.x)) {
				if (plat.getHeight() - 32 < pos.y) {
					state.setTestVal("hitplatform", true);
					this.elevatedSegment = plat;
					state.state.land(state);
				} else {
					body.setPos(pos.x, pos.y-8, 0);
					body.setVel(vel.x, 0, 0);
					state.setTestVal("hitroof", true);
				}
			}
			break;
		case STAIRS:
			plat = (Platform) other;
			if (input.is("UP")
					|| (plat.getHeight(pos.x) < (pos.y + 4) && plat
							.getHeight(pos.x) > plat.getHeightLocal() * 0.35f
							+ plat.getPos().y)) {
				if (plat.isBetween(state.test("facingleft"), pos.x)) {
					if (plat.getHeight(pos.x) - 8 < pos.y) {
						state.setTestVal("hitplatform", true);
						this.elevatedSegment = plat;
						state.state.land(state);
					}
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		// if (other.equals(elevatedSegment) && other.type ==
		// EntityType.PLATFORM) {
		// state.setTestVal("hitplatform", false);
		// if (!state.state.canLand()) {
		// state.setState(State.FALLING);
		// }
		// }
	}

	/************ private functions ************/
	/******************************************/
	protected void checkGround() {
		if (!state.test("hitplatform")) {
			Vector3 pos = body.getPos().cpy();
			// System.out.println(pos);
			if (pos.x > curCurve.lastPointOnCurve().x
					&& curGroundSegment + 1 < WitchCraft.ground.getNumCurves()) {
				curGroundSegment++;
				curCurve = WitchCraft.ground.getCurve(curGroundSegment);
			} else if (pos.x < curCurve.firstPointOnCurve().x
					&& curGroundSegment - 1 >= 0) {
				curGroundSegment--;
				curCurve = WitchCraft.ground.getCurve(curGroundSegment);
			}
			Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
					curGroundSegment, pos.x);
			state.setTestVal("grounded", false);
			if (pos.y <= groundPoint.y) {
				correctHeight(groundPoint.y);
				state.setTestVal("grounded", true);
				state.state.land(state);
				body.setVel(0f, 0f, 0f);
			}
		} else {
			float x = body.getPos2D().x;
			state.setTestVal("grounded", false);
			if (elevatedSegment.isBetween(state.test("facingleft"), x)) {
				float groundPoint = elevatedSegment.getHeight(x);
				if (body.getPos().y < groundPoint) {
					correctHeight(groundPoint);
					state.setTestVal("grounded", true);
					state.state.land(state);
					body.setVel(0f, 0f, 0f);
				}
			} else {
				state.setTestVal("hitplatform", false);
			}
		}
	}

	private void updateSkeleton(boolean moving, float delta) {
		if (!state.inState(State.FLYING)) {
			cape.addWindForce(0, -400);
			if (state.testORNotState("grounded", State.JUMPING)
					|| state.animate.testUnderTime(0f, 0.95f)) {
				state.animate.applyTotalTime(true, delta);
			} else {
				// if you are not on the ground
				// and you are jumping
				// and the current animation time is over 0.95
				// then begin flying
				state.setState(State.FLYING);
				if (state.test("facingleft")) {
					cape.addWindForce(-500, 0);
				} else {
					cape.addWindForce(-500, 0);
				}
			}
		}
		state.animate.setPos(body.getPos(), -8f, 0f);
		state.animate.updateSkel(delta);
		// update the root position of the cape
		if (state.test("facingleft")) {
			if (state.inState(State.FLYING)) {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY() + 7,
						true, false);
			} else {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY(), true,
						false);
			}
		} else {
			if (state.inState(State.FLYING)) {
				cape.updatePos(neck.getWorldX() + 5, neck.getWorldY() + 7,
						false, false);
			} else {
				cape.updatePos(neck.getWorldX() + 7, neck.getWorldY(), false,
						false);
			}
		}
	}

	private void handleAttacking() {
		if (currentSkin.equals("archer")) {
			state.animate.bindPose();
			state.animate.setCurrent("drawbow", true);
			state.setState(State.ATTACK);
		} else if (currentSkin.contains("knight")) {
			state.animate.bindPose();
			state.animate.setCurrent("swordattack", true);
			state.setState(State.ATTACK);
		}
	}

	public void updateAttacking() {
		if (state.inState(State.ATTACK) && currentSkin.equals("archer")) {
			if (!shotArrow && state.animate.isTImeOverThreeQuarters(0f)) {
				arrow.setPos(arrowBone.getWorldX() + (facingLeft ? -128 : 128),
						arrowBone.getWorldY(), 0);
				arrow.pointAtTarget(WitchCraft.player.getPosPixels(), 650);
				shotArrow = true;
			}
		}
	}

	private boolean updateWalking(float delta) {
		if (WitchCraft.ON_ANDROID) {
			int axisVal = input.axisRange2();
			if (axisVal != 0) {
				if (state.test("grounded")) {
					int absval = Math.abs(axisVal);
					if (absval == 1) {
						state.state.setWalk(state);
					} else if (absval == 2) {
						state.state.setRun(state);
					}
				}
				body.setVel(
						Math.signum(axisVal) * state.state.getInputSpeed(state),
						body.getVel().y, 0f);
				state.setTestVal("facingleft", axisVal < 0);
				return true;
			} else if (state.state.canBeIdle(state)) { // state.animate.isTimeOverAQuarter(delta)
				state.setState(State.IDLE);
				state.animate.setCurrent("idle", true);
				state.animate.bindPose();
				body.setVel(0f, body.getVel().y, 0f);
			}
			return false;
		} else {
			boolean ismoving = false;
			if (input.is("Right")) {
				body.setVel(state.state.getInputSpeed(state), body.getVel().y,
						0f);
				state.setTestVal("facingleft", false);
				ismoving = true;
			} else if (input.is("Left")) {
				body.setVel(-state.state.getInputSpeed(state), body.getVel().y,
						0f);
				state.setTestVal("facingleft", true);
				ismoving = true;
			}
			if (state.test("grounded") && ismoving) {
				state.state.setWalk(state);
			} else if (!ismoving && state.state.canBeIdle(state)) { // state.animate.isTimeOverAQuarter(delta)
				state.setState(State.IDLE);
				state.animate.setCurrent("idle", true);
				state.animate.bindPose();
				body.setVel(0f, body.getVel().y, 0f);
			}
			return ismoving;
		}
	}

	/******************* setup functions ****************/
	/***************************************************/
	private void setupInput() {
		if (Controllers.getControllers().size > 0) {
			input.addInputState("Left", 21);
			input.addInputState("Right", 22);
			input.addInputState("UPAxis", 1);
			input.addInputState("UP", 19);
			input.addInputState("SwitchPower", 97);
			input.addInputState("SwitchPower1", 105);
			input.addInputState("SwitchPower2", 103);
			input.addInputState("UsePower", 96);
			input.addInputState("Interact", 100);
			input.addInputState("attack", 99);

		} else {
			input.addInputState("Left", Keys.LEFT);
			input.addInputState("Right", Keys.RIGHT);
			input.addInputState("UP", Keys.UP);
			input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
			input.addInputState("SwitchPower1", Keys.SHIFT_LEFT);
			input.addInputState("SwitchPower2", Keys.SHIFT_LEFT);
			input.addInputState("UsePower", Keys.SPACE);
			input.addInputState("Interact", Keys.D);
			input.addInputState("attack", Keys.A);
		}
	}

	private void setupTests() {
		state.addTest("grounded", false);
		state.addTest("hitplatform", false);
		state.addTest("hitroof", false);
		state.addTest("facingleft", false);
		state.addTest("invi", false);
		state.addTest("usingpower", false);
		state.addTest("dupeskin", false);
		state.addTest("usingdupeskin", false);
		state.addTest("hitnpc", false);
	}

	private void setupPowers() {
		powers = new ArrayList<Power>();
		powerUi = new ArrayList<Sprite>();
		powers.add(new FlyingPower());
		Sprite sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas")).findRegion("FUGE"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		powerUi.add(sprite);
		powers.add(new MindControlPower());
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("ANIMIMPERI"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		powerUi.add(sprite);
		// powers.put("shapecrow", new ShapeShiftCrowPower());
		// powers.put("shapecat", new ShapeShiftCatPower());
		powers.add(new InvisiblePower());
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("INVISIBIL"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		powerUi.add(sprite);
		powers.add(new DuplicateSkin());
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("EFFINGO"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		powerUi.add(sprite);
		// powers.put("intangible", new IntangibilityPower());
		// powers.put("convert", new ConvertPower());
		// powers.put("freeze", new FreezePower());
		// powers.put("death", new DeathPower());
	}

	private void setupAnimations(String name) {
		TextureAtlas atlas = WitchCraft.assetManager
				.get("data/spine/characters.atlas");
		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		state = new StateMachine(name, body.getPos(), new Vector2(0.5f, 0.5f),
				false, sd);
		state.animate.addAnimation("jump", sd.findAnimation("beginfly"));
		state.animate.addAnimation("idle", sd.findAnimation("idle"));
		state.animate.addAnimation("walk", sd.findAnimation("walk"));
		state.animate.addAnimation("run", sd.findAnimation("run"));
		state.animate.addAnimation("castspell", sd.findAnimation("castspell"));
		state.animate.addAnimation("swordattack",
				sd.findAnimation("overheadattack"));
		state.animate.addAnimation("drawbow", sd.findAnimation("drawbow"));

		state.animate.setCurrent("idle", true);
	}

	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.body.getPos().x, this.body.getPos().y));
		collisionBody = WitchCraft.world.createBody(def);
		collisionBody.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		feetFixture = collisionBody.createFixture(fixture);

		shape = new PolygonShape();
		shape.setAsBox(64 * Util.PIXEL_TO_BOX, 4 * Util.PIXEL_TO_BOX,
				new Vector2(0, 16).scl(Util.PIXEL_TO_BOX), 0f);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		hitRadius = collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
	}
}
