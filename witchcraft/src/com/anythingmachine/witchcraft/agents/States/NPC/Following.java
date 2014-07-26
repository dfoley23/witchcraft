package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;

public class Following extends NPCState {

	public Following(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void handleNPCContact(NonPlayer npc) {
		
	}

}
