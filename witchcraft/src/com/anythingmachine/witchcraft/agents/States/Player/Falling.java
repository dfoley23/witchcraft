package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Vector3;

public class Falling extends SharedState {

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
		sm.grounded = false;
		if (sm.hitplatform || sm.hitstairs) {
			float groundPoint = sm.elevatedSegment.getHeight();
			sm.phyState.body.setY(groundPoint);
			sm.grounded = true;
			sm.state.land();
			transistionToParent();
		}
	}

	@Override
	public void transistionIn() {
		super.transistionIn();
		sm.animate.setCurrent("idle", true);
		sm.animate.bindPose();
	}

	@Override
	public void land() {
		sm.setState(parent.name);
	}
}
