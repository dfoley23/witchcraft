package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;

public class FaceLeft implements CinematicAction {
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	private boolean hasended = false;
	private boolean faceleft;
	
	public FaceLeft(Entity e, float startDT, boolean val) {
		this.startDT = startDT;
		this.component = e;
		this.faceleft = val;
	}
	
	@Override
	public void update(float dt) {
		if( cineTime >= startDT ) {
			component.faceLeft(faceleft);
			hasended = true;
		}
	}

	@Override
	public boolean isStarted(float dt) {
		cineTime += dt;
		return cineTime >= startDT;
	}

	@Override
	public boolean isEnded() {
		System.out.println("face left "+hasended);
		return hasended;
	}

}
