package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.Util.Util;
import com.anythingmachine.aiengine.NPCStateMachine;

public class Running  extends SharedState {

	public Running(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		super.transistionIn();
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);		
		if ( !sm.phyState.body.isStable() )
			sm.phyState.body.setVel(sm.facingleft ? -Util.PLAYERRUNSPEED: Util.PLAYERRUNSPEED, 0, 0);

	}
	
	@Override
	public void setRun() {
	}
		
}
