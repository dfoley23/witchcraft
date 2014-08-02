package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class CloudParticle extends KinematicParticle {
	protected Sprite sprite;
	protected float lifetime;
	protected float life;
	protected float scale;
	protected float startScale;
	protected float dS;
	
	public CloudParticle (Vector3 pos, float lifetime, float beginScale, float endScale, float speedX, float speedY) {
		super(pos, 0);
		this.lifetime = lifetime;
		this.life = 0.0f;
		this.startScale = beginScale;
		this.setVel(speedX, speedY, 0);
		this.dS = (endScale-beginScale)/lifetime;
		this.scale = beginScale;
		sprite = new Sprite(WitchCraft.assetManager.get("data/world/otherart.atlas", TextureAtlas.class).findRegion("dust"));
		sprite.setScale(beginScale, beginScale);
		sprite.setColor(0.4f, 0.4f, 0.4f, 0.8f);
	}
	
	@Override
	public void draw(Batch batch) {		
		if( life < lifetime ) {
			sprite.setPosition(pos.x, pos.y);
			sprite.draw(batch);
		}
	}
		
	@Override
	public void update(float dt) {
		life += dt;
		this.pos.x += this.vel.x*dt;
		this.pos.y += this.vel.y*dt;
        scale += dS*dt;
		sprite.setScale(scale, scale);
	}
	
	public void reset(Vector3 pos, float speedX, float speedY) {
		this.pos = new Vector3( pos.x, pos.y , pos.z);
		this.setVel(speedX, speedY, 0);
		scale = startScale;
		life = 0;
	}
	
	public boolean isDead() {
		return life >= lifetime;
	}
		
	public void setFade(float fade) {
		this.sprite.setAlpha(fade);
		
	}

	public void setScale(float scale) {
		sprite.scale(scale);
	}
	
	public void setRotation(float rotation) {
		sprite.setRotation(rotation);
	}
}
