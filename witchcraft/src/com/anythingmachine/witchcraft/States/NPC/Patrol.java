package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class Patrol extends Working {

	public Patrol(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] {ActionEnum.PATROL, ActionEnum.TURN, ActionEnum.WALK};
	}

}
