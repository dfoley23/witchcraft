package com.anythingmachine.witchcraft.agents.States.Player;

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
		float windx = GamePlayManager.windx+250;
		
		cape.addWindForce(-windx, -400);

		cape.updatePos(sm.facingleft? sm.neck.getWorldX() -22 : sm.neck.getWorldX() + 22, sm.neck.getWorldY()-8);
		
		cape.flip(sm.facingleft);
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
