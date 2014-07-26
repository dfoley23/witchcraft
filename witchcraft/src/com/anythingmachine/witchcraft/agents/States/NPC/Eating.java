package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Eating extends NPCState {

	public Eating(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.body.stop();
	}
	
	@Override
	public void setWalk() {
		
	}
	
	@Override
	public void setRun() {
		
	}
}
