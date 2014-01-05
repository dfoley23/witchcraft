package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TexturedParticle extends Particle {
	protected Sprite sprite;
	
	public TexturedParticle (Vector3 pos, EntityType type) {
		super(pos);
		this.stable = false;
		this.type = type;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public TexturedParticle copy(Vector3 pos) {
		return new TexturedParticle(pos, this.type);
	}
}
