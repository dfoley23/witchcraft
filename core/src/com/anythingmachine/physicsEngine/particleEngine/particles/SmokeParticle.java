package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.badlogic.gdx.math.Vector3;

public class SmokeParticle extends CloudParticle {
	
	public SmokeParticle (Vector3 pos, float lifetime, float beginScale, float endScale, float speedX, float speedY) {
		super(pos, lifetime,beginScale,endScale,speedX,speedY);
//		sprite = new Sprite(WitchCraft.assetManager.get("data/world/otherart.atlas", TextureAtlas.class).findRegion("fireTex"));
		sprite.setScale(beginScale*0.7f, beginScale);
		sprite.setColor(0.6f, 0.6f, 0.6f, 0.7f);
	}
		
	@Override
	public void update(float dt) {
		life += dt;
		this.pos.x += this.vel.x*dt;
		this.pos.y += this.vel.y*dt;
        scale += dS*dt;
		sprite.setScale(scale*0.7f, scale);
	}

}
