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
		float windx = GamePlayManager.windx+150;
		
		cape.addWindForce(-windx, -400);

		cape.rotate(sm.facingleft ? 16 : -16);

		cape.updatePos(sm.facingleft? sm.neck.getWorldX() -13 : sm.neck.getWorldX() + 13, sm.neck.getWorldY()-9);
		
		cape.flip(sm.facingleft);
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
		sm.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(this.parent);
	}
	
}
