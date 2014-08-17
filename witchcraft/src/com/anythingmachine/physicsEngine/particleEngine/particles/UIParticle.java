package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class UIParticle extends Particle {
	private Vector3 target;
	private Sprite sprite;
	private float waittime;
	private float time;
	
	public UIParticle (Vector3 pos, String region, String atlas, float time) {
		super(pos);
		target = pos;
		this.stable = true;
		sprite = new Sprite(((TextureAtlas) WitchCraft.assetManager
						.get(atlas))
						.findRegion(region));
		sprite.setPosition(pos.x, pos.y);
		waittime = time;
	}
	
	@Override
	public void draw(Batch batch) {
		sprite.draw(batch);		
	}
	
	@Override
	public void addPos(float x, float y) {
		this.pos.add(x, y, 0);
	}

	public void update(float dt) {
//		pos.lerp(target, dt);
		time += dt;
		sprite.setPosition(pos.x, pos.y);
	}
	
	public boolean isOverTime() {
		return time > waittime;
	}
	
	public void setTarget(Vector3 t) {
		target = t;
	}
	
	public float getHeight() {
		return sprite.getHeight();
	}
	
	public float getWidth() {
		return sprite.getWidth();
	}
}
