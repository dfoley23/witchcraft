package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class TexturedParticle extends Particle {
	protected Sprite sprite;
	
	public TexturedParticle (Vector3 pos, EntityType type, Sprite sprite, Vector3 extForce) {
		super(pos);
		this.sprite = sprite;
		this.sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		this.stable = false;
		this.externalForce = extForce;
		this.type = type;
	}
	
	@Override
	public void draw(Batch batch) {
		sprite.draw(batch);
	}

	public TexturedParticle copy(Vector3 pos, Vector3 ext) {
		return new TexturedParticle(pos, this.type, new Sprite(sprite), ext);
	}
	
	@Override
	public Vector3 accel(Vector3 pos, Vector3 vel, float t) {
		return externalForce;
	}

	@Override
	public void integratePos(Vector3 dxdp, float dt) {
		Vector3 ds = Util.sclVec(dxdp, dt);
		this.pos.x += ds.x;
		this.pos.y += ds.y;
		this.pos.z += ds.z;
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		Vector3 ds = Util.sclVec(dvdp, dt);
		this.vel.x += ds.x;
		this.vel.y += ds.y;
		this.vel.z += ds.z;
	}

}
