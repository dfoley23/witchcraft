package com.anythingmachine.physicsEngine.particleEngine;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.particleEngine.particles.FireParticle;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class FireEmitter extends Entity {
	private ArrayList<FireParticle> fires;
	private Vector3 initPos;
	private Random rand;
	private float lifetime;
	private float life;
	private int limit;
	private float beginScale;
	private float endScale;
	private float particleLifetime;
	private boolean addParticle = true;
	
	public FireEmitter(Vector3 pos, int limit, float particleLifetime,
			float lifetime, float startScale, float endScale) {
		this.particleLifetime = particleLifetime;
		this.beginScale = startScale;
		this.endScale = endScale;
		this.limit = limit;
		this.initPos = pos;
		this.lifetime = lifetime;
		fires = new ArrayList<FireParticle>();
		rand = new Random();
		int xvel = rand.nextInt(10) + 1;
		if (rand.nextBoolean()) {
			xvel *= -1;
		}
		fires.add(new FireParticle(initPos, (Math.max(0.56f, rand.nextFloat()))*particleLifetime, startScale,
					endScale, xvel, Util.FIRESPEED));
	}

	@Override
	public void update(float dt) {
		if ( addParticle && fires.size() < limit ) {
			int xvel = rand.nextInt(10) + 1;
			if (rand.nextBoolean()) {
				xvel *= -1;
			}
			fires.add(new FireParticle(initPos, (Math.max(0.56f, rand.nextFloat()))*particleLifetime, beginScale,
					endScale, xvel, Util.FIRESPEED));
			addParticle = false;
		} else {
			addParticle = true;
		}
		for (FireParticle f : fires) {
			if (f.isDead()) {
				if (life < lifetime) {
					int xvel = rand.nextInt(10) + 1;
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
		for (FireParticle f : fires) {
			f.draw(batch);
		}
	}

	@Override
	public boolean isEnded() {
		return life >= lifetime;
	}

}
