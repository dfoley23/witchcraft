package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Matrix4;

public class SharedState extends PlayerState {

	public SharedState(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void drawCape(Matrix4 cam) {
		parent.drawCape(cam);
	}
		
	@Override
	public void usePower() {
		parent.usePower();
	}
	
	@Override
	public void nextPower() {
		parent.nextPower();
	}

	@Override
	public void setIdle() {
		parent.setIdle();
	}
	
	@Override
	public void updatePower(float dt) {
		parent.updatePower(dt);
	}
	

}
