package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;

public class Mobbing extends NPCState {
	protected NPCState childState;
	
	public Mobbing(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
//	@Override
//	public void update(float dt) {
//		childState.update(dt);
//	}

	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] { ActionEnum.EAT, ActionEnum.SLEEP };
	}

	@Override
	public void handleNPCContact(NonPlayer npc) {
		childState.handleNPCContact(npc);
	}

	@Override
	public void transistionIn() {
		if ( childState == null || childState == this) {
			childState = sm.getState(NPCStateEnum.IDLE);
		}
	}
}
