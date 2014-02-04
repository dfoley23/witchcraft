package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.badlogic.gdx.math.Matrix4;

public class Running extends State{

	public Running(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
	}
	
	@Override
	public void drawCape(Matrix4 cam) {
		parent.drawCape(cam);
	}

	
	@Override
	public void setRun() {
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		parent.updatePower(dt);
	}

}
