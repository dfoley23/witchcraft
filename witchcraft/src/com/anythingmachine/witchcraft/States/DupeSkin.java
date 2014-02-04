package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.badlogic.gdx.math.Matrix4;

public class DupeSkin extends State {
	private float timeout = 50;
	private float time = 0;
	
	public DupeSkin (StateMachine sm , StateEnum name) {
		super(sm,  name);
	}
	
	@Override
	public void updatePower(float dt) {
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

	public void setWalk() {
		sm.setState(StateEnum.WALKING);
		sm.state.setParent(this);
		sm.animate.bindPose();
		sm.animate.setCurrent("walk", true);
	}

	public void setRun() {
		sm.setState(StateEnum.RUNNING);
		sm.state.setParent(this);
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
	}

	@Override
	public void setDupeSkin() {
		
	}
		
	@Override
	public void usePower() {
		
	}
			
	@Override 
	public void drawCape(Matrix4 cam) {
		
	}
	
	@Override
	public void nextPower() {
		
	}

	@Override
	public void setAttack() {
		if ( sm.input.is("attack") ) {
			if (sm.animate.isSkin("archer")) {
				sm.setState(StateEnum.ATTACKING);
				sm.state.setParent(this);
				sm.animate.setCurrent("drawbow", true);
				sm.animate.bindPose();
				sm.phyState.stop();
			} else if (!sm.animate.isSkin("player")) {
				sm.setState(StateEnum.ATTACKING);
				sm.state.setParent(this);
				sm.animate.setCurrent("swordattack", true);
				sm.animate.bindPose();
				sm.phyState.stop();
			}
		}
	}

}
