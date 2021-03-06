package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Patrolling extends Working {

	public Patrolling(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.body.stop();
	}

}
