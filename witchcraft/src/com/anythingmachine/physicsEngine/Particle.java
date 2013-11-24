package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Particle {
	protected Vector3 pos;
	protected Vector3 vel;
	protected boolean stable;
	
	public Particle(Vector3 pos) {
		//this.pos = pos;
		this.pos = new Vector3( pos.x, pos.y, pos.z );
		this.vel = new Vector3(0, 0, 0);
		this.stable = true;
	}
	
	public Particle(Vector3 pos, Vector3 vel) {
		this.pos = pos.cpy();
		this.vel = vel.cpy();
	}
	
	public void draw() {
		
	}
	
	public void draw(ShapeRenderer batch) {
		batch.setColor(Color.RED);
		batch.begin(ShapeType.Point);
		batch.point(pos.x, pos.y, pos.z);
		batch.end();
	}

	public Vector3 getPos() {
		return pos;
	}
	
	public Vector3 getVel() {
		return vel;
	}
	
	public void addForce(Vector3 force) {
		
	}
	public Vector3 accel(Particle p, float t) {
		return new Vector3(0, 0, 0);
	}
	
	public void integratePos(Vector3 dxdp, float dt) {
		
	}

	public void integrateVel(Vector3 dvdp, float dt) {

	}

	public boolean isStable() {
		return stable;
	}
	
}
