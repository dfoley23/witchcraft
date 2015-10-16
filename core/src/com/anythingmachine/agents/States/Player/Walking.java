package com.anythingmachine.agents.States.Player;

import com.anythingmachine.Util.Util;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.Gdx;

public class Walking extends PlayerState {

	public Walking(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("walk", true);
	}

	@Override
	public void setInputSpeed() {
		boolean inputRight = sm.input.isNowNotThen("Right");
		boolean inputLeft = sm.input.isNowNotThen("Left");
		if ( ( inputRight && sm.facingleft) || (inputLeft && !sm.facingleft) ) {
			sm.state.setIdle();
		} else if ( inputRight && !sm.facingleft ) {
			setRun();
			sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED);
		} else if ( (inputLeft && sm.facingleft) ) {
			setRun();
			sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED);
		} else {
			sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERWALKSPEED : Util.PLAYERWALKSPEED);
		}
	}
	
	@Override
	protected void hitWall(float sign) {
		sm.phyState.body.stopOnX();
		sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
		sm.state.setIdle();
	}

	@Override
	public void setWalk() {

	}

	@Override
	public void setRun() {
		sm.state.setState(PlayerStateEnum.RUNNING);
	}

}
