package com.anythingmachine.witchcraft.ParticleEngine;

import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Crow extends KinematicParticle {
	private Sprite fly1;
	private Sprite fly2;
	private Sprite stand;
	private float lasttime;
	private float standTime;
	private boolean frame;
	private Body collisionBody;

	public Crow(Vector3 pos, float speed) {
		super(pos, 0f);
		this.setVel(speed, 0, 0);
		fly1 = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crow2fly"));
		fly2 = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crow3fly"));
		stand = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crowstand"));
		lasttime = 0;
		frame = true;
		standTime = -1;
		fly1.flip(speed > 0, false);
		fly2.flip(speed > 0, false);
		stand.flip(speed > 0, false);
		buildCollisionBody();
	}

	public void update(float dt) {
		if ( standTime < 0) {
			pos.x += vel.x*dt;
		}
		collisionBody.setTransform(pos.x*Util.PIXEL_TO_BOX, (pos.y-32)*Util.PIXEL_TO_BOX, 0);

	}

	public void draw(Batch batch) {
		float dt = Gdx.graphics.getDeltaTime();
		if (standTime < 0) {
			if ( lasttime > 0.25f ){
				frame = !frame;
				lasttime = 0;
			}
			if (frame) {
				fly1.setPosition(pos.x, pos.y);
				fly1.draw(batch);
			}
			else {
				fly2.setPosition(pos.x, pos.y);
				fly2.draw(batch);
			}
		} else {
			stand.setPosition(pos.x, pos.y);
			stand.draw(batch);
			standTime -= dt;
		}
		lasttime+=dt;
	}
	
	public void setSpeed(float val) {
		fly1.flip(val > 0, false);
		fly2.flip(val > 0, false);
		stand.flip(val > 0, false);
		setVel(val, 0, 0 );
	}
	public void setPos(float x, float y ) {
		setPos(x, y, 0);
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture) {
		standTime = 6;
	}
	
	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.getPos().x, this.getPos().y));
		collisionBody = WitchCraft.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32 * Util.PIXEL_TO_BOX, 32 * Util.PIXEL_TO_BOX);
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
