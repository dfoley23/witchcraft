package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;

public class SharedState extends NPCState {

	public SharedState(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		parent.update(dt);
//		System.out.println(parent);
	}
	
	@Override
	public void takeAction(Action action) {
		parent.takeAction(action);
	}
	
}
