package com.anythingmachine.witchcraft.agents;

import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.States.NPC.Attacking;
import com.anythingmachine.witchcraft.States.NPC.Drinking;
import com.anythingmachine.witchcraft.States.NPC.Eating;
import com.anythingmachine.witchcraft.States.NPC.GoingTo;
import com.anythingmachine.witchcraft.States.NPC.GoingToQuickly;
import com.anythingmachine.witchcraft.States.NPC.Idle;
import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;
import com.anythingmachine.witchcraft.States.NPC.Running;
import com.anythingmachine.witchcraft.States.NPC.Sleeping;
import com.anythingmachine.witchcraft.States.NPC.Walking;
import com.anythingmachine.witchcraft.States.NPC.Working;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
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

public class NonPlayer extends Entity {
	protected NPCStateMachine sm;
	protected boolean active;
	protected Fixture feetFixture;
	protected Fixture hitRadius;

	public NonPlayer(String skinname, String atlasname, Vector2 pos,
			Vector2 bodyScale) {
		this.type = EntityType.NONPLAYER;
		active = true;

		setupAnimations(skinname, atlasname, pos, bodyScale);

		setupTests();
		
		setupAI();
	}

	public void update(float dT) {
		sm.update(dT);
		//			switch (sm.state) {
//			case WALKINGLEFT:
//				body.setVel(-50f, body.getVel().y, 0f);
//				sm.animate.setCurrent("walk", true);
//				facingLeft = true;
//				if (old != NPCStateEnum.WALKINGLEFT) {
//					sm.animate.bindPose();
//				}
//				break;
//			case WALKINGRIGHT:
//				body.setVel(50f, body.getVel().y, 0f);
//				sm.animate.setCurrent("walk", true);
//				facingLeft = false;
//				if (old != NPCStateEnum.WALKINGRIGHT) {
//					sm.animate.bindPose();
//				}
//				break;
//			case IDLE:
//				body.setVel(0, 0, 0);
//				sm.animate.setCurrent("idle", true);
//				if (old != NPCStateEnum.IDLE) {
//					sm.animate.bindPose();
//				}
//				break;
//			case SWORDATTACK:
//				body.setVel(0, body.getVel().y, 0f);
//				sm.animate.bindPose();
//				sm.animate.setCurrent("swordattack", true);
//				break;
//			default:
//				handleState(sm.state);
//				// body.setVel(50f, body.getVel().y, 0f);
//				// animate.setCurrent("walk", true);
//				// facingLeft = false;
//				// if (old != AIState.WALKINGRIGHT) {
//				// animate.bindPose();
//				// }
//				break;
//			}
//		}
//		checkGround();
//
//		if (active) {
//			sm.animate.setPos(body.getPos(), 0, -16f);
//			if (sm.state == NPCState.SWORDATTACK && sm.animate.atEnd()) {
//				sm.animate.applyTotalTime(false, sm.animate.getCurrentAnimTime());
//			} else {
//				sm.animate.update(delta);
//			}
//		}

	}

	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.test("facingleft"));
		sm.animate.draw(batch);
	}

	public Vector3 getPosPixels() {
		return sm.phyState.getPos();
	}

	public Vector2 getBodyScale() {
		return sm.animate.getScale();
	}

	public Skin getSkin() {
		return sm.animate.getSkin();
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		Vector3 pos = sm.phyState.body.getPos();
		Vector3 vel = sm.phyState.body.getVel();
		switch (other.type) {
		case WALL:
			// System.out.println("hello wall");
			if ( vel.x < 0 ) {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.setTestVal("hitleftwall", true);
			} else {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.setTestVal("hitrightwall", true);				
			}
			break;
		case LEVELWALL:
			// System.out.println("hello wall");
			if ( vel.x < 0 ) {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.setTestVal("hitleftwall", true);
			} else {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.setTestVal("hitrightwall", true);				
			}

			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(sm.test("facingleft"), pos.x)) {
				if (plat.getHeight() - 32 < pos.y)
					sm.elevatedSegment = plat;
			}
			break;
		default:
			break;
		}
	}

	public void correctHeight(float y) {
		sm.phyState.body.setPos(sm.phyState.getX(), y, 0f);
	}

	protected void setupAnimations(String skinname, String atlasname, Vector2 pos, Vector2 scale) {
		SkeletonBinary sb = new SkeletonBinary(
				(TextureAtlas) WitchCraft.assetManager
						.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		KinematicParticle body = new KinematicParticle(new Vector3(pos.x, 128f, 0f), -50f);

		sm = new NPCStateMachine(skinname, body.getPos(), scale, false, sd);

		sm.animate.addAnimation("idle", sd.findAnimation("idle"));
		sm.animate.addAnimation("walk", sd.findAnimation("walk"));
		sm.animate.addAnimation("run", sd.findAnimation("run"));
		sm.animate.addAnimation("swordattack", sd.findAnimation("overheadattack"));
		sm.animate.addAnimation("drawbow", sd.findAnimation("drawbow"));

		sm.animate.setCurrent("idle", true);
				
		buildPhysics(body);
		
		setupStates();
		sm.setInitialState(NPCStateEnum.IDLE);
	}

	protected void setupStates() {
		sm.addState(NPCStateEnum.ATTACKING, new Attacking(sm, NPCStateEnum.ATTACKING, this));
		sm.addState(NPCStateEnum.WALKING, new Walking(sm, NPCStateEnum.WALKING));
		sm.addState(NPCStateEnum.RUNNING, new Running(sm, NPCStateEnum.RUNNING));
		sm.addState(NPCStateEnum.GOINGTOEAT, new GoingTo(sm, NPCStateEnum.GOINGTOEAT, new Vector2(6612, sm.phyState.getY())));
		sm.addState(NPCStateEnum.GOINGTOSLEEP, new GoingTo(sm, NPCStateEnum.GOINGTOSLEEP, new Vector2(6612, sm.phyState.getY())));
		sm.addState(NPCStateEnum.GOINGTOWORK, new GoingToQuickly(sm, NPCStateEnum.GOINGTOWORK, new Vector2(4012, sm.phyState.getY())));
		sm.addState(NPCStateEnum.EATING, new Eating(sm, NPCStateEnum.EATING));
		sm.addState(NPCStateEnum.SLEEPING, new Sleeping(sm, NPCStateEnum.SLEEPING));
		sm.addState(NPCStateEnum.DRINKING, new Drinking(sm, NPCStateEnum.DRINKING));
		sm.addState(NPCStateEnum.IDLE, new Idle(sm, NPCStateEnum.IDLE));
		sm.addState(NPCStateEnum.WORKING, new Working(sm, NPCStateEnum.WORKING));
	}
	
	protected void setupAI() {
		Random rand = new Random();
		// ai goals and actions
		sm.behavior = new UtilityAI(7f);
		sm.behavior.addGoal(new Goal("Eat", rand.nextInt(5) + 1));
		sm.behavior.addGoal(new Goal("Sleep", rand.nextInt(5) + 1));
		sm.behavior.addGoal(new Goal("Hunt", rand.nextInt(20) + 7));
		sm.behavior.addGoal(new Goal("notidle", rand.nextInt(7) + 7));
		Action action = new Action(ActionEnum.EAT, NPCStateEnum.EATING);
		action.addAction(-4f, "Eat");
		action.addAction(2f, "Sleep");
		action.addAction(2f, "Hunt");
		action.addAction(-2f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.EAT, NPCStateEnum.EATING);
		action.addAction(-7f, "Eat");
		action.addAction(2f, "Sleep");
		action.addAction(3f, "Hunt");
		action.addAction(-1f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.DRINK, NPCStateEnum.DRINKING);
		action.addAction(-3f, "Eat");
		action.addAction(4f, "Sleep");
		action.addAction(1f, "Hunt");
		action.addAction(1f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.GOTOSLEEP, NPCStateEnum.GOINGTOSLEEP);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(2f, "Hunt");
		action.addAction(-6f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.SLEEP, NPCStateEnum.SLEEPING);
		action.addAction(4f, "Eat");
		action.addAction(-10f, "Sleep");
		action.addAction(3f, "Hunt");
		action.addAction(1f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.THINK, NPCStateEnum.IDLE);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-1f, "Hunt");
		action.addAction(3f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.GO, NPCStateEnum.GOINGTOWORK);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-5f, "Hunt");
		action.addAction(-2f, "notidle");
		sm.behavior.addAction(action);
		action = new Action(ActionEnum.GO, NPCStateEnum.GOINGTOEAT);
		action.addAction(1f, "Eat");
		action.addAction(2f, "Sleep");
		action.addAction(2f, "Hunt");
		action.addAction(-6f, "notidle");
		sm.behavior.addAction(action);

	}

	protected void setupTests() {
		sm.addTest("facingleft", false);
		sm.addTest("grounded", true);
	}

	protected void buildPhysics(KinematicParticle body) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(body.getX(), body.getY()));
		Body collisionBody = WitchCraft.world.createBody(def);
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
		
		WitchCraft.rk4System.addParticle(body);
		sm.phyState = new PhysicsState(body, collisionBody);
		sm.phyState.addFixture(feetFixture, "feet");
		sm.phyState.addFixture(hitRadius, "radius");
		
	}
}
