package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;

public class DuplicateSkin implements Power {
	private float timeout = 100f;
	private float time = 201f;

	public DuplicateSkin() {
		
	}
	
	@Override
	public void usePower(StateMachine state, AnimationManager animate, 
			KinematicParticle body, float dt) {
		if (state.state.canCastSpell(state) && 
				 time > timeout*2)  {
			body.setVel(0, body.getVel().y, 0);
			animate.bindPose();
			state.setTestVal("dupeskin", true);
			animate.setCurrent("castspell", true);
			time =0;
		} 
		
	}

	@Override
	public void updatePower(StateMachine state, AnimationManager animate, float dt) {
		if ( state.test("dupeskin") && animate.atEnd() ) {
			state.setTestVal("dupeskin", false);
		}
		time += dt;
		if ( state.test("usingdupeskin") ){
			if( time > timeout ) {
				time = 0;
				animate.switchSkin("player");
				animate.bindPose();
				animate.setCurrent("walk", true);
				state.setTestVal("usingdupeskin", false);
			}
		}
	}
}
