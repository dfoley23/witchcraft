package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.badlogic.gdx.math.Matrix4;

public class Walking extends State {

	public Walking(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("walk", true);
	}
	
	@Override
	public void setWalk() {

	}
	
	@Override
	public void drawCape(Matrix4 cam) {
		parent.drawCape(cam);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		parent.updatePower(dt);
	}

}
