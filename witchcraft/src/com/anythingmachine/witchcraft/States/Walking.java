package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Walking extends SharedState {

	public Walking(StateMachine sm, StateEnum name) {
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

}
