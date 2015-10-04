package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
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
	private Sprite fly3;
	private Sprite stand;
	private float lasttime;
	private float standTime;
	private int frame;
	private Body collisionBody;
	private boolean setStand = false;

	public Crow(Vector3 pos, float speed) {
		super(pos, 0f);
		this.setVel(speed, 0, 0);
		fly1 = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crow2fly"));
		fly2 = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crow4fly"));
		fly3 = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crow3fly"));
		stand = new Sprite(WitchCraft.assetManager.get(
				"data/world/otherart.atlas", TextureAtlas.class).findRegion(
				"crowstand"));
		lasttime = 0;
		frame = 0;
		standTime = -1;
		buildCollisionBody();
	}

	public void update(float dt) {
		if ( standTime < 0 && !setStand) {
			pos.x += vel.x*dt;
			pos.y += vel.y*dt;
		}
		collisionBody.setTransform((pos.x+32)*Util.PIXEL_TO_BOX, (pos.y+64)*Util.PIXEL_TO_BOX, 0);

		if ( standTime >= 0 && !setStand) {
			standTime -= dt;
			lasttime = 0;
		} else {
			lasttime+=dt;
		}
	}

	public void draw(Batch batch) {
		if (standTime < 0 && !setStand) {
			if ( lasttime > 0.1f ){
				frame = frame +1 > 2 ? 0 : frame + 1;
				lasttime = 0;
			}
			if (frame == 0) {
				fly1.setPosition(pos.x, pos.y);
				fly1.draw(batch);
			}
			else if (frame == 1) {
				fly2.setPosition(pos.x, pos.y+20);
				fly2.draw(batch);
			}
			else if (frame == 2) {
				fly3.setPosition(pos.x, pos.y+20);
				fly3.draw(batch);
			}
		} else {
			stand.setPosition(pos.x, pos.y);
			stand.draw(batch);
			lasttime = 0;
		}
	}
	
	public void setStandTime(float val) {
		standTime = val;
	}
	
	public void setStand(boolean val) {
		setStand = val;
	}
	
	public float getStandTime() {
		return standTime;
	}
	
	public boolean isFlipX() {
		return fly1.isFlipX();
	}
	
	public void setFlipX(boolean val) {
//		if ( val ) {
//			fly1.setScale(-1,1);
//			fly2.setScale(-1,1);
//			fly3.setScale(-1,1);
//			stand.setScale(-1,1);
//		} else {
//			fly1.setScale(1,1);
//			fly2.setScale(1,1);
//			fly3.setScale(1,1);
//			stand.setScale(1,1);			
//		}
		boolean flip = fly1.isFlipX() != val ? true : false;
		fly1.flip(flip, false);
		fly2.flip(flip, false);
		fly3.flip(flip, false);
		stand.flip(flip, false);
	}
			
	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		switch (other.type) {
		case PLATFORM:
			stop();
			standTime = 6;
		case STAIRS:
			stop();
			standTime = 6;
			break;
		default: 
			break;
		}

	}
	
	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.getPos().x, this.getPos().y));
		collisionBody = GamePlayManager.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32 * Util.PIXEL_TO_BOX, 32 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_TRIGGERS;
		fixture.filter.maskBits = Util.CATEGORY_ENVIRONMENT | Util.CATEGORY_ITEMS | Util.CATEGORY_PARTICLES;
		collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
	}

}
