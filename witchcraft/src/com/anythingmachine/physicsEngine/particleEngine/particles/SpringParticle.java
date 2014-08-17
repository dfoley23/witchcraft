package com.anythingmachine.physicsEngine.particleEngine.particles;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.Rope;
import com.anythingmachine.physicsEngine.Spring;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;

public class SpringParticle extends Particle {
	protected ArrayList<Spring> springs;
	
	public SpringParticle (Vector3 pos) {
		super(pos);		
		springs = new ArrayList<Spring>();
		this.stable = false;
		this.externalForce = new Vector3(0,Util.GRAVITY,0);
	}
	
	@Override
	public void applyImpulse(Vector3 force ) {
		this.externalForce = force;
	}

	public SpringParticle addSpring(Particle p, float restLength, float k, float damper) {
		springs.add(new Spring(p, restLength, k, damper));
		return this;
	}

	public SpringParticle addRope(Particle p, float restLength, float k, float damper) {
		springs.add(new Rope(p, restLength, k, damper));
		return this;
	}

	@Override
	public Vector3 accel(Vector3 pos, Vector3 vel, float t) {
		Vector3 result =  new Vector3(0, 0, 0);
		for( Spring s: springs ) {
			Vector3 force = s.accel(pos, vel);
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
		Vector3 ds = Util.sclVec(dxdp, dt);
		this.pos.x += ds.x;
		this.pos.y += ds.y;
		this.pos.z += ds.z;
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		Vector3 ds = Util.sclVec(dvdp, dt);
		this.vel.x += ds.x;
		this.vel.y += ds.y;
		this.vel.z += ds.z;
//		externalForce.x = 0;
//		externalForce.y = 0;
//		externalForce.z = 0;
	}
}
