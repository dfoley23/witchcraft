package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Inactive extends NPCState {
	public NPCState childState;
	
	public Inactive(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	public void update(float dt) {
		aiChoiceTime += dt;

		checkInLevel();
		
		checkInBounds();

		checkAttack();
		
		checkGround();

		takeAction(dt);
		
		childState.checkTarget();

		fixCBody();
		
	}

	@Override
	public void setChildState(NPCStateEnum state) {
		childState = sm.getState(state);
	}

	public void draw(Batch batch) {

	}
	
	@Override
	public void checkAttack() {
		if (WitchCraft.cam.inBigBounds(sm.phyState.body.getPos())) {
			sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState.body
					.getX();
			if (sm.canseeplayer) {
				if (GamePlayManager.player.inHighAlert()) {
					childState = sm.getState(NPCStateEnum.ATTACKING);
				} else if (GamePlayManager.player.inAlert()) {
					sm.state.setAlert();
				}
			}
		}
	}

	public void checkInLevel() {
		sm.onscreen = true;
		super.checkInLevel();
		sm.onscreen = false;
	}
	
	public void setAttack() {

	}

	public void setAlert() {

	}
	
	
	public void checkInBounds() {
		if( WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			sm.onscreen = true;
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
		sm.onscreen = false;
	}
	
	public boolean transistionOut() {
		return sm.onscreen;
	}

	public void immediateTransOut() {
		
	}

}
