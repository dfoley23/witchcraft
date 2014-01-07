package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TexturedParticle extends Particle {
	protected Sprite sprite;
	
	public TexturedParticle (Vector3 pos, EntityType type, Sprite sprite, Vector3 extForce) {
		super(pos);
		this.sprite = sprite;
		this.sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		this.stable = false;
		this.externalForce = extForce.cpy();
		this.type = type;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public TexturedParticle copy(Vector3 pos, Vector3 ext) {
		return new TexturedParticle(pos, this.type, new Sprite(sprite), ext);
	}
	
	@Override
	public Vector3 accel(Particle p, float t) {
		Vector3 result = new Vector3(0, 0, 0);
		result.add(externalForce);
		return result;
	}

	@Override
	public void integratePos(Vector3 dxdp, float dt) {
		this.pos.add(Util.sclVec(dxdp, dt));
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		this.vel.add(Util.sclVec(dvdp, dt));		
	}

}
