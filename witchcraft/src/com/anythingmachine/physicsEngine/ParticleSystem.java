package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

public class ParticleSystem implements PhysicsComponent {
	ArrayList<Particle> particles;
	
	public ParticleSystem(RK4Integrator rk4) {
		rk4.addComponent(this);
		particles = new ArrayList<Particle>();
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	@Override
	public ArrayList<Particle> getParticles() {
		return particles;
	}

}
