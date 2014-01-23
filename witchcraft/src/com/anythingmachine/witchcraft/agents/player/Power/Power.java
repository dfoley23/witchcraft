package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;

public interface Power {
	public void usePower(StateMachine state, AnimationManager animate, 
			KinematicParticle Body, float dt);
	public void updatePower(StateMachine state, AnimationManager animate, float dt);
}
