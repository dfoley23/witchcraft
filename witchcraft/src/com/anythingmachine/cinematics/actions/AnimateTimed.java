package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;

public class AnimateTimed implements CinematicAction {
	private float endDT;
	private String state;
	private boolean hasstarted = false;
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	protected boolean loop;

	public AnimateTimed(String state, Entity e, float startDT,float endDT, boolean loop ) {
		this.component = e;		
		this.startDT = startDT;
		this.endDT = endDT;
		this.state = state;
		this.loop = loop;
		hasstarted = startDT == 0.0f;
	}

	public boolean isStarted(float dt) {
		cineTime += dt;
		if( !hasstarted && cineTime >= startDT ) {
			if ( component.isAnimationEnded(dt) ) {
				component.setAnimation(state, loop);
				component.setStateByValue(state);
				component.setParentByValue("CINEMATIC");
				hasstarted = true;
			}
		}
		return hasstarted;
	}

	public void update(float dt) {
		if ( cineTime >= endDT ) {
			component.stop();
		}
	}

	public boolean isEnded() {
		return cineTime >= endDT;
	}
}
