package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class TexturedParticle extends KinematicParticle {
	protected Sprite sprite;
	
	public TexturedParticle (Vector3 pos, EntityType type, Sprite sprite, Vector3 extForce) {
		super(pos, extForce.y);
		this.sprite = sprite;
		this.sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		this.stable = false;
		this.externalForce = extForce;
		this.type = type;
	}
	
	@Override
	public void draw(Batch batch) {		
		sprite.setPosition(pos.x, pos.y);
		sprite.draw(batch);
	}
	
}
