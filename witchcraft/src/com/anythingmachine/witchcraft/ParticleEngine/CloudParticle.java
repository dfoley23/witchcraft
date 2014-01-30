package com.anythingmachine.witchcraft.ParticleEngine;

import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class CloudParticle extends KinematicParticle {
	private Sprite sprite;
	
	public CloudParticle(Vector3 pos, float scale, float rot, float speed) {
		super(pos, 0);
		this.setVel(speed, 0, 0);
		sprite = new Sprite(WitchCraft.assetManager.get("data/dust.png", Texture.class));
		sprite.setRotation(rot);
		sprite.scale(scale);
	}

	public void update(float dt) {
		pos.x += vel.x*dt;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setPosition(pos.x, pos.y);
		sprite.draw(batch);
	}
		
	public void setPos(float x, float y ) {
		sprite.setPosition(x, y);
		setPos(x, y, 0);
	}

	public void setScale(float scale) {
		sprite.scale(scale);
	}
	
	public void setRotation(float rotation) {
		sprite.setRotation(rotation);
	}
}
