package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class InOtherLevel extends NPCState {
	private NPCState childState;

	public InOtherLevel(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}

	public void update(float dt) {
		childState.checkTarget();

		setGoingTo();
	}

	@Override
	public void setChildState(NPCStateEnum state) {
		childState = sm.getState(state);
	}

	public void setGoingTo() {
		float levelwidth = GamePlayManager.levels.get(sm.npc.level);
		if (sm.phyState.body.getX() > levelwidth) {
			if (sm.npc.level != GamePlayManager.levels.size() - 1) {
				sm.npc.level += 1;
				if (sm.npc.level == GamePlayManager.currentlevel) {
					sm.phyState.body.setX(0);
					sm.setState(childState.name);
				}
			} else {
				sm.hitrightwall = true;
				sm.phyState.body.stopOnX();
			}
		}
		if (sm.phyState.body.getX() < 0) {
			if (sm.npc.level != 0) {
				sm.npc.level -= 1;
				if (sm.npc.level == GamePlayManager.currentlevel) {
					float activelevelwidth = GamePlayManager.levels
							.get(sm.npc.level);
					sm.phyState.body.setX(activelevelwidth);
					sm.setState(childState.name);
				} 
			}else {
					sm.hitleftwall = true;
					sm.phyState.body.stopOnX();
			}
		}  else if ( sm.npc.level == GamePlayManager.currentlevel ) {
			sm.setState(childState.name);
		}
	}

	public void draw(Batch batch) {

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
		if (action != null) {
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
		Gdx.app.log("set out of level", this.toString());
		if ( childState == null ) {
			childState = sm.getState(NPCStateEnum.IDLE);
		}
		sm.phyState.body.setGravityVal(0);
		sm.phyState.collisionBody.setAwake(true);
		sm.active = false;
	}

	public boolean transistionOut() {
		sm.phyState.body.setGravityVal(Util.GRAVITY);
		sm.phyState.collisionBody.setAwake(false);
		sm.active = true;
		return true;
	}

	public void immediateTransOut() {

	}

	public void checkGround() {
	}

}
