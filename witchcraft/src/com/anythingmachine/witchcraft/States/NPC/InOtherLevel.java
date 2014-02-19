package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.graphics.g2d.Batch;

public class InOtherLevel extends NPCState {
	private NPCState childState;
	
	public InOtherLevel(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	public void update(float dt) {
		checkInBounds();
		setGoingTo();
	}
	
	public void setGoingTo() {
		float levelwidth = GamePlayManager.levels.get(sm.npc.level);
		float activelevelwidth = GamePlayManager.levels.get(GamePlayManager.level);
		if (sm.npc.level < GamePlayManager.level && sm.phyState.body.getX() > levelwidth) {
			sm.phyState.body.setX(0);
			sm.setState(childState.name);
		}
		if (sm.npc.level > GamePlayManager.level && sm.phyState.body.getX() < activelevelwidth) {
			sm.phyState.body.setX(activelevelwidth);
			sm.setState(childState.name);
		}
	}
	public void draw(Batch batch) {

	}
	
	public void checkAttack() {

	}
	
	public void setAttack() {

	}

	public void setAlert() {

	}
		
	public void checkInBounds() {
		if( sm.npc.level == GamePlayManager.level ) {
			sm.setState(childState.name);
		}
	}

	public void takeAction(float dt) {
		if (aiChoiceTime > sm.behavior.getActionTime()) {
			takeAction(sm.behavior.ChooseAction(sm.state));
			aiChoiceTime = 0;
		}
	}
	
	public void setTalking(NonPlayer npc) {
		sm.npc = npc;
		childState = sm.getState(NPCStateEnum.TALKING);
	}
	
	public void setIdle() {
		sm.phyState.body.stop();
		childState = sm.getState(NPCStateEnum.IDLE);
	}
		
	public void takeAction(Action action) {
		if ( action != null )  {
			sm.behavior.takeAction(action);
			childState = sm.getState(action.getAIState());
			childState.transistionIn();
		}
	}
	
	public ActionEnum[] getPossibleActions() {
		return childState.getPossibleActions();
	}
	
	public void setWalk() {
		NPCState temp = childState;
		childState = sm.getState(NPCStateEnum.WALKING);
		childState.setParent(temp);
	}
	
	public void setRun() {
		NPCState temp = childState;
		childState = sm.getState(NPCStateEnum.RUNNING);
		childState.setParent(temp);
	}
	
	public void setParent(NPCState p) {
		childState = p;
	}
	
	public void transistionIn() {
		sm.phyState.body.setGravityVal(0);
		sm.phyState.collisionBody.setAwake(true);
		sm.active = false;
	}

	public void transistionOut() {
		sm.phyState.body.setGravityVal(Util.GRAVITY);
		sm.phyState.collisionBody.setAwake(false);
		sm.active = true;
	}

	public void immediateTransOut() {
		
	}

	public void checkGround() {
	}

}
