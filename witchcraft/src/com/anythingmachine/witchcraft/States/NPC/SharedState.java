package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class SharedState extends NPCState {

	public SharedState(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		parent.update(dt);
	}
	
	@Override
	public void takeAction(Action action) {
		parent.takeAction(action);
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return parent.getPossibleActions();
	}
	
	@Override
	public void setAttack() {
		parent.setAttack();
	}

	@Override
	public void takeAction(float dt) {
		parent.takeAction(dt);
	}

	@Override
	public void transistionIn() {
		if ( parent == null ) {
			parent = sm.getState(NPCStateEnum.IDLE);
		}
	}
	
	@Override
	public void setIdle() {
		parent.setIdle();
	}
	
}
