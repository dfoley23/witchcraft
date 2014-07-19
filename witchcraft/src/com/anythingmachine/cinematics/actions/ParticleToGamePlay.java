package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ParticleToGamePlay implements CinematicAction {
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	private boolean hasStarted = false;
	
	
	public ParticleToGamePlay(float startDT, Entity e) {
		this.component = e;		
		this.startDT = startDT;
	}
	
	public void update(float dt) {
	}
		
	public boolean isStarted(float dt) {
		cineTime += dt;
		if( cineTime >= startDT && !hasStarted) {
			GamePlayManager.addEntity(component);
			hasStarted = true;
			
		}
		return cineTime >= startDT;
	}
	
	public boolean isEnded() {
		return hasStarted;
	}
}
