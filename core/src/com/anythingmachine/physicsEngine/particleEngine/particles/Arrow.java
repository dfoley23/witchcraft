package com.anythingmachine.physicsEngine.particleEngine.particles;

import java.util.ArrayList;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.WitchCraft;
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

public class Arrow extends Particle {
	private Body collisionBody;
	private Sprite sprite;
	private boolean isStuck = false;

	public Arrow(Vector3 pos, Vector3 vel) {
		super(pos, vel);
		this.type = EntityType.ARROW;
		buildCollisionBody();
		GamePlayManager.rk4System.addParticle(this);
		sprite = ((TextureAtlas) WitchCraft.assetManager.get("data/spine/characters.atlas"))
				.createSprite("archer_xcf-aroow");
		sprite.scale(-0.4f);
		this.externalForce = new Vector3(0, Util.GRAVITY, 0);
	}

	public void destroy() {
		GamePlayManager.world.destroyBody(collisionBody);
	}

	public ArrayList<Particle> getParticles() {
		ArrayList<Particle> list = new ArrayList<Particle>();
		list.add(this);
		return list;
	}

	public void draw(Batch batch) {
		if (!WitchCraft.cam.inBigBounds(pos) && !destoryNextStep) {
			stable = true;
			if (!destroyed && collisionBody.isAwake()) {
				collisionBody.setAwake(false);
			}
		} else {
			float costheta = 0;
			if (!destroyed) {
				costheta = Util.dot(vel, new Vector3(1, 0, 0)) / vel.len();
				sprite.setPosition(0, 0);
				float rot = vel.y > 0 ? (float) Math.acos((double) costheta) : -(float) Math.acos((double) costheta);
				sprite.setRotation(rot * Util.RAD_TO_DEG);
			}
			if (!destroyed) {
				if (!collisionBody.isAwake()) {
					collisionBody.setAwake(true);
				}
				collisionBody.setTransform((pos.x + sprite.getWidth() * 0.5f) * Util.PIXEL_TO_BOX,
						(pos.y + sprite.getHeight() * 0.5f) * Util.PIXEL_TO_BOX, (float) Math.acos((double) costheta));
			}
			sprite.setPosition(pos.x, pos.y);
			sprite.draw(batch);
		}
	}

	public Vector2 getPos2D() {
		return new Vector2(pos.x, pos.y);
	}

	public Vector3 getVel() {
		return vel;
	}

	public Vector2 getVel2D() {
		return new Vector2(vel.x, vel.y);
	}

	@Override
	public void setStable(boolean val) {
		stable = val;
		if (!destroyed)
			collisionBody.setActive(!val);
	}

	public void setPos(float x, float y, float z) {
		this.pos.set(x, y, z);
		this.stable = false;
	}

	public void pointAtTarget(Vector3 target, float speed, float extraHeight) {
		Vector3 dir = new Vector3(target.x - pos.x, (target.y + extraHeight) - pos.y, 0);
		dir.scl(1 / dir.len());
		this.vel.set(dir.x * speed, dir.y * speed, 0);
		float costheta = Util.dot(vel, new Vector3(1, 0, 0)) / vel.len();
		sprite.setRotation((float) Math.acos((double) costheta) * Util.RAD_TO_DEG);
		sprite.setPosition(pos.x, pos.y);
		if (!destroyed)
			collisionBody.setTransform(pos.x * Util.PIXEL_TO_BOX, pos.y * Util.PIXEL_TO_BOX,
					(float) Math.acos((double) costheta));
	}

	public void setVel(float x, float y, float z) {
		this.vel.set(x, y, z);
		float costheta = Util.dot(vel, new Vector3(1, 0, 0)) / vel.len();
		sprite.setOrigin(0, 0);
		sprite.setRotation((float) Math.acos((double) costheta) * Util.RAD_TO_DEG);
		sprite.setPosition(pos.x, pos.y);
		if (!destroyed)
			collisionBody.setTransform(pos.x * Util.PIXEL_TO_BOX, pos.y * Util.PIXEL_TO_BOX,
					(float) Math.acos((double) costheta));
	}

	public void addPos(float x, float y) {
		this.pos.add(x, y, 0);
	}

	public void apply2DImpulse(float x, float y) {
		this.externalForce.set(x, y, 0);
	}

	public void applyImpulse(Vector3 force) {
		this.externalForce = force;
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		if (other.type == EntityType.PLATFORM || other.type == EntityType.WALL || other.type == EntityType.STAIRS) {
			stop();
			this.externalForce.y = 0;
			isStuck = true;
		}
	}

	@Override
	public Vector3 accel(Vector3 pos, Vector3 vel, float t) {
		Vector3 result = new Vector3(0, this.externalForce.y, 0);
		return result;
	}

	@Override
	public void integratePos(Vector3 dxdp, float dt) {
		this.pos.add(Util.sclVec(dxdp, dt));
	}

	@Override
	public void integrateVel(Vector3 dvdp, float dt) {
		this.vel.add(Util.sclVec(dvdp, dt));
		// externalForce = new Vector3(0, 0, 0);
		float costheta = Util.dot(vel, new Vector3(0, 0, 1)) / vel.len();
		sprite.setPosition(0, 0);
		sprite.setRotation((float) Math.acos((double) costheta) * Util.RAD_TO_DEG);
		sprite.setPosition(pos.x, pos.y);
		if (!destroyed)
			collisionBody.setTransform(pos.x * Util.PIXEL_TO_BOX, pos.y * Util.PIXEL_TO_BOX,
					(float) Math.acos((double) costheta));
	}

	public boolean isStable() {
		return stable;
	}

	public boolean isStuck() {
		return isStuck;
	}

	@Override
	public void destroyBody() {
		if ( GamePlayManager.world.isLocked() ) {
			destoryNextStep = true;
		} else if (!destroyed) {
			GamePlayManager.world.destroyBody(collisionBody);
			destroyed = true;
		}
	}

	private void buildCollisionBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(this.getPos().x, this.getPos().y));
		collisionBody = GamePlayManager.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		collisionBody.setBullet(true);
		shape.setAsBox(32 * Util.PIXEL_TO_BOX, 4 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PARTICLES;
		fixture.filter.maskBits = Util.CATEGORY_NPC | Util.CATEGORY_PLAYER | Util.CATEGORY_PLATFORMS;
		collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();
	}
}
