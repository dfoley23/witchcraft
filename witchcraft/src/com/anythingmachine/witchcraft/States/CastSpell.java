package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class CastSpell extends State {
	
	public CastSpell(StateMachine sm, StateEnum name) {
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
