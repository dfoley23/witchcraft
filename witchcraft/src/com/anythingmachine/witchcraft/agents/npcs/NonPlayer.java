package com.anythingmachine.witchcraft.agents.npcs;

import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.physicsEngine.particleEngine.particles.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.States.NPC.NPCState;
import com.anythingmachine.witchcraft.agents.States.NPC.NPCStateEnum;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;
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
	public String datafile;
	public int level;
	public NPCType npctype;

	public NonPlayer(String skinname, Vector2 pos, Vector2 bodyScale,
			String datafile, NPCType npctype) {
		this.type = EntityType.NONPLAYER;
		this.datafile = datafile;
		this.npctype = npctype;

		FileHandle handle = Gdx.files.internal(datafile);
		String[] fileContent = handle.readString().split("\n");

		level = Integer.parseInt(fileContent[0].split(",")[1]) - 1;

		setupAnimations(skinname, pos, bodyScale, fileContent);

		setupAI(fileContent);
		
		
	}

	public void update(float dT) {
		sm.update(dT);
	}

	public void checkInLevel() {
		sm.state.checkInLevel();
	}

	@Override
	public void setTalking(NonPlayer npc) {
		sm.state.setTalking(npc);
	}

	public void setCinematic() {
		sm.state.setCinematic();
	}
	
	@Override
	public void setParentState() {
		sm.state.transistionToParent();
	}
	
	@Override
	public void setStateByValue(String strvalue) {
		sm.setState(NPCStateEnum.valueOf(strvalue));
	}
	
	@Override
	public void setParentByValue(String strvalue) {
		sm.state.setParent(sm.getState(NPCStateEnum.valueOf(strvalue)));
	}
		
	public void draw(Batch batch) {
		sm.state.draw(batch);
	}
	@Override
	public void stop() {
		sm.state.setIdle();
	}

	@Override
	public void stopOnX() {		
		sm.state.setIdle();
	}

	@Override
	public void stopOnY() {
		sm.state.setIdle();
	}

	@Override
	public void addPos(float x, float y) {
		sm.phyState.body.addPos(x, y);
	}
	
	@Override
	public void faceLeft(boolean val) {
		sm.facingleft = val;
	}

	@Override
	public void switchBloodSword() {
		sm.animate.setRegion("right hand item", "sworda", "bloodysworda",
				false, 1, 1);
	}

	public Vector3 getPosPixels() {
		return sm.phyState.body.getPos();
	}

	public Vector2 getBodyScale() {
		return sm.animate.getScale();
	}

	public Skin getSkin() {
		return sm.animate.getSkin();
	}

	public boolean isCritcalAttacking() {
		return sm.inState(NPCStateEnum.ATTACKING)
				&& sm.animate.isTimeOverAQuarter(0);
	}

	public float getX() {
		return sm.phyState.body.getX();
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		sm.state.handleContact(contact, isFixture1);
	}

	protected void setupAnimations(String skinname, Vector2 pos, Vector2 scale,
			String[] fileContent) {
		SkeletonBinary sb = new SkeletonBinary(
				(TextureAtlas) WitchCraft.assetManager
						.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		KinematicParticle body = new KinematicParticle(new Vector3(pos.x, pos.y,
				0f), -50f);
//		body.useEuler(false);

		
		sm = new NPCStateMachine(skinname, body.getPos(), scale, false, sd,
				this);

		String[] animations = fileContent[1].split(",");

		for (int i = 1; i < animations.length; i++) {
			sm.animate.addAnimation(animations[i],
					sd.findAnimation(animations[i]));

		}

		sm.animate.setCurrent(fileContent[2].split(",")[1], true);

		buildPhysics(body);

		setupStates(fileContent);

		String[] statechain = fileContent[4].split(",");
		String initState = statechain[1];
		for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
			if (state.getName().equals(initState)) {
				if (this.level != GamePlayManager.currentlevel) {
					sm.setInitialState(state);
					NPCState temp = sm.state;
					sm.setState(NPCStateEnum.INOTHERLEVEL);
					sm.state.setParent(temp);
					break;
				} else {
					sm.setInitialState(state);
					break;
				}
			}
		}
	}

	protected void setupStates(String[] filecontent) {
		String[] states = filecontent[3].split(",");
		for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
			for (int i = 1; i < states.length; i++) {
				if (!sm.stateExists(state)) {
					String statename = states[i];
					if (states[i].contains(";")) {
						statename = states[i].split(";")[0];
					}
					if (state.getName().equals(statename)) {
						if (states[i].contains(";")) {
							String[] pos = states[i].split(";");
							sm.addState(
									state,
									state.constructState(
											sm,
											new Vector2(
													Float.parseFloat(pos[1]),
													pos[2].equals("Y") ? sm.phyState.body
															.getY() : Float
															.parseFloat(pos[2])),
											Integer.parseInt(pos[3])));
						} else {
							sm.addState(state, state.constructState(sm));
						}
					} else if (state.isGlobal()) {
						sm.addState(state, state.constructState(sm));
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
		int l = 7;
		String line = filecontent[l];
		while (!line.equals("ENDACTIONS")) {
			String[] action = line.split(",");
			for (ActionEnum a : ActionEnum.EAT.values()) {
				if (a.getName().equals(action[0])) {
					for (NPCStateEnum state : NPCStateEnum.DEAD.values()) {
						if (state.getName().equals(action[1])) {
							Action act = new Action(a, state);
							for (int i = 1; i < goals.length; i++) {
								act.addAction(Float.parseFloat(action[1 + i]),
										goals[i]);
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
		Body collisionBody = GamePlayManager.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_NPC;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		feetFixture = collisionBody.createFixture(fixture);

		shape = new PolygonShape();
		shape.setAsBox(64 * Util.PIXEL_TO_BOX, 32 * Util.PIXEL_TO_BOX);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_NPC;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		feetFixture = collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();

		GamePlayManager.rk4System.addParticle(body);
		sm.phyState = new PhysicsState(body, collisionBody);
		sm.phyState.addFixture(feetFixture, "feet");
		sm.phyState.addFixture(hitRadius, "radius");

	}
}
