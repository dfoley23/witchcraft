package com.anythingmachine.witchcraft.agents.player;

import java.util.HashMap;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.Agent;
import com.anythingmachine.witchcraft.agents.player.Power.ConvertPower;
import com.anythingmachine.witchcraft.agents.player.Power.DeathPower;
import com.anythingmachine.witchcraft.agents.player.Power.FreezePower;
import com.anythingmachine.witchcraft.agents.player.Power.IntangibilityPower;
import com.anythingmachine.witchcraft.agents.player.Power.InvisiblePower;
import com.anythingmachine.witchcraft.agents.player.Power.MindControlPower;
import com.anythingmachine.witchcraft.agents.player.Power.Power;
import com.anythingmachine.witchcraft.agents.player.Power.ShapeShiftCatPower;
import com.anythingmachine.witchcraft.agents.player.Power.ShapeShiftCrowPower;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Platform;
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
	private boolean facingLeft;
	private boolean onGround;
	private Cape cape;
	private State playerState;
	private Bone neck;
	private InputManager input;
	private KinematicParticle body;
	private AnimationManager animate;
	private Body collisionBody;
	private boolean hitRoof;
	private HashMap<String, Power> powers;
	private Power power;

	public Player() {
		this.playerState = State.IDLE;
		this.curGroundSegment = 0;
		this.curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		this.onElevatedSegment = false;
		this.body = new KinematicParticle(new Vector3(32f,
				WitchCraft.ground.findPointOnCurve(curGroundSegment, 32f).y, 0f), Util.GRAVITY);
		WitchCraft.rk4.addComponent(body);
		this.input = new InputManager();
		hitRoof = false;
		setupInput();
		setupAnimations("player");

		neck = animate.findBone("neck");

		cape = new Cape(3, 5);
		buildCollisionBody();
		setupPowers();

		type = EntityType.PLAYER;

	}

	public void update(float dT) {

		float delta = Gdx.graphics.getDeltaTime();

		checkGround();

		boolean moving = false;
		if (input.is("Right") || input.is("Left")) {
			updateWalking();
			moving = true;
		} else {
			if (playerState.canBeIdle() && animate.isTimeOverAQuarter(delta)) {
				playerState = State.IDLE;
				animate.setCurrent("idle", true);
				animate.bindPose();
				body.setVel(0f, body.getVel().y, 0f);
			}
		}

		animate.setFlipX(facingLeft);

		if (input.is("Fly") && !hitRoof) {
			updateFlying();
		} else if ( hitRoof ) {
			body.apply2DImpulse(0, -1);
			hitRoof = false;
		} else if ( onElevatedSegment ){
			body.setVel(body.getVel().x, 0, 0);
		}

		if ( input.is("UsePower") ) {
			//power.usePower(playerState);
			animate.switchSkin("invi");
		}

		updateSkeleton(moving, delta);
		if (playerState.startingToFly())
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).mul(Util.PIXEL_TO_BOX),
					Util.HALF_PI);
		else
			collisionBody.setTransform(
					body.getPos2D().add(-8, 64).mul(Util.PIXEL_TO_BOX), 0);

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
		if (other.type == EntityType.PLATFORM) {
			Platform plat = (Platform)other;
			Vector2 pos = body.getPos2D();
			if ( plat.isBetween(pos.x) ) {
				if ( plat.getHeight()-64 < pos.y) {
					onElevatedSegment = true;
					this.elevatedSegment = plat;
					if (playerState.canLand())
						playerState = State.LANDING;
				} else {
					System.out.println(plat.getHeight());
					System.out.println(pos);
					//body.setPos(pos.x,plat.getHeight()-10, 0);
					body.setVel(body.getVel2D().x, -body.getVel2D().y, 0);
					hitRoof = true;
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
		if ( other.type == EntityType.PLATFORM ) {
			Platform plat = (Platform) other;
			if ( plat.equals(elevatedSegment)) 
				this.onElevatedSegment = false;
			if( !playerState.canLand() ) {
				playerState = State.FALLING;
			}
		}
	}
	
	private void updateFlying() {
		body.setVel(body.getVel().x, 90f, 0f);
		if (playerState.canFly()) {
			animate.setCurrent("jump", true);
			animate.bindPose();
			playerState = State.JUMPING;
		}
	}

	private void checkGround() {
		if (!onElevatedSegment) {
			Vector3 pos = body.getPos().cpy();
			if (pos.x > curCurve.lastPointOnCurve().x) {
				curGroundSegment++;
				curCurve = WitchCraft.ground.getCurve(curGroundSegment);
			} else if (pos.x < curCurve.firstPointOnCurve().x) {
				curGroundSegment--;
				curCurve = WitchCraft.ground.getCurve(curGroundSegment);
			}
			Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(curGroundSegment,
					pos.x);
			onGround = false;
			if (pos.y <= groundPoint.y) {
				correctHeight(groundPoint.y);
				onGround = true;
				playerState = playerState.land();
			}
		} else {
			float groundPoint = elevatedSegment.getHeight();
			onGround = false;
			if ( body.getPos().y < groundPoint) {
				correctHeight(groundPoint);
				onGround = true;
			}
		}
	}

	private void updateSkeleton(boolean moving, float delta) {
		if (playerState != State.FLYING) {
			cape.addWindForce(0, -400);
			if (animate.getTime() + delta > animate.getCurrentAnimTime()
					&& !moving) {
				playerState = State.IDLE;
				animate.setCurrent("idle", true);
				animate.bindPose();
				body.setVel(0, 0, 0);
			} else if (onGround || playerState != State.JUMPING
					|| animate.getTime() + delta < 0.95f) {
				animate.applyTotalTime(true, delta);
			} else {
				playerState = State.FLYING;
				if (facingLeft) {
					cape.addWindForce(-500, 0);
				} else {
					cape.addWindForce(-500, 0);
				}
			}
		}
		animate.setPos(body.getPos(), -8f, 0f);
		animate.updateSkel(delta);
		if (facingLeft) {
			if (playerState == State.FLYING) {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY() + 7,
						true, false);
			} else {
				cape.updatePos(neck.getWorldX() + 25, neck.getWorldY(), true,
						false);
			}
		} else {
			if (playerState == State.FLYING) {
				cape.updatePos(neck.getWorldX() + 5, neck.getWorldY() + 7,
						false, false);
			} else {
				cape.updatePos(neck.getWorldX() + 7, neck.getWorldY(), false,
						false);
			}
		}
	}

	private void updateWalking() {
		if (onGround && !input.is("Fly") && playerState.canWalk()) {
			animate.setCurrent("walk", true);
			animate.bindPose();
			playerState = State.WALKING;
		}
		if (input.is("Right")) {
			body.setVel(playerState.getInputSpeed(), body.getVel().y, 0f);
			facingLeft = false;
		} else if (input.is("Left")) {
			body.setVel(-playerState.getInputSpeed(), body.getVel().y, 0f);
			facingLeft = true;
		}
	}

	private void setupInput() {
		input.addInputState("Left", Keys.LEFT);
		input.addInputState("Right", Keys.RIGHT);
		input.addInputState("Fly", Keys.UP);
		input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
		input.addInputState("UsePower", Keys.SPACE);
		input.addInputState("Interact", Keys.A);
	}

	private void setupPowers() {
//		powers.put("mindcontrol", new MindControlPower());
//		powers.put("shapecrow", new ShapeShiftCrowPower());
//		powers.put("shapecat", new ShapeShiftCatPower());
//		powers.put("invisible", new InvisiblePower());
//		powers.put("intangible", new IntangibilityPower());
//		powers.put("convert", new ConvertPower());
//		powers.put("freeze", new FreezePower());
//		powers.put("death", new DeathPower());
	}

	private void setupAnimations(String name) {
		TextureAtlas atlas = WitchCraft.assetManager.getAtlas("player");

		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/player.skel"));

		animate = new AnimationManager(name, body.getPos(), new Vector2(0.5f,
				0.5f), false, sd);
		animate.addAnimation(
				"jump",
				sb.readAnimation(
						Gdx.files.internal("data/spine/player-jump.anim"), sd));
		animate.addAnimation(
				"walk",
				sb.readAnimation(
						Gdx.files.internal("data/spine/player-walk.anim"), sd));
		animate.addAnimation(
				"idle",
				sb.readAnimation(
						Gdx.files.internal("data/spine/player-idle.anim"), sd));
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
