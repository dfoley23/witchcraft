package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

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
	
	public void update(float dt) {
		if( cineTime >= startDT ) {
			component.faceLeft(faceleft);
			hasended = true;
		}
	}

	public boolean isStarted(float dt) {
		cineTime += dt;
		return cineTime >= startDT;
	}

	public boolean isEnded() {
		System.out.println("face left "+hasended);
		return hasended;
	}

}
