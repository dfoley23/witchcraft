package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.agents.npcs.NonPlayer;
import com.anythingmachine.aiengine.NPCStateMachine;

public class Following extends NPCState {

	public Following(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void handleNPCContact(NonPlayer npc) {
		
	}

}
