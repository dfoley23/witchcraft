package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.badlogic.gdx.math.Matrix4;

public class Invisible extends PlayerState {
	private float timeout = 7f;
	private float time = 0;
	private float lasttime = 0;

	public Invisible(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	public void update(float dt) {
		checkGround();
		
		setInputSpeed();

		switchPower();

		usePower();
		
		updatePower(dt);
		
		sm.phyState.correctCBody(-8, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		Cape cape = GamePlayManager.player.cape;

		cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
	}

	
	@Override
	public void transistionIn() {
		if (!sm.animate.getSkin().equals("invi")) {
			float now = System.currentTimeMillis();
			if (now - lasttime < 150000 && false) {
				sm.setState(PlayerStateEnum.IDLE);
			} else {
				lasttime = now;
				sm.animate.switchSkin("invi");
				sm.animate.bindPose();
			}
		}
	}

	@Override
	public void drawCape(Matrix4 cam) {
		GamePlayManager.player.cape.draw(cam, 0.5f);
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
			sm.setState(PlayerStateEnum.IDLE);
		} else {
			sm.animate.bindPose();
			sm.phyState.stop();
		}
	}

}
