package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

public class SharedState extends PlayerState {

	public SharedState(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void drawCape(Matrix4 cam) {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.drawCape(cam);
	}

	@Override
	public void draw(Batch batch) {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.draw(batch);
	}

	@Override
	public void addWindToCape(float dt) {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.addWindToCape(dt);		
	}
	
	@Override
	public void usePower() {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.usePower();
	}
	
	@Override
	public void nextPower() {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
			parent.nextPower();
	}

	@Override
	public void setIdle() {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.setIdle();
		sm.setState(parent.name);
	}
	
	@Override
	public void updatePower(float dt) {
		if ( parent.name == this.name) 
			parent = sm.getState(PlayerStateEnum.IDLE);
		parent.updatePower(dt);
	}
	
	@Override
	public void transistionIn() {
		if ( parent == null || parent.name == this.name) {
			parent = sm.getState(PlayerStateEnum.IDLE);
		}
	}
	

}
