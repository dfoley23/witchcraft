package com.anythingmachine.witchcraft.agents.player;

import java.util.ArrayList;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.States.Attacking;
import com.anythingmachine.witchcraft.States.CastSpell;
import com.anythingmachine.witchcraft.States.Dead;
import com.anythingmachine.witchcraft.States.DupeSkin;
import com.anythingmachine.witchcraft.States.DupeSkinPower;
import com.anythingmachine.witchcraft.States.Falling;
import com.anythingmachine.witchcraft.States.Flying;
import com.anythingmachine.witchcraft.States.Idle;
import com.anythingmachine.witchcraft.States.Invisible;
import com.anythingmachine.witchcraft.States.Jumping;
import com.anythingmachine.witchcraft.States.Landing;
import com.anythingmachine.witchcraft.States.MindControlPower;
import com.anythingmachine.witchcraft.States.Running;
import com.anythingmachine.witchcraft.States.ShapeCrow;
import com.anythingmachine.witchcraft.States.StateEnum;
import com.anythingmachine.witchcraft.States.Walking;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Player extends Entity {
	public Cape cape;
	private StateMachine state;

	public Player(RK4Integrator rk4) {
		setupState("player");
		setupInput();
		setupPowers();
		setupTests();

		cape = new Cape(3, 5, rk4, state.phyState.getPos());
		type = EntityType.PLAYER;

	}

	/** public functions **/

	public void update(float dT) {
//		 System.out.println(state.state.name);
		state.update(dT);
		// check if on ground

		// handle user power state.input

		// update skeletal animation

		// rotate collision box when flying
		// System.out.println(state.state);
	}

	public void draw(Batch batch) {
		state.state.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
		state.state.drawCape(cam);
	}

	public Vector3 getPosPixels() {
		return state.phyState.body.getPos();
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		Vector3 pos = state.phyState.body.getPos();
		Vector3 vel = state.phyState.body.getVel();
		float sign;
		switch (other.type) {
		case NONPLAYER:
			state.state.hitNPC((NonPlayer) other);
			break;
		case WALL:
			sign = Math.signum(vel.x);
//			System.out.println("hello wall");
			state.phyState.stopOnX();
			if (sign == -1) {
				state.setTestVal("hitleftwall", true);
			} else {
				state.setTestVal("hitrightwall", true);
			}
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(state.test("facingleft"), pos.x)) {
				if (plat.getHeight() - 32 < pos.y) {
					state.setTestVal("hitplatform", true);
					state.elevatedSegment = plat;
					state.state.land();
				} else {
					state.phyState.stopOnY();
					state.setTestVal("hitroof", true);
				}
			}
			break;
		case STAIRS:
			plat = (Platform) other;
			if (state.input.is("UP")
					|| (plat.getHeight(pos.x) < (pos.y + 4) && plat
							.getHeight(pos.x) > plat.getHeightLocal() * 0.35f
							+ plat.getPos().y)) {
				if (plat.isBetween(state.test("facingleft"), pos.x)) {
					System.out.println("hello");
					if (plat.getHeight(pos.x) - 8 < pos.y) {
						state.setTestVal("hitplatform", true);
						state.elevatedSegment = plat;
						state.state.land();
					}
				}
			}
			break;
		case LEVELWALL:
			sign = Math.signum(vel.x);
			System.out.println("hello wall");
			state.phyState.stopOnX();
			if (sign == -1) {
				state.setTestVal("hitleftwall", true);
			} else {
				state.setTestVal("hitrightwall", true);
			}
			break;
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

	public void drawUI(Batch batch) {
		state.drawUI(batch);
	}

	/************ private functions ************/
	/******************************************/

	/******************* setup functions ****************/
	/***************************************************/
	private void setupInput() {
		if (Controllers.getControllers().size > 0) {
			state.input.addInputState("Left", 21);
			state.input.addInputState("Right", 22);
			state.input.addInputState("UPAxis", 1);
			state.input.addInputState("UP", 19);
			state.input.addInputState("SwitchPower", 97);
			state.input.addInputState("SwitchPower1", 105);
			state.input.addInputState("SwitchPower2", 103);
			state.input.addInputState("UsePower", 96);
			state.input.addInputState("Interact", 100);
			state.input.addInputState("attack", 99);

		} else {
			state.input.addInputState("Left", Keys.LEFT);
			state.input.addInputState("Right", Keys.RIGHT);
			state.input.addInputState("UP", Keys.UP);
			state.input.addInputState("down", Keys.DOWN);
			state.input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
			state.input.addInputState("UsePower", Keys.SPACE);
			state.input.addInputState("Interact", Keys.D);
			state.input.addInputState("attack", Keys.A);
		}
	}

	private void setupStates() {
		state.addState(StateEnum.IDLE, new Idle(state, StateEnum.IDLE));
		state.addState(StateEnum.WALKING, new Walking(state, StateEnum.WALKING));
		state.addState(StateEnum.RUNNING, new Running(state, StateEnum.RUNNING));
		state.addState(StateEnum.JUMPING, new Jumping(state, StateEnum.JUMPING));
		state.addState(StateEnum.FLYING, new Flying(state, StateEnum.FLYING));
		state.addState(StateEnum.FALLING, new Falling(state, StateEnum.FALLING));
		state.addState(StateEnum.LANDING, new Landing(state, StateEnum.LANDING));
		state.addState(StateEnum.ATTACKING, new Attacking(state,
				StateEnum.ATTACKING));
		state.addState(StateEnum.DEAD, new Dead(state, StateEnum.DEAD));
		state.addState(StateEnum.CASTSPELL, new CastSpell(state, StateEnum.CASTSPELL));
		state.addState(StateEnum.DUPESKIN, new DupeSkin(state, StateEnum.DUPESKIN));
		/*power states*/
		state.addState(StateEnum.DUPESKINPOWER, new DupeSkinPower(state, StateEnum.DUPESKINPOWER));
		state.addState(StateEnum.MINDCONTROLPOWER, new MindControlPower(state, StateEnum.MINDCONTROLPOWER));
		state.addState(StateEnum.INVISIBLEPOWER, new Invisible(state, StateEnum.INVISIBLEPOWER));
		state.addState(StateEnum.SHAPECROWPOWER, new ShapeCrow(state, StateEnum.SHAPECROWPOWER));
//		state.addState(StateEnum.SHAPECATPOWER, new ShapeCatPower(state, StateEnum.SHAPECATPOWER));
//		state.addState(StateEnum.INTANGIBLEPOWER, new Intangible(state, StateEnum.INTANGIBLEPOWER));
		state.setInitialState(StateEnum.IDLE);
	}

	private void setupTests() {
		state.addTest("grounded", false);
		state.addTest("hitplatform", false);
		state.addTest("hitroof", false);
		state.addTest("facingleft", false);
		state.addTest("invi", false);
		state.addTest("hitrightwall", false);
		state.addTest("hitleftwall", false);
	}

	private void setupPowers() {
		float width = WitchCraft.cam.camera.viewportWidth/2f;
		float height = WitchCraft.cam.camera.viewportHeight;
		state.powerUi = new ArrayList<Sprite>();
		Sprite sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas")).findRegion("FUGE"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f, height	- sprite.getHeight());
		state.powerUi.add(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("ANIMIMPERI"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f, height	- sprite.getHeight());
		state.powerUi.add(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("INVISIBIL"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f, height	- sprite.getHeight());
		state.powerUi.add(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("EFFINGO"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f, height	- sprite.getHeight());
		state.powerUi.add(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("MUTATIO"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f, height	- sprite.getHeight());
		state.powerUi.add(sprite);
		// powers.put("intangible", new IntangibilityPower());
		// powers.put("convert", new ConvertPower());
		// powers.put("freeze", new FreezePower());
		// powers.put("death", new DeathPower());
	}

	private void setupState(String name) {
		TextureAtlas atlas = WitchCraft.assetManager
				.get("data/spine/characters.atlas");
		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		KinematicParticle body = new KinematicParticle(new Vector3(256f,128f,0f),Util.GRAVITY * 3);

		state = new StateMachine(name, body.getPos(), new Vector2(0.53f, 0.53f), false, sd);

		state.animate.addAnimation("jump", sd.findAnimation("beginfly"));
		state.animate.addAnimation("idle", sd.findAnimation("idle"));
		state.animate.addAnimation("walk", sd.findAnimation("walk"));
		state.animate.addAnimation("run", sd.findAnimation("run"));
		state.animate.addAnimation("castspell", sd.findAnimation("castspell"));
		state.animate.addAnimation("swordattack",
				sd.findAnimation("overheadattack"));
		state.animate.addAnimation("drawbow", sd.findAnimation("drawbow"));
		state.animate.addAnimation("dead", sd.findAnimation("ded"));

		setupStates();

		buildPhysics(body);
	}

	private void buildPhysics(KinematicParticle body) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(body.getPos().x, body.getPos().y));
		Body collisionBody = WitchCraft.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		Fixture feetFixture = collisionBody.createFixture(fixture);

		shape = new PolygonShape();
		shape.setAsBox(35 * Util.PIXEL_TO_BOX, 4 * Util.PIXEL_TO_BOX,
				new Vector2(0, 16).scl(Util.PIXEL_TO_BOX), 0f);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		Fixture hitRadius = collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
		WitchCraft.rk4System.addParticle(body);

		state.phyState = new PhysicsState(body, collisionBody);
		state.phyState.addFixture(feetFixture, "feet");
		state.phyState.addFixture(hitRadius, "radius");

	}
}
