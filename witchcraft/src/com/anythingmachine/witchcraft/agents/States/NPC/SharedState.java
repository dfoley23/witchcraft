package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;

public class SharedState extends NPCState {

	public SharedState(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		parent = sm.getState(NPCStateEnum.IDLE);
	}
	
	@Override
	public void update(float dt) {
		if ( parent.name != this.name ) 
			parent.update(dt);
	}
	
	@Override
	public void takeAction(Action action) {
		if ( parent.name != this.name ) 
			parent.takeAction(action);
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		if ( parent.name != this.name ) 
			return parent.getPossibleActions();
		return new ActionEnum[] { };
	}
	
	@Override
	public void setAttack() {
		if ( parent.name != this.name ) 
			parent.setAttack();
	}

	@Override
	public void takeAction(float dt) {
		if ( parent.name != this.name ) 
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
