package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Matrix4;

public class DupeSkin extends PlayerState {
	private float timeout = 10;
	private float time = 0;

	public DupeSkin(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void updatePower(float dt) {
		time += dt;
		if (time > timeout) {
			sm.animate.switchSkin("player");
			sm.setState(parent.name);
		}
	}

	public void update(float dt) {
		checkGround();

		setInputSpeed();

		updatePower(dt);

		sm.phyState.correctCBody(-8, 64, 0);

		setAttack();

		sm.animate.setFlipX(sm.facingleft);
	}

	@Override
	public void setIdle() {
		if ( time > timeout ) {
			time = 0;
			sm.animate.switchSkin("player");
			sm.setState(PlayerStateEnum.IDLE);
		} else {
			parent = sm.getState(PlayerStateEnum.IDLE);
			sm.animate.bindPose();
			sm.animate.setCurrent("idle", true);
			sm.phyState.body.stop();
		}
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
	public void transistionOut() {
		if (time > timeout) {
			sm.animate.switchSkin("player");
			time = 0;
		}
	}

	@Override
	public void setRun() {
		sm.setState(PlayerStateEnum.RUNNING);
		sm.state.setParent(sm.getState(PlayerStateEnum.DUPESKIN));
	}

	@Override
	public void setWalk() {
		sm.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(sm.getState(PlayerStateEnum.DUPESKIN));
	}

	@Override
	public void setAttack() {
		if (sm.input.is("attack")) {
			if (sm.animate.isSkin("archer")) {
				sm.setState(PlayerStateEnum.ATTACKING);
				sm.state.setParent(this);
				sm.animate.setCurrent("drawbow", true);
				sm.animate.bindPose();
				sm.phyState.body.stop();
			} else if (!sm.animate.isSkin("player")) {
				sm.setState(PlayerStateEnum.ATTACKING);
				sm.state.setParent(this);
				sm.animate.setCurrent("swordattack", true);
				sm.animate.bindPose();
				sm.phyState.body.stop();
			}
		}
	}

}
