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
import com.anythingmachine.witchcraft.States.NPC.Clean;
import com.anythingmachine.witchcraft.States.NPC.Drinking;
import com.anythingmachine.witchcraft.States.NPC.Eating;
import com.anythingmachine.witchcraft.States.NPC.GoingTo;
import com.anythingmachine.witchcraft.States.NPC.Idle;
import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;
import com.anythingmachine.witchcraft.States.NPC.Patrol;
import com.anythingmachine.witchcraft.States.NPC.Running;
import com.anythingmachine.witchcraft.States.NPC.Sleeping;
import com.anythingmachine.witchcraft.States.NPC.Walking;
import com.anythingmachine.witchcraft.States.NPC.Working;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
	protected Fixture feetFixture;
	protected Fixture hitRadius;
	protected String datafile;

	public NonPlayer(String skinname, String atlasname, Vector2 pos,
			Vector2 bodyScale, String datafile) {
		this.type = EntityType.NONPLAYER;
		this.datafile = datafile;

		FileHandle handle = Gdx.files.internal(datafile);
		String[] fileContent = handle.readString().split("\n");

		setupAnimations(skinname, atlasname, pos, bodyScale, fileContent);

		setupAI(fileContent);
	}

	public void update(float dT) {
		if ( sm.active ) {
			sm.canseeplayer = sm.facingleft == WitchCraft.player.getX() < sm.phyState.getX();
			if ( WitchCraft.player.inHighAlert() ) {
				sm.state.setAttack();
			} else if ( WitchCraft.player.inAlert() ) {
				sm.state.setAlert();
			}
		}
		sm.update(dT);
		// switch (sm.state) {
		// case WALKINGLEFT:
		// body.setVel(-50f, body.getVel().y, 0f);
		// sm.animate.setCurrent("walk", true);
		// facingLeft = true;
		// if (old != NPCStateEnum.WALKINGLEFT) {
		// sm.animate.bindPose();
		// }
		// break;
		// case WALKINGRIGHT:
		// body.setVel(50f, body.getVel().y, 0f);
		// sm.animate.setCurrent("walk", true);
		// facingLeft = false;
		// if (old != NPCStateEnum.WALKINGRIGHT) {
		// sm.animate.bindPose();
		// }
		// break;
		// case IDLE:
		// body.setVel(0, 0, 0);
		// sm.animate.setCurrent("idle", true);
		// if (old != NPCStateEnum.IDLE) {
		// sm.animate.bindPose();
		// }
		// break;
		// case SWORDATTACK:
		// body.setVel(0, body.getVel().y, 0f);
		// sm.animate.bindPose();
		// sm.animate.setCurrent("swordattack", true);
		// break;
		// default:
		// handleState(sm.state);
		// // body.setVel(50f, body.getVel().y, 0f);
		// // animate.setCurrent("walk", true);
		// // facingLeft = false;
		// // if (old != AIState.WALKINGRIGHT) {
		// // animate.bindPose();
		// // }
		// break;
		// }
		// }
		// checkGround();
		//
		// if (active) {
		// sm.animate.setPos(body.getPos(), 0, -16f);
		// if (sm.state == NPCState.SWORDATTACK && sm.animate.atEnd()) {
		// sm.animate.applyTotalTime(false, sm.animate.getCurrentAnimTime());
		// } else {
		// sm.animate.update(delta);
		// }
		// }

	}

	public void setTalking(NonPlayer npc) {
		sm.state.setTalking(npc);
	}
	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.facingleft);
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
		case NONPLAYER:
			sm.hitnpc = true;
			sm.npc = (NonPlayer) other;
			break;
		case WALL:
			// System.out.println("hello wall");
			if (vel.x < 0) {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.hitleftwall = true;
			} else {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.hitrightwall = true;
			}
			break;
		case LEVELWALL:
			// System.out.println("hello wall");
			if (vel.x < 0) {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.hitleftwall = true;
			} else {
				sm.phyState.setVel(-vel.x, vel.y);
				sm.hitrightwall = true;
			}

			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(sm.facingleft, pos.x)) {
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

	protected void setupAnimations(String skinname, String atlasname,
			Vector2 pos, Vector2 scale, String[] fileContent) {
		SkeletonBinary sb = new SkeletonBinary(
				(TextureAtlas) WitchCraft.assetManager
						.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		KinematicParticle body = new KinematicParticle(new Vector3(pos.x, 128f,
				0f), -50f);

		sm = new NPCStateMachine(skinname, body.getPos(), scale, false, sd, this);

		String[] animations = fileContent[1].split(",");

		for (int i = 1; i < animations.length; i++) {
			sm.animate.addAnimation(animations[i],
					sd.findAnimation(animations[i]));

		}
		// sm.animate.addAnimation("idle", sd.findAnimation("idle"));
		// sm.animate.addAnimation("walk", sd.findAnimation("walk"));
		// sm.animate.addAnimation("run", sd.findAnimation("run"));
		// sm.animate.addAnimation("swordattack",
		// sd.findAnimation("overheadattack"));
		// sm.animate.addAnimation("drawbow", sd.findAnimation("drawbow"));

		sm.animate.setCurrent(fileContent[2].split(",")[1], true);

		buildPhysics(body);

		setupStates(fileContent);

		String initState = fileContent[4].split(",")[1];
		for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
			if (state.getName().equals(initState)) {
				sm.setInitialState(state);				
			}
		}
	}

	protected void setupStates(String[] filecontent) {
		String[] states = filecontent[3].split(",");
		for (int i = 1; i < states.length; i++) {
			for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
				String statename = states[i];
				if ( states[i].contains(";")) {
					statename = states[i].split(";")[0];
				}
				if (state.getName().equals(statename)) {
					switch (state) {
					case IDLE:
						sm.addState(NPCStateEnum.IDLE, new Idle(sm,
								NPCStateEnum.IDLE));
						break;
					case WALKING:
						sm.addState(NPCStateEnum.WALKING, new Walking(sm,
								NPCStateEnum.WALKING));
						break;
					case RUNNING:
						sm.addState(NPCStateEnum.RUNNING, new Running(sm,
								NPCStateEnum.RUNNING));
						break;
					case DEAD:
						break;
					case ATTACKING:
						sm.addState(NPCStateEnum.ATTACKING, new Attacking(sm,
								NPCStateEnum.ATTACKING, this));
						break;
					case EATING:
						sm.addState(NPCStateEnum.EATING, new Eating(sm,
								NPCStateEnum.EATING));
						break;
					case SLEEPING:
						sm.addState(NPCStateEnum.SLEEPING, new Sleeping(sm,
								NPCStateEnum.SLEEPING));
						break;
					case TALKING:
						break;
					case CLEANING:
						sm.addState(NPCStateEnum.CLEANING, new Clean(sm,
								NPCStateEnum.CLEANING));
						break;
					case HUNTING:
						break;
					case GUARDING:
						break;
					case PATROLLING:
						sm.addState(NPCStateEnum.PATROLLING, new Patrol(sm,
								NPCStateEnum.PATROLLING));
						break;
					case FOLLOWING:
						break;
					case LEADING:
						break;
					case GOINGTOEAT:
						String[] pos = states[i].split(";");
						sm.addState(
								NPCStateEnum.GOINGTOEAT,
								new GoingTo(
										sm,
										NPCStateEnum.GOINGTOEAT,
										new Vector2(
												Float.parseFloat(pos[1]),
												pos[2].equals("Y") ? sm.phyState
														.getY() : Float
														.parseFloat(pos[2]))));
						break;
					case GOINGTOSLEEP:
						pos = states[i].split(";");
						sm.addState(
								NPCStateEnum.GOINGTOSLEEP,
								new GoingTo(
										sm,
										NPCStateEnum.GOINGTOSLEEP,
										new Vector2(
												Float.parseFloat(pos[1]),
												pos[2].equals("Y") ? sm.phyState
														.getY() : Float
														.parseFloat(pos[2]))));
						break;
					case MOBBING:
						break;
					case DRINKING:
						sm.addState(NPCStateEnum.DRINKING, new Drinking(sm,
								NPCStateEnum.DRINKING));
						break;
					case DRUNK:
						break;
					case ALARMED:
						break;
					case TIRED:
						break;
					case THEIVING:
						break;
					case GOINGTOWORK:
						pos = states[i].split(";");
						sm.addState(
								NPCStateEnum.GOINGTOWORK,
								new GoingTo(
										sm,
										NPCStateEnum.GOINGTOWORK,
										new Vector2(
												Float.parseFloat(pos[1]),
												pos[2].equals("Y") ? sm.phyState
														.getY() : Float
														.parseFloat(pos[2]))));
						break;
					case WORKING:
						sm.addState(NPCStateEnum.WORKING, new Working(sm,
								NPCStateEnum.WORKING));
						break;
					case RETURNFROMWORK:
						break;
					case GOINGTOPATROL:
						pos = states[i].split(";");
						sm.addState(
								NPCStateEnum.GOINGTOPATROL,
								new GoingTo(
										sm,
										NPCStateEnum.GOINGTOPATROL,
										new Vector2(
												Float.parseFloat(pos[1]),
												pos[2].equals("Y") ? sm.phyState
														.getY() : Float
														.parseFloat(pos[2]))));
						break;
					}
				}
			}
		}
	}

	protected void setupAI(String[] filecontent) {
		Random rand = new Random();
		// ai goals and actions
		sm.behavior = new UtilityAI(7f);
		String[] goals = filecontent[5].split(",");
		for (int i = 1; i < goals.length; i++) {
			float val = Float.parseFloat(goals[i].split(";")[1]);
			sm.behavior.addGoal(new Goal(goals[i], val));
		}
		int l= 7;
		String line = filecontent[l];
		while (!line.equals("ENDACTIONS")) {
			String[] action = line.split(",");
			for (ActionEnum a : ActionEnum.EAT.values()) {
				if (a.getName().equals(action[0])) {
					for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
						if (state.getName().equals(action[1])) {
							Action act = new Action(a,state);
							for ( int i = 1; i< goals.length; i++ ) {
								act.addAction(Float.parseFloat(action[1+i]), goals[i]);
							}
							sm.behavior.addAction(act);							
							break;
						}
					}
					break;
				}
			}
			l++;			
			line = filecontent[l];
		}
	}

	protected void buildPhysics(KinematicParticle body) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(body.getX(), body.getY()));
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
