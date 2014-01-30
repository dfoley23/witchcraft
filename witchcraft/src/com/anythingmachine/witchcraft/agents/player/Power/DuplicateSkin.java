package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;

public class DuplicateSkin implements Power {

	public DuplicateSkin() {
		
	}
	
	@Override
	public void usePower(StateMachine state, float dt) {
		state.state.setCastSpell();
		state.state.setDupeSkin();
	}

	@Override
	public void updatePower(StateMachine state, float dt) {
	}
}
