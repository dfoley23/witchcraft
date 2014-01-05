package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class TexturedBodyParticle extends TexturedParticle {
	private Body collisionBody;
	
	public TexturedBodyParticle(Vector3 pos, EntityType type ) {
		super(pos, type);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		buildCollisionBody();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		sprite.setPosition(pos.x, pos.y);
		sprite.setRotation(collisionBody.getAngle()*Util.RAD_TO_DEG);
		sprite.draw(batch);
	}
	
	public void destroy() {
		WitchCraft.world.destroyBody(collisionBody);
	}

	private void buildCollisionBody(){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(pos.x, pos.y));
		collisionBody = WitchCraft.world.createBody(def);
		collisionBody.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth() * Util.PIXEL_TO_BOX, sprite.getHeight() * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();	
	}
}
