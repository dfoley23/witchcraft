package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class DupeSkin extends State {
	private float timeout = 50;
	private float time = 0;
	
	public DupeSkin (StateMachine sm , StateEnum name) {
		super(sm,  name);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		time += dt;
	}
	
	@Override
	public void setIdle() {
		if ( time > timeout ) {
			time = 0;
			sm.animate.switchSkin("player");
			super.setIdle();
		} else {
			sm.animate.bindPose();
			sm.animate.setCurrent("idle", true);
			sm.phyState.stop();
		}
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public void setDupeSkin() {
		
	}
	
	@Override
	public void setCastSpell() {
		
	}
	@Override 
	public void setJumping() {
		
	}

	@Override
	public void setInvi() {
		
	}
		
	@Override
	public void setAttack() {
		if ( sm.input.is("attack") ) {
			if (sm.animate.isSkin("archer")) {
				sm.setState(StateEnum.ATTACKING);
				sm.state.setParent(StateEnum.DUPESKIN);
				sm.animate.setCurrent("drawbow", true);
				sm.animate.bindPose();
				sm.phyState.stop();
			} else if (!sm.animate.isSkin("player")) {
				sm.setState(StateEnum.ATTACKING);
				sm.state.setParent(StateEnum.DUPESKIN);
				sm.animate.setCurrent("swordattack", true);
				sm.animate.bindPose();
				sm.phyState.stop();
			}
		}
	}

}
