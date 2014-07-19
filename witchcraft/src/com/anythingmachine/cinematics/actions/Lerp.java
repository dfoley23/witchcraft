package com.anythingmachine.cinematics.actions;

import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.math.Vector3;

public class Lerp implements CinematicAction {
	private Vector3 target;
	private float da;
	private float alpha;
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;

	/**
	 * 
	 * @param e
	 * @param startDT when to start the animation
	 * @param da
	 *            an amount from 0-1 indicating the percentage of the distance
	 *            between the position and the target to move
	 * @param target
	 */
	public Lerp(Entity e, float startDT, float da, Vector3 target) {
		this.component = e;		
		this.startDT = startDT;
		this.target = target;
		this.da = da;
		alpha = 0.0f;
	}

	public void update(float dt) {
		alpha += da;
		component.getPos().lerp(target, da);
	}

	public boolean isStarted(float dt) {
		cineTime += dt;
		return cineTime >= startDT;
	}
	
	public boolean isEnded() {
		System.out.println("apha: "+alpha);
		System.out.println("cineTime: "+cineTime);
		return alpha >= 1;
	}
}
