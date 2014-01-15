package com.anythingmachine.witchcraft.agents.player;

import java.util.ArrayList;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.Agent;
import com.anythingmachine.witchcraft.agents.player.Power.FlyingPower;
import com.anythingmachine.witchcraft.agents.player.Power.InvisiblePower;
import com.anythingmachine.witchcraft.agents.player.Power.MindControlPower;
import com.anythingmachine.witchcraft.agents.player.Power.Power;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Platform;
import com.anythingmachine.witchcraft.ground.Stairs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
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
	private StateMachine playerState;
	private Bone neck;
	private InputManager input;
	private Body collisionBody;
	private ArrayList<Power> powers;
	private int power;

	public Player(RK4Integrator rk4) {
		this.curGroundSegment = 0;
		this.curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		this.body = new KinematicParticle(
				new Vector3(32f, WitchCraft.ground.findPointOnCurve(
						curGroundSegment, 32f).y, 0f), Util.GRAVITY * 3);
		WitchCraft.rk4System.addParticle(body);

		playerState = new StateMachine();
		this.playerState.setState(State.IDLE);
		this.input = new InputManager();
		setupInput();
		setupAnimations("player");

		neck = animate.findBone("neck");

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

		// check if on ground
		checkGround();

		// handle walking input
		boolean moving = updateWalking(delta);

		animate.setFlipX(playerState.test("facingleft"));

		if (playerState.testtest("grounded", "hitplatform")) {
			body.setVel(body.getVel().x, 0, 0);
		}

		// handle user power input
		if (input.is("SwitchPower")) {
			power = (power + 1) >= powers.size() ? 0 : power + 1;
		}
		if (input.is("UsePower")) {
			powers.get(power).usePower(playerState, animate, body);
		} else {
			powers.get(power).updatePower(playerState, animate, dT);
		}

		// update skeletal animation
		updateSkeleton(moving, delta);

		// rotate collision box when flying
		if (playerState.state.startingToFly()) {
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).mul(Util.PIXEL_TO_BOX),
					Util.HALF_PI);
		} else {
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).mul(Util.PIXEL_TO_BOX), 0);
		}
		// System.out.println(playerState.state);
	}

	public void draw(SpriteBatch batch, Matrix4 cam) {
		animate.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
		if (animate.getSkin().getName().equals("invi")) {
			cape.draw(cam, 0.1f);
		} else {
			cape.draw(cam, 1f);
		}
	}

	public Vector3 getPosPixels() {
		return body.getPos();
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

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		switch (other.type) {
		case PLATFORM:
			Platform plat = (Platform) other;
			Vector2 pos = body.getPos2D();
			if (plat.isBetween(pos.x)) {
				if (plat.getHeight() - 64 < pos.y) {
					playerState.setTestVal("hitplatform", true);
					this.elevatedSegment = plat;
					if (playerState.state.canLand())
						playerState.setState(State.LANDING);
				} else {
					// System.out.println("platfrom");
					// System.out.println(plat.getHeight());
					// System.out.println(pos);
					// body.setPos(pos.x,plat.getHeight()-10, 0);
					body.setVel(body.getVel2D().x, -50, 0);
					playerState.setTestVal("hitroof", true);
				}
			}
			break;
		case STAIRS:
			plat = (Stairs) other;
			pos = body.getPos2D();
			// System.out.println("stairsdfsdfd");
			if (plat.isBetween(playerState.test("facingleft") ? pos.x - 16 : pos.x)) {
				if (plat.getHeight() - 70 < pos.y) {
					playerState.setTestVal("hitplatform", true);
					this.elevatedSegment = plat;
					if (playerState.state.canLand())
						playerState.setState(State.LANDING);
				} else {
					// System.out.println(plat.getHeight());
					// System.out.println(pos);
					// body.setPos(pos.x,plat.getHeight()-10, 0);
					body.setVel(body.getVel2D().x, -50, 0);
					playerState.setTestVal("hitroof", true);
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
		if (other.type == EntityType.PLATFORM) {
			Platform plat = (Platform) other;
			if (plat.equals(elevatedSegment))
				playerState.setTestVal("hitplatform", false);
			if (!playerState.state.canLand()) {
				playerState.setState(State.FALLING);
			}
		}
	}

	/************ private functions ************/
	/******************************************/
	@Override
	protected void checkGround() {
		if (!playerState.test("hitplatform")) {
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
			playerState.setTestVal("grounded", false);
			if (pos.y <= groundPoint.y) {
				correctHeight(groundPoint.y);
				playerState.setTestVal("grounded", true);
				playerState.setState(playerState.state.land());
			}
		} else {
			float x = body.getPos2D().x;
			playerState.setTestVal("grounded", false);
			if (elevatedSegment.isBetween(playerState.test("facingleft") ? x - 16 : x)) {
				float groundPoint = elevatedSegment.getHeight(x);
				if (body.getPos().y < groundPoint) {
					correctHeight(groundPoint);
					playerState.setTestVal("grounded", true);
				}
			} else {
				playerState.setTestVal("hitplatform", false);
			}
		}
	}

	private void updateSkeleton(boolean moving, float delta) {
		if (!playerState.inState(State.FLYING)) {
			cape.addWindForce(0, -400);
			if (animate.atEnd() && !moving) {
				playerState.setState(State.IDLE);
				animate.setCurrent("idle", true);
				animate.bindPose();
				body.setVel(0f, 0f, 0f);
			} else if (playerState.testORNotState("grounded", State.JUMPING)
					|| animate.testUnderTime(0f, 0.95f)) {
				animate.applyTotalTime(true, delta);
			} else {
				// if you are not on the ground
				// and you are jumping
				// and the current animation time is over 0.95
				// then begin flying
				playerState.setState(State.FLYING);
				if (playerState.test("facingleft")) {
					cape.addWindForce(-500, 0);
				} else {
					cape.addWindForce(-500, 0);
				}
			}
		}
		animate.setPos(body.getPos(), -8f, 0f);
		animate.updateSkel(delta);
		// update the root position of the cape
		if (playerState.test("facingleft")) {
			if (playerState.inState(State.FLYING)) {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY() + 7,
						true, false);
			} else {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY(), true,
						false);
			}
		} else {
			if (playerState.inState(State.FLYING)) {
				cape.updatePos(neck.getWorldX() + 5, neck.getWorldY() + 7,
						false, false);
			} else {
				cape.updatePos(neck.getWorldX() + 7, neck.getWorldY(), false,
						false);
			}
		}
	}

	private boolean updateWalking(float delta) {
		boolean ismoving = false;
		if (input.is("Right")) {
			body.setVel(playerState.state.getInputSpeed(), body.getVel().y, 0f);
			playerState.setTestVal("facingleft", false);
			ismoving = true;
		} else if (input.is("Left")) {
			body.setVel(-playerState.state.getInputSpeed(), body.getVel().y, 0f);
			playerState.setTestVal("facingleft", true);
			ismoving = true;
		}
		if (playerState.test("grounded") && ismoving
				&& playerState.state.canWalk()) {
			animate.setCurrent("walk", true);
			animate.bindPose();
			playerState.setState(State.WALKING);
		}
		// else if (playerState.state.canBeIdle()
		// && animate.isTimeOverAQuarter(delta)) {
		// playerState.setState(State.IDLE);
		// animate.setCurrent("idle", true);
		// animate.bindPose();
		// body.setVel(0f, body.getVel().y, 0f);
		// }
		return ismoving;
	}

	/******************* setup functions ****************/
	/***************************************************/
	private void setupInput() {
		input.addInputState("Left", Keys.LEFT);
		input.addInputState("Right", Keys.RIGHT);
		input.addInputState("Fly", Keys.UP);
		input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
		input.addInputState("UsePower", Keys.SPACE);
		input.addInputState("Interact", Keys.A);
	}

	private void setupTests() {
		playerState.addTest("grounded", false);
		playerState.addTest("hitplatform", false);
		playerState.addTest("hitroof", false);
		playerState.addTest("facingleft", false);
	}

	private void setupPowers() {
		powers = new ArrayList<Power>();
		powers.add(new FlyingPower());
		powers.add(new MindControlPower());
		// powers.put("shapecrow", new ShapeShiftCrowPower());
		// powers.put("shapecat", new ShapeShiftCatPower());
		powers.add(new InvisiblePower());
		// powers.put("intangible", new IntangibilityPower());
		// powers.put("convert", new ConvertPower());
		// powers.put("freeze", new FreezePower());
		// powers.put("death", new DeathPower());
	}

	private void setupAnimations(String name) {
		TextureAtlas atlas = WitchCraft.assetManager.getAtlas("characters");
		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));
		animate = new AnimationManager(name, body.getPos(), new Vector2(0.5f,
				0.5f), false, sd);
		animate.addAnimation(
				"jump",
				sb.readAnimation(
						Gdx.files.internal("data/spine/characters-beginfly.anim"), sd));
		animate.addAnimation(
				"walk",
				sb.readAnimation(
						Gdx.files.internal("data/spine/characters-walk.anim"), sd));
		animate.addAnimation(
				"idle",
				sb.readAnimation(
						Gdx.files.internal("data/spine/characters-idle.anim"), sd));
		animate.addAnimation("castspell", sb.readAnimation(
				Gdx.files.internal("data/spine/characters-castspell.anim"), sd));
		animate.setCurrent("idle", true);
	}

	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.body.getPos().x, this.body.getPos().y));
		collisionBody = WitchCraft.world.createBody(def);
		collisionBody.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(24 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
	}
}
