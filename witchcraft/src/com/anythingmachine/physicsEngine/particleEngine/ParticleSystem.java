package com.anythingmachine.physicsEngine.particleEngine;

import java.util.ArrayList;
import java.util.Iterator;

import com.anythingmachine.physicsEngine.Particle;
import com.anythingmachine.physicsEngine.PhysicsComponent;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ParticleSystem implements PhysicsComponent {
	ArrayList<Particle> particles;
	
	public ParticleSystem(RK4Integrator rk4) {
		rk4.addComponent(this);
		particles = new ArrayList<Particle>();
	}
	
	public void draw(Batch batch) {
		Iterator<Particle> it = particles.iterator();
		while( it.hasNext() ) {
			Particle p = it.next();
			if ( !p.isStable() )
				p.draw(batch);
			if ( p.isDestroyed() ) {
				it.remove();
			}
		}
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public void destroyParticle(Particle p) {
		particles.remove(p);
	}
	
	@Override
	public ArrayList<Particle> getParticles() {
		return particles;
	}

}
