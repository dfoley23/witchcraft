package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class FireParticle extends CloudParticle {
	
	public FireParticle (Vector3 pos, float lifetime, float beginScale, float endScale, float speedX, float speedY) {
		super(pos, lifetime,beginScale,endScale,speedX,speedY);
		sprite = new Sprite(WitchCraft.assetManager.get("data/world/otherart.atlas", TextureAtlas.class).findRegion("fireTex"));
		sprite.setScale(beginScale*0.7f, beginScale);
//		float colorRed = (float)Math.random();
//		sprite.setColor(0.7f, 0.4f, 0.1f, 0.9f);
	}
	
	@Override
	public void update(float dt) {
		life += dt;
//		float theta = (((life - 0) * (720 - 0)) / (lifetime - 0));
//		this.vel.x = 100*(float)Math.sin(Util.DEG_TO_RAD*theta);
		this.pos.x += this.vel.x*dt;
		this.pos.y += this.vel.y*dt;
        scale += dS*dt;
		sprite.setScale(scale*0.7f, scale);
	}
	
}
