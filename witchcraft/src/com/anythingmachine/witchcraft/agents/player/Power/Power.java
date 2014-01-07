package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;

public interface Power {
	public void usePower(State state, AnimationManager animate, 
			KinematicParticle Body);
	public void updatePower(State state, AnimationManager animate, float dt);
}
