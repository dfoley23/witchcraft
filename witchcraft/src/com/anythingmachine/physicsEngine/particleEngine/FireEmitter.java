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

	public FireEmitter(Vector3 pos, int limit, float particleLifetime,
			float lifetime, float startScale, float endScale) {
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
					f.reset(initPos, xvel, (rand.nextFloat()+0.5f)*Util.FIRESPEED);
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
