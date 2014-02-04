package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.math.Matrix4;

public class Invisible extends State {
	private float timeout = 7f;
	private float time = 0;
	private float lasttime = 0;

	public Invisible(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		if (!sm.animate.getSkin().equals("invi")) {
			float now = System.currentTimeMillis();
			if (now - lasttime < 150000) {
				sm.setState(StateEnum.IDLE);
			} else {
				lasttime = now;
				sm.animate.switchSkin("invi");
				sm.animate.bindPose();
			}
		}
	}

	@Override
	public void drawCape(Matrix4 cam) {
		WitchCraft.player.cape.draw(cam, 0.5f);
	}

	@Override
	public void updatePower(float dt) {
		time += dt;
	}

	@Override
	public void nextPower() {

	}

	@Override
	public void usePower() {

	}

	@Override
	public void setIdle() {
		if (time > timeout) {
			time = 0;
			sm.animate.switchSkin("player");
			sm.setState(StateEnum.IDLE);
		} else {
			sm.animate.bindPose();
			sm.phyState.stop();
		}
	}

	@Override
	public void setWalk() {
		sm.setState(StateEnum.WALKING);
		sm.state.setParent(this);
	}

	@Override
	public void setRun() {
		sm.setState(StateEnum.RUNNING);
		sm.state.setParent(this);
	}

}
