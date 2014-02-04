package com.anythingmachine.witchcraft.agents;

import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.aiengine.UtilityAI.AIState;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.esotericsoftware.spine.Skin;

public class NonPlayer extends Agent {
	protected boolean inAir;
	protected UtilityAI behavior;
	protected float aiChoiceTime = 0.f;
	protected AIState state = AIState.IDLE;
	protected Vector2 bodyScale;
	protected boolean active;
	protected AnimationManager animate;
	protected boolean facingLeft;
	protected boolean onGround;
	protected KinematicParticle body;
	protected Body collisionBody;
	protected Fixture feetFixture;
	protected Fixture hitRadius;

	public NonPlayer(String skinname, String atlasname, Vector2 pos,
			Vector2 bodyScale) {
		this.bodyScale = bodyScale;
		this.type = EntityType.NONPLAYER;
		this.curGroundSegment = 0;
		active = true;
		// ArrayList<Vector2> points = WitchCraft.ground.getCurveBeginPoints();
		// for (int i = 0; i < points.size(); i++) {
		// if (pos.x > points.get(i).x) {
		// this.curGroundSegment = i;
		// }
		// }
		//
		// curCurve = WitchCraft.ground.getCurve(curGroundSegment);

		this.body = new KinematicParticle(new Vector3(pos.x, 128f, 0f), -50f);
		WitchCraft.rk4System.addParticle(body);

		setupAnimations(skinname, atlasname);

		setupAI();
		buildCollisionBody();
	}

	public void update(float dT) {
		float delta = Gdx.graphics.getDeltaTime();

		aiChoiceTime += dT;
		if (aiChoiceTime > 7) {
			AIState old = state;
			state = behavior.ChooseAction();
			aiChoiceTime = 0;
			switch (state) {
			case WALKINGLEFT:
				body.setVel(-50f, body.getVel().y, 0f);
				animate.setCurrent("walk", true);
				facingLeft = true;
				if (old != AIState.WALKINGLEFT) {
					animate.bindPose();
				}
				break;
			case WALKINGRIGHT:
				body.setVel(50f, body.getVel().y, 0f);
				animate.setCurrent("walk", true);
				facingLeft = false;
				if (old != AIState.WALKINGRIGHT) {
					animate.bindPose();
				}
				break;
			case IDLE:
				body.setVel(0, 0, 0);
				animate.setCurrent("idle", true);
				if (old != AIState.IDLE) {
					animate.bindPose();
				}
				break;
			case SWORDATTACK:
				body.setVel(0, body.getVel().y, 0f);
				animate.bindPose();
				animate.setCurrent("swordattack", true);
				break;
			default:
				handleState(state);
				// body.setVel(50f, body.getVel().y, 0f);
				// animate.setCurrent("walk", true);
				// facingLeft = false;
				// if (old != AIState.WALKINGRIGHT) {
				// animate.bindPose();
				// }
				break;
			}
		}
		checkGround();

		if (active) {
			animate.setPos(body.getPos(), 0, -16f);
			if (state == AIState.SWORDATTACK && animate.atEnd()) {
				animate.applyTotalTime(false, animate.getCurrentAnimTime());
			} else {
				animate.update(delta);
			}
		}
		collisionBody.setTransform(Util.addVecsToVec2(body.getPos(), -8, 64).scl(Util.PIXEL_TO_BOX), 0);

	}

	public void draw(Batch batch) {
		animate.setFlipX(facingLeft);
		animate.draw(batch);
	}

	public Vector3 getPosPixels() {
		return body.getPos();
	}

	public int getCurSegment() {
		return curGroundSegment;
	}

	public Vector2 getBodyScale() {
		return bodyScale;
	}

	public void incrementSegment() {
		this.curGroundSegment++;
	}

	public void decrementSegment() {
		this.curGroundSegment++;
	}

