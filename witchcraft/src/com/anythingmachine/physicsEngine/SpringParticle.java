package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class SpringParticle extends Particle {
	private ArrayList<Spring> springs;
	
	public SpringParticle (Vector3 pos) {
		super(pos);		
		springs = new ArrayList<Spring>();
		this.stable = false;
	}
	
	@Override
	public void draw() {
		
	}

	@Override
	public void applyImpulse(Vector3 force ) {
		this.externalForce = force;
	}

	public void addSpring(Particle p, float restLength, float k, float damper) {
		springs.add(new Spring(p, restLength, k, damper));
	}
	
	@Override
	public Vector3 accel(Particle p, float t) {
		Vector3 result =  new Vector3(0, 0, 0);
		for( Spring s: springs ) {
			Vector3 force = s.accel(p);
			//System.out.println(force);
			result.x += force.x;
			result.y += force.y;
			result.z += force.z;
		}
		result.x += externalForce.x;
		result.y += externalForce.y;
		result.z += externalForce.z;
		//System.out.println(result);
		return result;
	}
		
	@Override
	public void integratePos(Vector3 dxdp, float dt) {
		this.pos.add(Util.sclVec(dxdp, dt));
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		this.vel.add(Util.sclVec(dvdp, dt));		
		externalForce = new Vector3(0, 0, 0);
	}
}
