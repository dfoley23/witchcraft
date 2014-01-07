package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.badlogic.gdx.Gdx;

public class InvisiblePower implements Power {
	private float timeout = 7f;
	private float time;
	
	public InvisiblePower () {
		
	}
	
	@Override
	public void usePower(State state, AnimationManager animate, 
			KinematicParticle Body) {
		animate.switchSkin("invi");
		animate.bindPose();
		time = Gdx.graphics.getDeltaTime();
	}

	@Override
	public void updatePower(State state, AnimationManager animate, float dt) {
		time += dt;
		if( time > timeout ) {
			animate.switchSkin("player");
			animate.bindPose();
		}
	}

}
