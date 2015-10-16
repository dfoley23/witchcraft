package com.anythingmachine.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Cinematic extends SharedState {

	public Cinematic(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm,name);
	}
	
	@Override
	public void update(float dt) {
		checkGround();
		
		updatePower(dt);

		sm.phyState.correctCBody(0, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		addWindToCape(dt);
	}
			
	@Override
	public void switchPower() {
	}
				
	@Override
	public void usePower() {
	}
	
	@Override
	public void nextPower() {
	}

	@Override
	public void setIdle() {
	}

	public void transistionIn() {
		parent = sm.getState(PlayerStateEnum.IDLE);
		sm.facingleft = false;
		sm.phyState.body.stop();
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
	}

	public void transistionOut() {
				
	}
	
}
