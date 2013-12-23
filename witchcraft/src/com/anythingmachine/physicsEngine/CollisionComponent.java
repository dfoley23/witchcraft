package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.math.Vector3;


public class CollisionComponent {
	protected boolean active;
	protected Particle particle;
	protected Vector3 pos;
	
	public CollisionComponent() {
		
	}

	public void update() {
		pos = particle.pos;
	}
	
	public void setActive(boolean val) {
		active = val;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Vector3 getPos() {
		return pos;
	}

	public boolean isColliding(RectCollisionComponent c) {
		return false;
	}

}
