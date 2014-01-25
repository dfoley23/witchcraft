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
	public void usePower(StateMachine state, float dt) {
		if (state.state.canCastSpell() && 
				 time > timeout*2)  {
			state.phyState.stopOnX();
			state.animate.bindPose();
			state.setTestVal("dupeskin", true);
			state.animate.setCurrent("castspell", true);
			time =0;
		} 
		
	}

	@Override
	public void updatePower(StateMachine state, float dt) {
		if ( state.test("dupeskin") && state.animate.atEnd() ) {
			state.setTestVal("dupeskin", false);
		}
		time += dt;
		if ( state.test("usingdupeskin") ){
			if( time > timeout ) {
				time = 0;
				state.animate.switchSkin("player");
				state.animate.bindPose();
				state.animate.setCurrent("walk", true);
				state.setTestVal("usingdupeskin", false);
			}
		}
	}
}
