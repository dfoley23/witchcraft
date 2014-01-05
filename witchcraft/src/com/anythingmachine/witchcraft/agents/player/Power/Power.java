package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;

public interface Power {
	public void usePower(State state);
	public void updatePower(State state, float dt);
}
