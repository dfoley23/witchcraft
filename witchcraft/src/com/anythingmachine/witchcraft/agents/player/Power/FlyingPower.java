package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;

public class FlyingPower implements Power {
	private float hitrooftimeout = 0.5f;
	private float time = 0f;

	@Override
	public void usePower(StateMachine state, float dt) {
		if (!state.test("hitroof")) {
			state.state.setJumping();
		} else {
			time += dt;
			if ( time > hitrooftimeout ) {
				state.setTestVal("hitroof", false);
				time = 0;
			}
		}
	}

	@Override
	public void updatePower(StateMachine state, float dt) {
		time += dt;
		if ( time > hitrooftimeout ) {
			state.setTestVal("hitroof", false);
			time = 0;
		}
	}

}
