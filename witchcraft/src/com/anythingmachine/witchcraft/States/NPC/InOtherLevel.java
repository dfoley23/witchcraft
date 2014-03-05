package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.badlogic.gdx.graphics.g2d.Batch;

public class InOtherLevel extends NPCState {
	private NPCState childState;
	private float waittime;

	public InOtherLevel(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		waittime = 0;
	}

	@Override
	public void update(float dt) {
		aiChoiceTime += dt;
		waittime += dt;
		
		childState.setGoingTo(waittime);
		
		childState.checkTarget();

		takeAction(dt);

		checkInLevel();
	}

	@Override
	public void setChildState(NPCStateEnum state) {
		childState = sm.getState(state);
		childState.transistionIn();
	}

	@Override
	public void checkInLevel() {
		if (sm.me.level == GamePlayManager.currentlevel) {
			sm.setState(childState.name);
		}
	}

	@Override
	public void draw(Batch batch) {

	}

	@Override
	public void switchLevel(int level) {	
		if ( level-1 > sm.me.level ) {
			sm.phyState.body.setX(64);
		} else {
			sm.phyState.body.setX(GamePlayManager.levels.get(level-1)-64);
		}
		sm.me.level = level-1;
		if ( sm.me.level == GamePlayManager.currentlevel ) {
			sm.setState(childState.name);			
		}
	}

	@Override
	public void takeAction(float dt) {
		if (aiChoiceTime > sm.behavior.getActionTime()) {
			takeAction(sm.behavior.ChooseAction(childState));
			aiChoiceTime = 0;
		}
	}
	
	@Override
	public void setTalking(NonPlayer npc) {
		sm.npc = npc;
		childState = sm.getState(NPCStateEnum.TALKING);
		childState.transistionIn();
	}

	@Override
	public void setIdle() {
		childState = sm.getState(NPCStateEnum.IDLE);
		childState.transistionIn();
	}

	@Override
	public void takeAction(Action action) {
		if (action != null) {			
			sm.behavior.takeAction(action);
			childState = sm.getState(action.getAIState());
			childState.transistionIn();
		}
	}

	@Override
	public void setWalk() {
	}

	@Override
	public void setRun() {
	}

	@Override
	public void setParent(NPCState p) {
		childState = p;
	}

	@Override
	public void transistionIn() {
		sm.phyState.body.stop();
		sm.phyState.body.setGravityVal(0);
		sm.phyState.collisionBody.setAwake(true);
		sm.inlevel = false;
		sm.onscreen = false;
		// sm.active = false;
	}

	@Override
	public boolean transistionOut() {
		if (sm.me.level == GamePlayManager.currentlevel) {
			sm.phyState.body.setGravityVal(Util.GRAVITY);
			sm.phyState.collisionBody.setAwake(false);
			sm.inlevel = true;
			// sm.active = true;
			return true;
		}
		return false;
	}

	@Override
	public void immediateTransOut() {

	}

	@Override
	public void checkGround() {
	}

}
