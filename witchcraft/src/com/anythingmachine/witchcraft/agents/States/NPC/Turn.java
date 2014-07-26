package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Turn extends SharedState {

	public Turn(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		super.transistionIn();
		sm.facingleft = !sm.facingleft;
		sm.animate.setFlipX(sm.facingleft);
	}
}
