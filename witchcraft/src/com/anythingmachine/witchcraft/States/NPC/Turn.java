package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Turn extends NPCState {

	public Turn(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.facingleft = !sm.facingleft;
		sm.animate.setFlipX(sm.facingleft);
	}
}
