package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

public class Invisible extends PlayerState {
	private float timeout = 7f;
	private float time = 0;

	public Invisible(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	public void update(float dt) {
		checkGround();

		setInputSpeed();

		switchPower();

		usePower();

		updatePower(dt);

		sm.phyState.correctCBody(0, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		parent.addWindToCape(dt);

	}

	@Override
	public void transistionIn() {
		sm.animate.switchSkin("invi");
		sm.animate.bindPose();
	}

	@Override
	public void draw(Batch batch) {
	}

	@Override
	public void drawCape(Matrix4 cam) {
		GamePlayManager.player.cape.draw(cam, 0.5f);
	}

	@Override
	public void updatePower(float dt) {
		time += dt;
		if (time > timeout) {
			sm.animate.switchSkin("player");
			sm.setState(parent.name);
		}
	}

	@Override
	public void nextPower() {

	}

	@Override
	public void usePower() {

	}

	@Override
	public void setRun() {
		if (parent.name == PlayerStateEnum.RUNNING) {
			parent.setRun();
		} else {
			parent = sm.getState(PlayerStateEnum.RUNNING);
			parent.transistionIn();
		}
	}

	@Override
	public void setWalk() {
		if (parent.name == PlayerStateEnum.WALKING) {
			parent.setWalk();
		} else {
			parent = sm.getState(PlayerStateEnum.WALKING);
			parent.transistionIn();
		}
	}

	@Override
	public void transistionOut() {
		if (time > timeout) {
			time = 0;
			sm.animate.switchSkin("player");
			sm.dupeSkin = "";
		}
	}

	@Override
	protected void setDead() {
		time = 0;
		sm.animate.switchSkin("player");
		sm.dupeSkin = "";
		sm.setState(PlayerStateEnum.DEAD);
	}

	@Override
	public void setIdle() {
		if (time > timeout) {
			time = 0;
			sm.animate.switchSkin("player");
			sm.setState(PlayerStateEnum.IDLE);
		} else {
			sm.animate.bindPose();
			sm.phyState.body.stop();
			parent = this;
		}
	}

}
