package com.anythingmachine.physicsEngine;

import java.util.HashMap;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class PhysicsState {
	public KinematicParticle body;
	public Body collisionBody;
	public HashMap<String, Fixture> fixtures;
	
	public PhysicsState(KinematicParticle body, Body cbody) {
		this.body = body;
		this.collisionBody = cbody;
		fixtures = new HashMap<String, Fixture>();
	}
	
	public void addFixture(Fixture fix, String name) {
		fixtures.put(name, fix);
	}
	
	public void stopOnX() {
		body.setVel(0, body.getVel().y, 0);
	}
	
	public void stopOnY() {
		body.setVel(body.getVel().x, 0, 0);
	}
	public void stop() {
		body.setVel(0, 0, 0);
	}
	public void setVel(float x, float y) {
		body.setVel(x, y, 0);
	}
	public void correctHeight(float y) {
		body.setPos(body.getX(), y, 0);
	}
	public void setYVel(float yv) {
		body.setVel(body.getVelX(), yv, 0);
	}
	public void setXVel(float xv) {
		body.setVel(xv, body.getVelY(), 0);
	}

	public void correctCBody(float x, float y, float theta) {
		collisionBody.setTransform(Util.addVecsToVec2(body.getPos(), x, y).scl(Util.PIXEL_TO_BOX), theta);
	}
	
	public Vector3 getVel() {
		return body.getVel();
	}
	
	public Vector3 getPos() { 
		return body.getPos();
	}

	public float getX() {
		return body.getX();
	}
	
	public float getY(){
		return body.getY();
	}
	
	public float getVelX(){
		return body.getVelX();
	}
	
	public float getVelY(){
		return body.getVelY();
	}
}