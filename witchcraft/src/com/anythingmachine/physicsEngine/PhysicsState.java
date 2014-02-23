package com.anythingmachine.physicsEngine;

import java.util.HashMap;

import com.anythingmachine.witchcraft.Util.Util;
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
	
	public void correctCBody(float x, float y, float theta) {
		collisionBody.setTransform(Util.addVecsToVec2(body.getPos(), x, y).scl(Util.PIXEL_TO_BOX), theta);
	}
	
}