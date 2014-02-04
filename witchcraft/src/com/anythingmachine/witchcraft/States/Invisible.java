package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.math.Matrix4;

public class Invisible extends State {
	private float timeout = 7f;
	private float time = 22f;
	private float lasttime = -100;

	public Invisible(StateMachine sm, StateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		float now = System.currentTimeMillis();	
		if ( now - lasttime < 150000) {
			super.setIdle();
		} else {
			lasttime = now;
			sm.animate.switchSkin("invi");
			sm.animate.bindPose();
		}
	}
	
	public void drawCape(Matrix4 cam) {
		WitchCraft.player.cape.draw(cam, 0.5f);
	}
	
	@Override
	public void updatePower(float dt) {
		time += dt;
		if ( time > timeout ) {
			sm.animate.switchSkin("player");
			super.setIdle();
			time = 0;
		}
	}
	
	@Override
	public void nextPower() {
		
	}
	
	@Override
	public void usePower() {
		
	}
	
}
