package com.anythingmachine.physicsEngine.particleEngine;

import com.anythingmachine.physicsEngine.TexturedBodyParticle;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class MindBeamParticle extends TexturedBodyParticle {
	private float scale = 0.5f;
	
	public MindBeamParticle(Vector3 pos, Sprite sprite, Vector3 ext) {
		super(pos,EntityType.PARTICLE, sprite, ext);
		sprite.setScale(1f, scale);
	}
	
	@Override
	public void draw(Batch batch) {
		scale += 0.02f;
		if ( scale < 2f ) {
			collisionBody.setTransform(Util.sclVecTo2(getPos(), Util.PIXEL_TO_BOX),
					collisionBody.getAngle());
			sprite.setScale(1f, scale);
			sprite.setPosition(pos.x, pos.y);
			sprite.setRotation(collisionBody.getAngle()*Util.RAD_TO_DEG);
			sprite.draw(batch);
		} else if ( !destroyed ){
			this.destroy();
		}
	}

	@Override
	public MindBeamParticle copy(Vector3 pos, Vector3 ext) {
		return new MindBeamParticle(pos, new Sprite(sprite), ext);
	}
}
