package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class CloudParticle extends KinematicParticle {
	private Sprite sprite;
	
	public CloudParticle(Vector3 pos, float scale, float rot, float speed) {
		super(pos, 0);
		this.setVel(speed, 0, 0);
		sprite = new Sprite(WitchCraft.assetManager.get("data/world/otherart.atlas", TextureAtlas.class).findRegion("dust"));
		sprite.setRotation(rot);
		sprite.scale(scale);
	}

	public void update(float dt) {
		pos.x += vel.x*dt;
	}
	
	public void draw(Batch batch) {
		sprite.setPosition(pos.x, pos.y);
		sprite.draw(batch);
	}
		
	public void setScale(float scale) {
		sprite.scale(scale);
	}
	
	public void setRotation(float rotation) {
		sprite.setRotation(rotation);
	}
}