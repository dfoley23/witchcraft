package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;

public class RK4Integrator {
	private ArrayList<PhysicsComponent> components;
	private float t;
	
	public RK4Integrator() {
		components = new ArrayList<PhysicsComponent>();	    
		t = 0f;
	}

	public void step(float dt) {
		for(PhysicsComponent c: components) {
			for(Particle p: c.getParticles() ) {
				if( !p.isStable() )
					integrate(p, t, dt);
			}
		}
		t += dt;
	}
	    
	public void addComponent( PhysicsComponent c) {
		this.components.add(c);
	}
	
	private void integrate(Particle p, float t, float dt) {
         Derivative a = evaluate(p, t, 0.0f, new Derivative());
         Derivative b = evaluate(p, t, dt*0.5f, a);
         Derivative c = evaluate(p, t, dt*0.5f, b);
         Derivative d = evaluate(p, t, dt, c);

         Vector3 dpdt = new Vector3(0, 0, 0);
         Vector3 dvdt = new Vector3(0, 0, 0);
         dpdt.x = 1.0f/6.0f * (a.dp.x + 2.0f*(b.dp.x + c.dp.x) + d.dp.x);
         dpdt.y = 1.0f/6.0f * (a.dp.y + 2.0f*(b.dp.y + c.dp.y) + d.dp.y);
         dpdt.z = 1.0f/6.0f * (a.dp.z + 2.0f*(b.dp.z + c.dp.z) + d.dp.z);
         
         dvdt.x = 1.0f/6.0f * (a.dv.x + 2.0f*(b.dv.x + c.dv.x) + d.dv.x);
         dvdt.y = 1.0f/6.0f * (a.dv.y + 2.0f*(b.dv.y + c.dv.y) + d.dv.y);
         dvdt.z = 1.0f/6.0f * (a.dv.z + 2.0f*(b.dv.z + c.dv.z) + d.dv.z);

         //System.out.println(dpdt);
         //System.out.println(dvdt);
         p.integratePos( dpdt, dt);
         p.integrateVel( dvdt, dt);
    }
	
	private Derivative evaluate(Particle p, float t, float dt, Derivative d){
		Vector3 pos = Util.addVecs(p.getPos().cpy(), Util.sclVec(d.dp.cpy(), dt));
	    Vector3 vel = Util.addVecs(p.getVel().cpy(), Util.sclVec(d.dv.cpy(), dt));

	    Particle state = new Particle(pos, vel);
	    Derivative output = new Derivative();
	    output.dp = state.vel;//p.getVel().cpy();
	    output.dv = p.accel(state, t+dt);
//	    System.out.println("deriv dp " + output.dp);
//	    System.out.println("dreiv dv " + output.dv);
	    return output;
	}

}
