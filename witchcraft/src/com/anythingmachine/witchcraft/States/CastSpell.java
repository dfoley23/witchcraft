package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class CastSpell extends State {
	private float lasttime = -100;
	
	public CastSpell(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	@Override
	public void setCastSpell() {

	}

	@Override
	public void setWalk() {

	}

	@Override
	public void setRun() {

	}

	@Override
	public void setDupeSkin() {
		float now = System.currentTimeMillis();		
		if ( now - lasttime > 150000) {
			lasttime = now;
			sm.setState(StateEnum.DUPESKIN);
			sm.animate.switchSkin(sm.dupeSkin);
			sm.animate.bindPose();
		}
	}

	@Override
	public void setInputSpeed() {
		setIdle();
	}

	@Override
	public void setIdle() {
		if (sm.animate.atEnd()) {
			super.setIdle();
		}
	}

	@Override
	public void setJumping() {

	}
}
