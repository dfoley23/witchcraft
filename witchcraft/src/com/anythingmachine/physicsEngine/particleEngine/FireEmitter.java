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
	private int limit;
	private Vector3 initPos;
	private Random rand;
	private float lifetime;
	private float life;
	private boolean hasStarted;

	public FireEmitter(Vector3 pos, int limit, float particleLifetime,
			float lifetime, float startScale, float endScale) {
		this.limit = limit;
		this.initPos = pos;
		this.lifetime = lifetime;
		fires = new ArrayList<FireParticle>();
		rand = new Random();
		for (int i = 0; i < limit; i++) {
			int xvel = rand.nextInt(3) + 1;
			if (rand.nextBoolean()) {
				xvel *= -1;
			}
			fires.add(new FireParticle(initPos, (rand.nextFloat()+0.5f)*particleLifetime, startScale,
					endScale, xvel, (rand.nextFloat()+0.5f)*Util.FIRESPEED));
		}
	}

	@Override
	public void update(float dt) {
		for (FireParticle f : fires) {
			if (f.isDead()) {
				if (life < lifetime) {
					int xvel = rand.nextInt(3) + 1;
					if (rand.nextBoolean()) {
						xvel *= -1;
					}
					f.resetFire(initPos, xvel, (rand.nextFloat()+0.5f)*Util.FIRESPEED);
				}
			}
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

	public boolean isDead() {
		return life >= lifetime;
	}

}
