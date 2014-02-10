package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Clean extends NPCState {
	
	public Clean(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("clean", true);
		sm.phyState.stop();
	}
	
	@Override
	public void setIdle() {
		if ( sm.animate.atEnd() ) {
			super.setIdle();
		}
	}

}