package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;

public class InvisiblePower implements Power {
	private float timeout = 7f;
	private float time = 22f;
	
	public InvisiblePower () {
		
	}
	
	@Override
	public void usePower(StateMachine state, float dt) {
		if ( !state.test("invi") && time > timeout*2) {
			state.setTestVal("invi", true);
			state.animate.switchSkin("invi");
			state.animate.bindPose();
			time = 0;
		}
	}

	@Override
	public void updatePower(StateMachine state, float dt) {
		time += dt;
		if( time > timeout ) {
			if ( state.test("invi") ) {
				time = 0;
				state.animate.switchSkin("player");				
				state.animate.bindPose();
				state.animate.setCurrent("walk", true);
				state.setTestVal("invi", false);
				state.setTestVal("usingdupeskin", false);
			}
		}
	}

}
