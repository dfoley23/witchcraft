package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Particle extends Entity implements PhysicsComponent {
	protected Vector3 pos;
	protected Vector3 vel;
	protected boolean stable;
	protected Vector3 externalForce;
	
	public Particle(Vector3 pos) {
		//this.pos = pos;
		this.pos = new Vector3( pos.x, pos.y, pos.z );
		this.vel = new Vector3(0, 0, 0);
		this.stable = true;
		this.externalForce = new Vector3( 0, 0, 0);

	}
	
	public Particle(Vector3 pos, Vector3 vel) {
		this.pos = pos.cpy();
		this.vel = vel.cpy();
	}
	
	public ArrayList<Particle> getParticles(){
		ArrayList<Particle> list = new ArrayList<Particle>();
		list.add(this);
		return list;
	}

	public void draw() {
		
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
