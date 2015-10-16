package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Vector3;

public class Falling extends PlayerState {

	public Falling(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void setIdle() {

	}

	@Override
	public void setAttack() {

	}

	@Override
	public void setWalk() {
	}

	@Override
	public void switchPower() {

	}

	@Override
	public void usePower() {

	}

	@Override
	public void setRun() {
	}

	@Override
	public void updatePower(float dt) {

	}

	@Override
	public void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		if (sm.phyState.body.getY() < GamePlayManager.lowestPlatInLevel.getDownPosY()) {
			sm.currentPlatform = GamePlayManager.lowestPlatInLevel;
			sm.phyState.body.setY(sm.currentPlatform.getDownPosY());
			sm.hitplatform = true;
			sm.state.land();
		}else if (sm.hitplatform || sm.hitstairs) {
		    sm.grounded = false;
		    float groundPoint = sm.currentPlatform.getHeight(pos.x);
			sm.phyState.body.setY(groundPoint);
			sm.state.land();
		}
	}

	@Override
	public void transistionIn() {
		super.transistionIn();
		sm.animate.setCurrent("idle", true);
		sm.animate.bindPose();
		if ( this.parent == this ) {
			this.parent = sm.getState(PlayerStateEnum.IDLE);
		}
	}

	@Override
	public void land() {
		sm.setState(parent.name);
		sm.phyState.body.stop();
	}
}
