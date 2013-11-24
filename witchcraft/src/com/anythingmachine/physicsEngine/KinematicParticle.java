package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class KinematicParticle extends Particle {
	
	public KinematicParticle (Vector3 pos) {
		super(pos);		
	}
	
	@Override
	public void draw() {
		
	}
	
	@Override
	public void draw(ShapeRenderer batch) {
	}	
	
	public void setPos(Vector3 pos) {
		this.pos.set(pos);
	}

	public void setPos(float x, float y, float z) {
		this.pos.set(x, y, z);
	}

	public void addPos(float x, float y) {
		this.pos.add(x, y, 0);
	}
	
	@Override
	public Vector3 accel(Particle p, float t) {
		return Vector3.Zero;
	}
		
	@Override
	public void integratePos(Vector3 dxdp, float dt) {
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
	}
}
