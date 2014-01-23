package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class FlyingPower implements Power {
	private KinematicParticle body;
	private float hitrooftimeout = 0.25f;
	private float time = 0f;

	@Override
	public void usePower(StateMachine state, AnimationManager animate,
			KinematicParticle body, float dt) {
		this.body = body;
		if (!state.test("hitroof") ) {
			body.setVel(body.getVel().x, 150f, 0f);
			if (state.state.canFly(state)) {
				animate.setCurrent("jump", true);
				animate.bindPose();
				state.setState(State.JUMPING);
			} else if ( state.inState(State.FLYING) ) {
				Vector2 vel = body.getVel2D();
				body.setVel((state.test("facingleft") ? -1 : 1)*350f, vel.y, 0f);
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
	public void updatePower(StateMachine state, AnimationManager animate,
			float dt) {
		if ( state.inState(State.FLYING) ) {
			Vector2 vel = body.getVel2D();
			body.setVel((state.test("facingleft") ? -1 : 1)*350f, vel.y, 0f);
		}
	}

}
