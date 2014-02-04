package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.badlogic.gdx.math.Matrix4;

public class SharedState extends State {

	public SharedState(StateMachine sm, StateEnum name) {
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