	public Skin getSkin() {
		return animate.getSkin();
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		Vector3 pos = body.getPos();
		Vector3 vel = body.getVel();
		switch (other.type) {
		case WALL:
			// System.out.println("hello wall");
			body.setVel(-vel.x, vel.y, 0);
			facingLeft = !facingLeft;
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(facingLeft, pos.x)) {
				if (plat.getHeight() - 32 < pos.y)
					elevatedSegment = plat;
			}
			break;
		default:
			break;
		}
	}

	protected void checkGround() {
		Vector3 pos = body.getPos();
//		if (pos.x > curCurve.lastPointOnCurve().x) {
//			curGroundSegment++;
//			if (curGroundSegment >= WitchCraft.ground.getNumCurves()) {
//				body.setVel(-50, 0, 0);
//				facingLeft = !facingLeft;
//			}
//			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
//		} else if (pos.x < curCurve.firstPointOnCurve().x) {
//			curGroundSegment--;
//			if (curGroundSegment == 0) {
//				body.setVel(50, 0, 0);
//				facingLeft = !facingLeft;
//			}
//			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
//		}
//		animate.setFlipX(facingLeft);
//		Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
//				curGroundSegment, pos.x);
//		if (pos.y < groundPoint.y) {
//			correctHeight(groundPoint.y);
//			onGround = true;
//		}
		onGround = false;
		if (elevatedSegment != null && elevatedSegment.isBetween(facingLeft, pos.x)) {
			float groundPoint = elevatedSegment.getHeight(pos.x);
//			if (pos.y < groundPoint) {
				body.setPos(pos.x, groundPoint, 0);
				onGround=true;
//			}
		} 
	}

	public void correctHeight(float y) {
		body.setPos(body.getPos().x, y, 0f);
	}

	protected void handleState(AIState state) {
	}

	protected void setupAnimations(String skinname, String atlasname) {
		SkeletonBinary sb = new SkeletonBinary(
				(TextureAtlas) WitchCraft.assetManager
						.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		animate = new AnimationManager(skinname, body.getPos(), new Vector2(
				0.6f, 0.7f), true, sd);
		animate.addAnimation("idle", sd.findAnimation("idle"));
		animate.addAnimation("walk", sd.findAnimation("walk"));
		animate.addAnimation("run", sd.findAnimation("run"));
		animate.addAnimation("swordattack", sd.findAnimation("overheadattack"));

		animate.setCurrent("idle", true);

	}

	protected void setupAI() {
		Random rand = new Random();
		// ai goals and actions
		behavior = new UtilityAI();
		behavior.addGoal(new Goal("Eat", rand.nextInt(5) + 1));
		behavior.addGoal(new Goal("Sleep", rand.nextInt(5) + 1));
		behavior.addGoal(new Goal("Hunt", rand.nextInt(20) + 7));
		Action action = new Action("eatBread", AIState.IDLE);
		action.addAction(-5f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(3f, "Hunt");
		behavior.addAction(action);
		action = new Action("eatMeat", AIState.IDLE);
		action.addAction(-7f, "Eat");
		action.addAction(2f, "Sleep");
		action.addAction(3f, "Hunt");
		behavior.addAction(action);
		action = new Action("sleepInHouse", AIState.IDLE);
		action.addAction(3f, "Eat");
		action.addAction(-7f, "Sleep");
		action.addAction(5f, "Hunt");
		behavior.addAction(action);
		action = new Action("sleepInBed", AIState.IDLE);
		action.addAction(3f, "Eat");
		action.addAction(-6f, "Sleep");
		action.addAction(5f, "Hunt");
		behavior.addAction(action);
		action = new Action("standIdle", AIState.IDLE);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-5f, "Hunt");
		behavior.addAction(action);
		action = new Action("walkLeft", AIState.WALKINGLEFT);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-8f, "Hunt");
		behavior.addAction(action);
		action = new Action("walkRight", AIState.WALKINGRIGHT);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-4f, "Hunt");
		behavior.addAction(action);

	}

	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.body.getPos().x, this.body.getPos().y));
		collisionBody = WitchCraft.world.createBody(def);
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
		shape.setAsBox(64 * Util.PIXEL_TO_BOX, 32 * Util.PIXEL_TO_BOX);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		feetFixture = collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
	}
}
