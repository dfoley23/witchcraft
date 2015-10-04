package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Walking extends SharedState {

	public Walking(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("walk", true);
	}
	
	@Override
	public void setWalk() {

	}
	
	@Override
	public void setRun() {
		sm.setState(PlayerStateEnum.RUNNING);
		sm.state.setParent(this.parent);
	}

}
