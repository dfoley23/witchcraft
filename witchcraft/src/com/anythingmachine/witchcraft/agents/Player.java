package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Player extends Agent {
	private Body body;
	private boolean facingLeft;
	private boolean inAir;
	private Skeleton skel;
	private Animation jump;
	private Animation walk;
	private Animation animation;
	private float totalTime = 0f;
	private PlayerState playerState;
	public enum PlayerState { 
		IDLE, WALKING, JUMPING
	}
	
	public Player( World world, Ground ground ) {
		createBody( world );
		playerState = PlayerState.IDLE;
		this.ground = ground;
		this.curGroundSegment = 0;
		curCurve = ground.getCurve(curGroundSegment);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/spine/spineboy.atlas"));
        SkeletonBinary sb = new SkeletonBinary(atlas);
        SkeletonData sd = sb.readSkeletonData(Gdx.files
                .internal("data/spine/spineboy.skel"));

        jump = sb.readAnimation(
                Gdx.files.internal("data/spine/spineboy-jump.anim"), sd);

        walk = sb.readAnimation(
                Gdx.files.internal("data/spine/spineboy-walk.anim"), sd);
        animation = null;
        skel = new com.esotericsoftware.spine.Skeleton(sd);
        skel.setToBindPose();
        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x - 16f);
        root.setY(getPosPixels().y - 8f);
        root.setScaleX(1f);
        root.setScaleY(1f);
        skel.updateWorldTransform();
	}
	
	public void update( float dT ) {
		float delta = Gdx.graphics.getDeltaTime();
		
		totalTime += delta;
		/**
		 * Detect requested motion.
		 */
		if ( Gdx.input.isKeyPressed(Input.Keys.P))
			Gdx.app.log("players position in pixels", "" + body.getPosition().mul(Util.PIXELS_PER_METER));
			
		boolean moveLeft = false;
		boolean moveRight = false;
		boolean doJump = false;

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			moveRight = true;
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() > Gdx.graphics.getWidth() * 0.80f) {
					moveRight = true;
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			moveLeft = true;
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() < Gdx.graphics.getWidth() * 0.20f) {
					moveLeft = true;
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			doJump = true;
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getY() < Gdx.graphics.getHeight() * 0.20f) {
					doJump = true;
				}
			}
		}

		/**
		 * Act on that requested motion.
		 * 
		 * This code changes the jumper's direction. It's handled separately
		 * from the jumping so that the player can jump and move simultaneously.
		 * The horizontal figure was arrived at experimentally -- try other
		 * values to experience different speeds.
		 * 
		 * The impulses are applied to the center of the jumper.
		 */
		if (moveRight) {
			body.applyLinearImpulse(new Vector2(0.05f, 0.0f),
					body.getWorldCenter());
			facingLeft = false;
			skel.setFlipX(facingLeft);
			if ( !inAir && playerState != PlayerState.WALKING )
			{
				totalTime = 0;
				playerState = PlayerState.WALKING;
				animation = walk;
			}
		} else if (moveLeft) {
			body.applyLinearImpulse(new Vector2(-0.05f, 0.0f),
					body.getWorldCenter());
			facingLeft = true;
			skel.setFlipX(facingLeft);
			if( !inAir && playerState != PlayerState.WALKING )
			{
				totalTime = 0;
				playerState = PlayerState.WALKING;
				animation = walk;
			}
		}
		
		Vector2 pos = body.getPosition().mul(Util.PIXELS_PER_METER).sub(0.0f, 32f);
		if ( pos.x > curCurve.lastPointOnCurve().x) {
			curGroundSegment++;
			curCurve = ground.getCurve(curGroundSegment);
		} else if ( pos.x < curCurve.firstPointOnCurve().x ) {
			curGroundSegment--;
			curCurve = ground.getCurve(curGroundSegment);
		}
		boolean onGround = false;
		Vector2 groundPoint = ground.findPointOnCurve(curGroundSegment, pos.x);
		if ( pos.y < groundPoint.y ) {
			correctHeight(groundPoint.y);
			onGround = true;
		}

		/**
		 * The jumper dude can only jump while on the ground. There are better
		 * ways to detect ground contact, but for our purposes it is sufficient
		 * to test that the vertical velocity is zero (or close to it). As in
		 * the above code, the vertical figure here was found through
		 * experimentation. It's enough to get the guy off the ground.
		 * 
		 * As before, impulse is applied to the center of the jumper.
		 */
		if (doJump && (Math.abs(body.getLinearVelocity().y) < 1e-9 || onGround)) {
			inAir = true;
			if (playerState != PlayerState.JUMPING) {
				playerState = PlayerState.JUMPING;
				animation = jump;
				totalTime = 0;
			}
		} else {
			doJump = false;
			inAir = false;
		}


		if ( animation != null ) {
			if ( totalTime > animation.getDuration() && !moveLeft && !moveRight) {
				totalTime = 0;
				playerState = PlayerState.IDLE;
				animation = null;
		        skel.setToBindPose(); 
		        Bone root = skel.getRootBone();
		        root.setX(getPosPixels().x - 16f);
		        root.setY(getPosPixels().y - 32f);
			} else { 
		        Bone root = skel.getRootBone();
		        root.setX(getPosPixels().x - 16f);
		        root.setY(getPosPixels().y - 32f);
				animation.apply(skel, totalTime, true);
			}
		}
        skel.updateWorldTransform();
        skel.update(delta);
	}
	
	public void draw( SpriteBatch batch ) {
        skel.draw(batch);
	}
	
	public Vector2 getPosMeters() {
		return body.getPosition().sub(0, (32f)*(1.f / Util.PIXELS_PER_METER));
	}
	
	public Vector2 getPosPixels() {
		return body.getPosition().mul(Util.PIXELS_PER_METER);
	}
	public int getCurSegment() {
		return curGroundSegment;
	}
	
	public void correctHeight(float y) {
		//body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
		body.setTransform(body.getPosition().x, (y+32f)*(1.f / Util.PIXELS_PER_METER), 0.0f);
	}
	
	public void incrementSegment() {
		this.curGroundSegment++;
	}
	
	public void decrementSegment() {
		this.curGroundSegment++;
	}
	
	private void createBody( World world ) {

		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(3.0f, 3.0f);

		body = world.createBody(jumperBodyDef);

		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		PolygonShape jumperShape = new PolygonShape();
		jumperShape.setAsBox(32f / (2 * Util.PIXELS_PER_METER),
				64f / (2 * Util.PIXELS_PER_METER));

		/**
		 * The character should not ever spin around on impact.
		 */
		body.setFixedRotation(true);

		/**
		 * The density and friction of the jumper were found experimentally.
		 * Play with the numbers and watch how the character moves faster or
		 * slower.
		 */
		FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 1.0f;
		jumperFixtureDef.friction = 5.0f;

		body.createFixture(jumperFixtureDef);
		body.setLinearDamping(5);
		jumperShape.dispose();
	}
}
