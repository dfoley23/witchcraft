package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;

public class StartAction implements CinematicAction {
	private float endDT;
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	
	
	public StartAction(float startDT, float endDT, Entity e) {
		this.component = e;		
		this.startDT = startDT;
		this.endDT = endDT;
	}
	
	public void update(float dt) {
		if( cineTime >= startDT ) 
			component.start();
	}
	
	public boolean isStarted(float dt) {
		cineTime += dt;
		return cineTime >= startDT;
	}
	
	public boolean isEnded() {
		return cineTime >= endDT;
	}
}
