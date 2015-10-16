package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.aiengine.PlayerStateMachine;
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
	public void setInputSpeed() {
		boolean inputRight = sm.input.isNowNotThen("Right");
		boolean inputLeft = sm.input.isNowNotThen("Left");
		if (this.parent.name != PlayerStateEnum.WALKING && this.parent.name != PlayerStateEnum.RUNNING) {
			if (inputRight) {
				sm.facingleft = false;
				if (!sm.hitrightwall) {
					sm.hitleftwall = false;
					setWalk();
					sm.phyState.body.setXVel(Util.PLAYERWALKSPEED);
				}
			} else if (inputLeft) {
				sm.facingleft = true;
				if (!sm.hitleftwall) {
					sm.hitrightwall = false;
					setWalk();
					sm.phyState.body.setXVel(-Util.PLAYERWALKSPEED);
				}
			}
		} else {
			if ((inputRight && sm.facingleft) || (inputLeft && !sm.facingleft)) {
				if (this.parent.name == PlayerStateEnum.RUNNING) {
					setWalk();
				} else {
					sm.state.setIdle();
				}
			} else if (inputRight && !sm.facingleft) {
				if (this.parent.name == PlayerStateEnum.RUNNING) {
					setRun();
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED);
				} else {
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERWALKSPEED : Util.PLAYERWALKSPEED);
				}
			} else if ((inputLeft && sm.facingleft)) {
				if (this.parent.name == PlayerStateEnum.RUNNING) {
					setRun();
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED);
				} else {
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERWALKSPEED : Util.PLAYERWALKSPEED);
				}
			} else {
				if (this.parent.name == PlayerStateEnum.RUNNING) {
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED);
				} else {
					sm.phyState.body.setXVel(sm.facingleft ? -Util.PLAYERWALKSPEED : Util.PLAYERWALKSPEED);
				}
			}
		}
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

	@Override
	public void setState(PlayerStateEnum newstate) {
		parent = sm.getState(newstate);
	}

}
