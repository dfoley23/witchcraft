package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class KinematicParticle extends Particle {
	
	public KinematicParticle (Vector3 pos, float gravityval) {
		super(pos);		
		this.externalForce.y = gravityval;
		this.stable = false;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
	}
		
	public void setVel(float x, float y, float z) {
		this.vel = new Vector3(x, y, z);
	}
	
	public void addVel(float x, float y, float z) {
		this.vel.add(x, y, z);
	}
	
	public void addPos(float x, float y) {
		this.pos.add(x, y, 0);
	}
	
	@Override
	public Vector3 accel(Particle p, float t) {
		Vector3 result = new Vector3(0, 0, 0);
		result.add(externalForce);
		return result;
	}

	@Override
	public void integratePos(Vector3 dxdp, float dt) {
		this.pos.add(Util.sclVec(dxdp, dt));
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		this.vel.add(Util.sclVec(dvdp, dt));		
	}
}
