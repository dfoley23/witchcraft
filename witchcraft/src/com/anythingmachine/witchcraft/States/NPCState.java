package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.NPCStateMachine;

public class NPCState {
	protected NPCStateMachine sm;
	public NPCStateEnum name;	public PlayerState parent;
	
	public NPCState(NPCStateMachine sm, NPCStateEnum name) {
		this.sm = sm;
		this.name = name;
	}
	
	public NPCStateEnum[] getPossibleActions() {
		return name.getFollowUpStates();
	}
	
	public void transistionIn() {
		
	}
	
	public void transistionOut() {
		
	}
	
	public void immediateTransOut() {
		
	}

}
