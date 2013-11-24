package com.anythingmachine.witchcraft.agents.player;

import java.util.ArrayList;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.Agent;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class Player extends Agent {
	private Body body;
	private boolean facingLeft;
	private boolean onGround;
	private Cape cape;
	private Skeleton skel;
	private Animation jump;
	private Animation walk;
	private Animation idle;
	private Animation animation;
	private float totalTime = 0f;
	private State playerState;
	private Bone neck;
	private TextureAtlas atlas;
	private InputManager input;
	
	public Player(World world, Ground ground, RK4Integrator rk4) {
		createBody(world);
		this.playerState = State.IDLE;
		this.ground = ground;
		this.curGroundSegment = 0;
		this.curCurve = ground.getCurve(curGroundSegment);

		this.input = new InputManager();
		setupInput();
		
		atlas = new TextureAtlas(Gdx.files.internal("data/spine/player.atlas"));

		setupSkeleton(world);

		cape = new Cape(3, 5, rk4);
		
	}

	public void update(float dT) {
		
		float delta = Gdx.graphics.getDeltaTime();

		totalTime += delta;
		Vector2 pos = body.getPosition().mul(Util.PIXELS_PER_METER)
				.sub(0.0f, 32f);
		if (pos.x > curCurve.lastPointOnCurve().x) {
			curGroundSegment++;
			curCurve = ground.getCurve(curGroundSegment);
		} else if (pos.x < curCurve.firstPointOnCurve().x) {
			curGroundSegment--;
			curCurve = ground.getCurve(curGroundSegment);
		}
		Vector2 groundPoint = ground.findPointOnCurve(curGroundSegment, pos.x);
		onGround = false;
		if (pos.y < groundPoint.y) {
			correctHeight(groundPoint.y);
			onGround = true;
			playerState = playerState.land();
		}
		boolean moving = false;
		if ( input.is("Right") || input.is("Left")) {
			updateWalking();
			moving = true;
		}
		
		skel.setFlipX(facingLeft);
		
		if( input.is("Fly") ) {
			updateFlying();
		} 
		updateSkeleton(moving, delta);
	}

	public void draw(SpriteBatch batch, Matrix4 cam) {
		skel.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
		cape.draw(cam);
	}
	public Vector2 getPosMeters() {
		return body.getPosition().sub(0, (32f) * (1.f / Util.PIXELS_PER_METER));
	}

	public Vector2 getPosPixels() {
		return body.getPosition().mul(Util.PIXELS_PER_METER);
	}

	public int getCurSegment() {
		return curGroundSegment;
	}

	public void correctHeight(float y) {
		// body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
		body.setTransform(body.getPosition().x, (y + 32f)
				* (1.f / Util.PIXELS_PER_METER), 0.0f);
	}

	public void incrementSegment() {
		this.curGroundSegment++;
	}

	public void decrementSegment() {
		this.curGroundSegment++;
	}

	private void updateFlying() {
		body.setGravityScale(0.5f);
		body.applyLinearImpulse(new Vector2(0.0f, 0.2f),
				body.getWorldCenter());
		if (onGround && playerState != State.JUMPING) {
			skel.setToBindPose();
			Bone root = skel.getRootBone();
			root.setScaleX(0.5f);
			root.setScaleY(0.5f);
			playerState = State.JUMPING;
			animation = jump;
			totalTime = 0;
		}
	}
	
	private void updateSkeleton(boolean moving, float delta) {
		Bone root = skel.getRootBone();
		root.setX(getPosPixels().x - 8f);
		root.setY(getPosPixels().y - 32f);
		if (playerState != State.FLYING) {
			cape.addWindForce(0, -400);
			if (totalTime > animation.getDuration() && !moving) {
				totalTime = 0;
				playerState = State.IDLE;
				animation = idle;
				skel.setToBindPose();
				root.setX(getPosPixels().x - 8f);
				root.setY(getPosPixels().y - 32f);
				root.setScaleX(0.5f);
				root.setScaleY(0.5f);
			} else if (onGround || playerState != State.JUMPING
					|| totalTime < 0.95f) {
				animation.apply(skel, totalTime, true);
			} else {
				playerState = State.FLYING;
				if ( facingLeft ) {
					cape.addWindForce(-500, 0);
				} else {
					cape.addWindForce(-500, 0);					
				}
			}
		}
		skel.updateWorldTransform();
		skel.update(delta);
		cape.flip(facingLeft);
		if (facingLeft) {
			if (!onGround) {
				cape.updatePos(neck.getWorldX(),
						neck.getWorldY()+20, true, true);
			} else {
				cape.updatePos(neck.getWorldX()+24,
						neck.getWorldY()-3, true, false);
			}
		} else {
			if (!onGround) {
				cape.updatePos(neck.getWorldX()-8,
						neck.getWorldY()+25, false, true);
			} else {
				cape.updatePos(neck.getWorldX()+10,
						neck.getWorldY()-3, false, false);
			}
		}
		// Position each attachment body.
		for (Slot slot : skel.getSlots()) {
			if (slot.getAttachment() != null) {
				RegionAttachment attachment = (RegionAttachment) slot
						.getAttachment();
				if (attachment.body == null)
					continue;
				float x = (root.getX() + slot.getBone().getX())
						* Util.PIXEL_TO_BOX;
				float y = (root.getY() + slot.getBone().getY())
						* Util.PIXEL_TO_BOX;
				float rotation = slot.getBone().getRotation();
				attachment.body.setTransform(x, y, rotation * Util.DEG_TO_RAD);
			}
		}
	}
	
	private void updateWalking() {
		if (onGround && !input.is("Fly") && playerState != State.WALKING) {
			totalTime = 0;
			skel.setToBindPose();
			Bone root = skel.getRootBone();
			root.setScaleX(0.5f);
			root.setScaleY(0.5f);
			playerState = State.WALKING;
			animation = walk;
		}
		if (input.is("Right")) {
			if (!onGround) {
				body.applyLinearImpulse(new Vector2(0.15f, 0.0f),
					body.getWorldCenter());
			} else {
				body.applyLinearImpulse(new Vector2(0.05f, 0.0f),
						body.getWorldCenter());
			}
			facingLeft = false;
		} else if (input.is("Left")) {
			if (!onGround) {
				body.applyLinearImpulse(new Vector2(-0.15f, 0.0f),
						body.getWorldCenter());
			} else {
				body.applyLinearImpulse(new Vector2(-0.05f, 0.0f),
						body.getWorldCenter());
			}
			facingLeft = true;
		}
	}
	private void createBody(World world) {

		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(3.0f, 3.0f);

		body = world.createBody(jumperBodyDef);

		PolygonShape jumperShape = new PolygonShape();
		jumperShape.setAsBox(32f / (2 * Util.PIXELS_PER_METER),
				64f / (2 * Util.PIXELS_PER_METER));

		body.setFixedRotation(true);

		FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 1.0f;
		jumperFixtureDef.friction = 5.0f;
		jumperFixtureDef.isSensor = true;

		body.createFixture(jumperFixtureDef);
		body.setLinearDamping(3);
		jumperShape.dispose();
	}

	private void setupInput() {
		input.addInputState("Left", Keys.LEFT);
		input.addInputState("Right", Keys.RIGHT);
		input.addInputState("Fly", Keys.UP);
		input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
		input.addInputState("UsePower", Keys.SPACE);		
		input.addInputState("Interact", Keys.A);
	}
	
	private void setupSkeleton(World world) {
		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/player.skel"));

		jump = sb.readAnimation(
				Gdx.files.internal("data/spine/player-jump.anim"), sd);

		walk = sb.readAnimation(
				Gdx.files.internal("data/spine/player-walk.anim"), sd);

		idle = sb.readAnimation(
				Gdx.files.internal("data/spine/player-idle.anim"), sd);
		animation = idle;
		skel = new com.esotericsoftware.spine.Skeleton(sd);

		skel.updateWorldTransform();

		skel.setSkin("player");
		skel.setToBindPose();
		ArrayList<String> names = new ArrayList<String>();
		for (Slot slot : skel.getSlots()) {
			// System.out.println(slot.getBone().getData().getName());
			if (!names.contains(slot.getBone().getData().getName()))
				names.add(slot.getBone().getData().getName());
		}
		for (Slot slot : skel.getSlots()) {
			if (slot.getAttachment() != null
					&& names.contains(slot.getBone().getData().getName())) {
				RegionAttachment attachment = (RegionAttachment) slot
						.getAttachment();
				if (slot.getBone().getData().getName().equals("left shoulder"))
					attachment.setRegion(atlas.findRegion("left-shoulder"));
				else if (slot.getBone().getData().getName().equals("left arm"))
					attachment.setRegion(atlas.findRegion("left-arm"));
				else if (slot.getBone().getData().getName().equals("left hand"))
					attachment.setRegion(atlas.findRegion("left-hand"));
				else if (slot.getBone().getData().getName().equals("left foot"))
					attachment.setRegion(atlas.findRegion("left-foot"));
				else if (slot.getBone().getData().getName()
						.equals("left lower leg"))
					attachment.setRegion(atlas.findRegion("left-lower-leg"));
				else if (slot.getBone().getData().getName()
						.equals("left upper leg"))
					attachment.setRegion(atlas.findRegion("left-upper-leg"));
				else if (slot.getBone().getData().getName().equals("torso"))
					attachment.setRegion(atlas.findRegion("torso"));
				else if (slot.getBone().getData().getName().equals("pelvis"))
					attachment.setRegion(atlas.findRegion("pelvis"));
				else if (slot.getBone().getData().getName()
						.equals("right foot"))
					attachment.setRegion(atlas.findRegion("right-foot"));
				else if (slot.getBone().getData().getName()
						.equals("right lower leg"))
					attachment.setRegion(atlas.findRegion("right-lower-leg"));
				else if (slot.getBone().getData().getName()
						.equals("right upper leg"))
					attachment.setRegion(atlas.findRegion("right-upper-leg"));
				else if (slot.getBone().getData().getName().equals("head"))
					attachment.setRegion(atlas.findRegion("head"));
				else if (slot.getBone().getData().getName()
						.equals("right shoulder"))
					attachment.setRegion(atlas.findRegion("right-shoulder"));
				else if (slot.getBone().getData().getName().equals("right arm"))
					attachment.setRegion(atlas.findRegion("right-arm"));
				else if (slot.getBone().getData().getName()
						.equals("right hand"))
					attachment.setRegion(atlas.findRegion("right-hand"));

				names.remove(slot.getBone().getData().getName());
				PolygonShape boxPoly = new PolygonShape();
				boxPoly.setAsBox((attachment.getRegion().getRegionWidth() / 8f)
						* Util.PIXEL_TO_BOX, (attachment.getRegion()
						.getRegionHeight() / 8f) * Util.PIXEL_TO_BOX,
						new Vector2(slot.getBone().getX(), slot.getBone()
								.getY()).mul(Util.PIXEL_TO_BOX), 0.0f);

				BodyDef boxBodyDef = new BodyDef();
				boxBodyDef.type = BodyType.StaticBody;

				attachment.body = world.createBody(boxBodyDef);
				attachment.body.createFixture(boxPoly, 1);

				slot.setAttachment(attachment);
				// boxPoly.dispose();
			}
		}
		Bone root = skel.getRootBone();
		root.setX(getPosPixels().x - 8f);
		root.setY(getPosPixels().y - 8f);
		root.setScaleX(0.5f);
		root.setScaleY(0.5f);

		neck = skel.findBone("neck");
	}
}
