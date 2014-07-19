package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;

public class MoveAndStopTimed implements CinematicAction {
	private float dx;
	private float dy;
	private float endDT;
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	
	/**
	 * moves by dx and dy each step
	 * starts at startDT from when the CinematicTrigger began
	 * ends at endDT from when the cinematicTrigger began
	 * @param dx
	 * @param dy
	 * @param startDT
	 * @param endDT
	 */
	public MoveAndStopTimed(Entity e, float dx, float dy, float startDT, float endDT) {
		this.component = e;		
		this.startDT = startDT;
		this.dx = dx;
		this.dy = dy;
		this.endDT = endDT;
	}
	

	public void update(float dt) {
		cineTime += dt;
		if ( cineTime >= startDT && cineTime < endDT ) 
			component.addPos(dx, dy);
	}
	
	public boolean isEnded() {
		return cineTime >= endDT;
	}
	
	public boolean isStarted(float dt) {
		return true;
	}
}
