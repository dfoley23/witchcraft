package com.anythingmachine.physicsEngine;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Particle extends Entity {
	protected Vector3 pos;
	protected Vector3 vel;
	protected boolean stable;
	protected Vector3 externalForce;
	protected ParticleSystem system;
	protected boolean destroyed = false;
	
	public Particle(Vector3 pos) {
		//this.pos = pos;
		this.pos = new Vector3( pos.x, pos.y, pos.z );
		this.vel = new Vector3(0, 0, 0);
		this.stable = true;
		this.externalForce = new Vector3( 0, 0, 0);
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	public Particle(Vector3 pos, Vector3 vel) {
		this.pos = pos.cpy();
		this.vel = vel.cpy();
	}
	
	public void draw(SpriteBatch batch) {
		
	}
		
	public Vector3 getPos() {
		return pos;
	}
	
	public Vector2 getPos2D() {
		return new Vector2(pos.x, pos.y);
	}
	
	public Vector3 getVel() {
		return vel;
	}

	public Vector2 getVel2D() {
		return new Vector2(vel.x, vel.y);
	}

	public void setPos(float x, float y, float z) {
		this.pos.set(x, y, z);
	}

	public void addPos(float x, float y) {
		this.pos.add(x, y, 0);
	}

	public void apply2DImpulse(float x, float y) {
		this.externalForce.set(x, y, 0);	
	}

	public void applyImpulse(Vector3 force) {
		this.externalForce = force;	
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
