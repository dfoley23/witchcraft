package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Running extends SharedState {

	public Running(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
	}
	
	@Override
	public void setRun() {
	}
	
}
