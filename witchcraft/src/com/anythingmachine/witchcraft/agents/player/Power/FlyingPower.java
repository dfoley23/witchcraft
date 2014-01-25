package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.States.StateEnum;
import com.badlogic.gdx.math.Vector2;

public class FlyingPower implements Power {
	private float hitrooftimeout = 0.25f;
	private float time = 0f;

	@Override
	public void usePower(StateMachine state, float dt) {
		if (!state.test("hitroof") ) {
			state.state.setJumping();
			if ( state.inState(StateEnum.FLYING) ) {
				Vector2 vel = state.phyState.body.getVel2D();
				state.phyState.setVel((state.test("facingleft") ? -1 : 1)*350f, vel.y);
			}
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
		if ( state.inState(StateEnum.FLYING) ) {
			Vector2 vel = state.phyState.body.getVel2D();
			state.phyState.setVel((state.test("facingleft") ? -1 : 1)*350f, vel.y);
		}
	}

}
