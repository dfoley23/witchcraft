package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class CastSpell extends PlayerState {
	
	public CastSpell(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.phyState.stopOnX();
		sm.animate.setCurrent("castspell", true);
	}

	@Override
	public void nextPower() {
		
	}

	@Override
	public void usePower() {
		
	}
		
	@Override
	public void setWalk() {

	}

	@Override
	public void setRun() {

	}

	@Override
	public void setInputSpeed() {
		int axisVal = sm.input.axisRange2();
		if (axisVal > 0) {
			sm.setTestVal("facingleft", false);
		} else if (axisVal < 0) {
			sm.setTestVal("facingleft", true);
		} 	
		setIdle();
	}

	@Override
	public void setIdle() {
		if (sm.animate.atEnd()) {
			super.setIdle();
		}
	}

}
