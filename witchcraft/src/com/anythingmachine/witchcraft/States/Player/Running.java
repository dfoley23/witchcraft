package com.anythingmachine.witchcraft.States.Player;

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
	
	@Override
	public void setWalk() {
		sm.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(this.parent);
	}
	
}
