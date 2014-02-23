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

	@Override
	public void update(float dt) {
		aiChoiceTime += dt;

		childState.checkTarget();

		takeAction(dt);

		checkInLevel();
	}

	@Override
	public void setChildState(NPCStateEnum state) {
		childState = sm.getState(state);
	}

	@Override
	public void checkInLevel() {
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
			} else {
				sm.hitleftwall = true;
				sm.phyState.body.stopOnX();
			}
		} else if (sm.npc.level == GamePlayManager.currentlevel) {
			sm.setState(childState.name);
		}
	}

	@Override
	public void draw(Batch batch) {

	}

	@Override
	public void takeAction(float dt) {
		if (aiChoiceTime > sm.behavior.getActionTime()) {
			takeAction(sm.behavior.ChooseAction(sm.state));
			aiChoiceTime = 0;
		}
	}

	@Override
	public void setTalking(NonPlayer npc) {
		sm.npc = npc;
		childState = sm.getState(NPCStateEnum.TALKING);
	}

	@Override
	public void setIdle() {
		sm.phyState.body.stop();
		childState = sm.getState(NPCStateEnum.IDLE);
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
	public ActionEnum[] getPossibleActions() {
		return childState.getPossibleActions();
	}

	@Override
	public void setWalk() {
		NPCState temp = childState;
		childState = sm.getState(NPCStateEnum.WALKING);
		childState.setParent(temp);
	}

	@Override
	public void setRun() {
		NPCState temp = childState;
		childState = sm.getState(NPCStateEnum.RUNNING);
		childState.setParent(temp);
	}

	@Override
	public void setParent(NPCState p) {
		childState = p;
	}

	@Override
	public void transistionIn() {
		sm.phyState.body.setGravityVal(0);
		sm.phyState.collisionBody.setAwake(true);
		sm.inlevel = false;
		sm.onscreen = false;
		// sm.active = false;
	}

	@Override
	public boolean transistionOut() {
		if (sm.npc.level == GamePlayManager.currentlevel) {
			System.out.println("in same level");
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
