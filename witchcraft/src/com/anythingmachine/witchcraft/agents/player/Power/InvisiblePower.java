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
	public void usePower(StateMachine state, AnimationManager animate, 
			KinematicParticle Body, float dt) {
		if ( !state.test("invi") && time > timeout*2) {
			state.setTestVal("invi", true);
			animate.switchSkin("invi");
			animate.bindPose();
			time = 0;
		}
	}

	@Override
	public void updatePower(StateMachine state, AnimationManager animate, float dt) {
		time += dt;
		if( time > timeout ) {
			if ( state.test("invi") ) {
				time = 0;
				animate.switchSkin("player");				
				animate.bindPose();
				animate.setCurrent("walk", true);
				state.setTestVal("invi", false);
				state.setTestVal("usingdupeskin", false);
			}
		}
	}

}
