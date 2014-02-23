package com.anythingmachine.witchcraft.States.Player;

import java.util.Random;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.agents.player.items.Cape;

public class Running extends SharedState {
	
	public Running(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void addWindToCape(float dt) {
		Cape cape = GamePlayManager.player.cape;

		int windx = 0;		
		if ( sm.windtimeout > 0 ) {
			windx = sm.rand.nextInt(1500);
			if ( windx > 600 ) 
				windx = 0;
		} else if ( sm.windtimeout < -1 ) {
			sm.windtimeout = 1.5f;
		}
		windx+=250;
		sm.windtimeout-=dt;
		
		cape.addWindForce(sm.facingleft ? windx : -windx, -400);

		cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
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
