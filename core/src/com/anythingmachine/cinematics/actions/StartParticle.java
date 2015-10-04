package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;

public class StartParticle implements CinematicAction {
	private Entity component;
	private float startDT;
	private float cineTime = 0f;
	private boolean hasStarted = false;
	
	public StartParticle(float startDT, Entity e) {
		this.component = e;		
		this.startDT = startDT;
	}

	public void update(float dt) {
	}
			
	public boolean isStarted(float dt) {
		cineTime += dt;
		if( cineTime >= startDT) {
			hasStarted = true;
			component.setStable(false);
		}
		return cineTime >= startDT;
	}
	
	public boolean isEnded() {
		return hasStarted;
	}
}
