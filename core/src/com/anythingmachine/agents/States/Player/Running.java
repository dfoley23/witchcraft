package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.agents.player.items.Cape;
import com.anythingmachine.aiengine.PlayerStateMachine;

public class Running extends PlayerState {
	
	public Running(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void addWindToCape(float dt) {
		Cape cape = GamePlayManager.player.cape;
		float windx = GamePlayManager.windx+150;
		
		cape.addWindForce(-windx, -400);

		cape.rotate(sm.facingleft ? 16 : -16);

		cape.updatePos(sm.facingleft? sm.neck.getWorldX() -13 : sm.neck.getWorldX() + 13, sm.neck.getWorldY()-9);
		
		cape.flip(sm.facingleft);
	}

	@Override
	public void setInputSpeed() {
		if ( (sm.input.isNowNotThen("Right") && sm.facingleft) || (sm.input.isNowNotThen("Left") && !sm.facingleft) ) {
			sm.state.setWalk();
		} else {
			sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED :Util.PLAYERRUNSPEED);
		}
	}

	@Override
	protected void hitWall(float sign) {
		sm.phyState.body.stopOnX();
		sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
		sm.state.setIdle();
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
	}
	
	@Override
	public void transistionOut() {
		Cape cape = GamePlayManager.player.cape;
		cape.rotate(0);
	}
	
	@Override
	public void setRun() {
	}
	
	@Override
	public void setWalk() {
		Cape cape = GamePlayManager.player.cape;
		cape.rotate(0);
		sm.state.setState(PlayerStateEnum.WALKING);
	}
	
}
