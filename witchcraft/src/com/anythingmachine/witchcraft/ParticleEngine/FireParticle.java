package com.anythingmachine.witchcraft.ParticleEngine;

import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class FireParticle extends KinematicParticle {
	protected Sprite sprite;
	private float lifetime;
	private float life;
	private float scale;
	private float startScale;
	private float dS;
	
	public FireParticle (Vector3 pos, float lifetime, float beginScale, float endScale, float speedX, float speedY) {
		super(pos, 0);
		this.lifetime = lifetime;
		this.life = 0;
		this.startScale = beginScale;
		this.setVel(speedX, speedY, 0);
		sprite = new Sprite(WitchCraft.assetManager.get("data/world/otherart.atlas", TextureAtlas.class).findRegion("dust"));
        sprite.setColor(0.7f, 0.1f, 0.1f, 0.9f);
		sprite.setScale(beginScale*0.7f, beginScale);
		this.dS = (endScale-beginScale)/lifetime;
	}
	
	@Override
	public void draw(Batch batch) {		
		if( life < lifetime ) {
			sprite.setPosition(pos.x, pos.y);
			sprite.draw(batch);
		}
	}
	
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
		} else if ( lifetime*0.25 < life) {
			sprite.setColor(0.7f, 0.1f, 0.1f, 0.9f);
		}
        scale += dS*dt;
		sprite.setScale(scale*0.7f, scale);
	}
	
	public void resetFire(Vector3 pos, float speedX, float speedY) {
		this.pos = new Vector3( pos.x, pos.y , pos.z);
		this.setVel(speedX, speedY, 0);
		scale = startScale;
		life = 0;
	}
	
	public boolean isDead() {
		return life >= lifetime;
	}


}
