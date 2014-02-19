package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Idle extends NPCState {

	public Idle(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.body.stop();
	}
}
