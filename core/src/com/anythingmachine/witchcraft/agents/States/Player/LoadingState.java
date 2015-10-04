package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class LoadingState extends PlayerState {
	
	public LoadingState(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm,name);
		
	}
	
	public void update(float dt) {
		checkGround();
				
		sm.phyState.correctCBody(0, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		addWindToCape(dt);
	}

	@Override
	protected void setDead() {
	}

	@Override
	public void setState(PlayerStateEnum newstate) {
	}

	@Override
	public void setIdle() {
		
	}
	
	@Override
	public void setAttack() {
		
	}
	
	@Override
	public void setWalk() {
	}

	@Override
	public void switchPower() {
		
	}
	
	@Override
	public void usePower() {
		
	}
	
	@Override
	public void setRun() {
	}
			
	@Override
	public void updatePower(float dt) {
		
	}
	
	@Override
	public void draw(Batch batch) {
		
	}
	@Override
	public void drawCape(Matrix4 cam) {
		
	}
	
	@Override
	public void setInputSpeed() {
		
	}
	
	@Override
	public void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.grounded = false;
		if (sm.hitplatform) {
			if (sm.currentPlatform.isBetween(sm.facingleft, pos.x)) {
				float groundPoint = sm.currentPlatform.getHeight(pos.x);
				if (pos.y < groundPoint) {
					sm.phyState.body.setY(groundPoint);
					sm.state.land();
				}
			}
		}
	}
	
	@Override
	public void transistionIn() {
	}
	
	@Override
	public void land() {
		sm.setState(PlayerStateEnum.IDLE);
		GamePlayManager.initworld = false;
	}


}
