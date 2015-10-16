package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Cleaning extends SharedState {
	
	public Cleaning(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);		
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("clean", true);
		sm.phyState.body.stop();
	}
	
	@Override
	public void setIdle() {
		if ( sm.animate.atEnd() ) {
			super.setIdle();
		}
	}

}
