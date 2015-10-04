package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.particleEngine.particles.Particle;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;

public class RK4Integrator {
	private ArrayList<PhysicsComponent> components;
	private float t;
	private float dt;
	
	public RK4Integrator(float dt) {
		this.dt = dt;
		components = new ArrayList<PhysicsComponent>();
		t = 0f;
	}

	public void step() {
		for (PhysicsComponent c : components) {
			for (Particle p : c.getParticles()) {
				if (!p.isStable())
					integrate(p, t, dt);
			}
		}
		t += dt;
	}

	public void addComponent(PhysicsComponent c) {
		this.components.add(c);
	}

	private void integrate(Particle p, float t, float dt) {
		if (!p.isEuler()) {
			Vector3 dpdt = new Vector3(0, 0, 0);
			Vector3 dvdt = new Vector3(0, 0, 0);
			Derivative a = evaluate(p, t, dt, new Derivative());
			Derivative b = evaluate(p, t, dt * 0.5f, a);
			Derivative c = evaluate(p, t, dt * 0.5f, b);
			Derivative d = evaluate(p, t, dt, c);

			dpdt.x = 1.0f / 6.0f * (a.dp.x + 2.0f * (b.dp.x + c.dp.x) + d.dp.x);
			dpdt.y = 1.0f / 6.0f * (a.dp.y + 2.0f * (b.dp.y + c.dp.y) + d.dp.y);
			dpdt.z = 1.0f / 6.0f * (a.dp.z + 2.0f * (b.dp.z + c.dp.z) + d.dp.z);

			dvdt.x = 1.0f / 6.0f * (a.dv.x + 2.0f * (b.dv.x + c.dv.x) + d.dv.x);
			dvdt.y = 1.0f / 6.0f * (a.dv.y + 2.0f * (b.dv.y + c.dv.y) + d.dv.y);
			dvdt.z = 1.0f / 6.0f * (a.dv.z + 2.0f * (b.dv.z + c.dv.z) + d.dv.z);
			p.integratePos(dpdt, dt);
			p.integrateVel(dvdt, dt);
		} else {
//			Derivative a = evaluateEuler(p, t, dt);
//			p.integratePos(a.dp, dt);
//			p.integrateVel(a.dv, dt);
			Vector3 vel = p.getVel();
			p.addPos(vel.x*dt,vel.y*dt);
			Vector3 newvel = p.accel(p.getPos(), vel, t + dt);
			p.addVel(newvel.x*dt, newvel.y*dt, newvel.z*dt);
		}
	}

	private Derivative evaluate(Particle p, float t, float dt, Derivative d) {
		Vector3 pos = Util.addVecs(p.getPos(),
				Util.sclVec(d.dp, dt));
		Vector3 vel = Util.addVecs(p.getVel(),
				Util.sclVec(d.dv, dt));

		Derivative output = new Derivative();
		output.dp = vel;
		output.dv = p.accel(pos, vel, t + dt);
		return output;
	}
	
	private Derivative evaluateEuler(Particle p, float t, float dt) {
		Vector3 vel = p.getVel();

		Derivative output = new Derivative();
		output.dp = vel;
		output.dv = p.accel(p.getPos(), vel, t + dt);
		return output;
	}

}
