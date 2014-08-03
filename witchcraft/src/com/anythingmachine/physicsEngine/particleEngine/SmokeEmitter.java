package com.anythingmachine.physicsEngine.particleEngine;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.particleEngine.particles.FireParticle;
import com.anythingmachine.physicsEngine.particleEngine.particles.SmokeParticle;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class SmokeEmitter extends Entity {
	private ArrayList<SmokeParticle> fires;
	private Vector3 initPos;
	private Random rand;
	private float lifetime;
	private float life;
	private int limit;
	private float beginScale;
	private float endScale;
	private float particleLifetime;
	private boolean addParticle = true;

	public SmokeEmitter(Vector3 pos, int limit, float particleLifetime,
			float lifetime, float startScale, float endScale) {
		this.particleLifetime = particleLifetime;
		this.beginScale = startScale;
		this.endScale = endScale;
		this.limit = limit;
		this.initPos = pos;
		this.lifetime = lifetime;
		fires = new ArrayList<SmokeParticle>();
		rand = new Random();
		int xvel = rand.nextInt(20) + 1;
		if (rand.nextBoolean()) {
			xvel *= -1;
		}
		fires.add(new SmokeParticle(initPos, (rand.nextFloat()+0.5f)*particleLifetime, startScale,
					endScale, xvel, Util.FIRESPEED*0.5f));
	}

	@Override
	public void update(float dt) {
		if ( addParticle && fires.size() < limit ) {
			int xvel = rand.nextInt(20) + 1;
			if (rand.nextBoolean()) {
				xvel *= -1;
			}
			fires.add(new SmokeParticle(initPos, (rand.nextFloat()+0.5f)*particleLifetime, beginScale,
					endScale, xvel, Util.FIRESPEED*0.5f));
			addParticle = false;
		} else {
			addParticle = true;
		}
		for (SmokeParticle f : fires) {
			if (f.isDead()) {
				if (life < lifetime) {
					int xvel = rand.nextInt(20) + 1;
					if (rand.nextBoolean()) {
						xvel *= -1;
					}
					f.reset(initPos, xvel, Util.FIRESPEED);
				}
			}
			if ( this.life / this.lifetime > 0.95f )
				f.setFade((1-this.life/this.lifetime));
			f.update(dt);
		}
		life += dt;
	}

	@Override
	public void draw(Batch batch) {
		for (SmokeParticle f : fires) {
			f.draw(batch);
		}
	}
	
	@Override
	public boolean isEnded() {
		return life >= lifetime;
	}

}
