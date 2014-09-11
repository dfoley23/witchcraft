package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Cinematic extends SharedState {

	public Cinematic(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm,name);
	}
	
	@Override
	public void update(float dt) {
		checkGround();
		
		updatePower(dt);

		sm.phyState.correctCBody(-8, 64, 0);

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
		if ( parent.name != this.name )
			this.parent = sm.state;
		else
			parent = sm.getState(PlayerStateEnum.IDLE);
		sm.facingleft = false;
		sm.phyState.body.stop();
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
	}

	public void transistionOut() {
				
	}
	
}
