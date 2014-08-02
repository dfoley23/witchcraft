package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.badlogic.gdx.math.Vector3;

public class FireParticle extends CloudParticle {
	
	public FireParticle (Vector3 pos, float lifetime, float beginScale, float endScale, float speedX, float speedY) {
		super(pos, lifetime,beginScale,endScale,speedX,speedY);
		sprite.setScale(beginScale*0.7f, beginScale);
//		float colorRed = (float)Math.random();
		sprite.setColor(0.7f, 0.1f, 0.1f, 0.9f);
	}
	
	@Override
	public void update(float dt) {
		life += dt;
		this.pos.x += this.vel.x*dt;
		this.pos.y += this.vel.y*dt;
		if( lifetime*0.75 < life) {
			sprite.setColor(1.0f, 0.7f, 0.1f, 0.9f);
		} else if ( lifetime*0.75 < life) {
			sprite.setColor(1f, 1.0f, 0.1f, 0.9f);
		} else if ( lifetime*0.5 < life) {
			sprite.setColor(0.7f, 0.7f, 0.1f, 0.9f);
		} else if ( lifetime*0.15 < life) {
			sprite.setColor(0.7f, 0.1f, 0.1f, 0.9f);
		}
        scale += dS*dt;
		sprite.setScale(scale*0.7f, scale);
	}
	
}
