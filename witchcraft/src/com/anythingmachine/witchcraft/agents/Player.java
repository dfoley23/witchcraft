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
	private float totalTime = 0f;
	private float animationDuration;
	private float timeLeft = 0f;
	private PlayerState playerState;
	public enum PlayerState { 
		IDLE, WALKING, JUMPING, WALKJUMP, 
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

        skel = new com.esotericsoftware.spine.Skeleton(sd);
        skel.setToBindPose();
        Bone root = skel.getRootBone();
        root.setX(100);
        root.setY(100);
        root.setScaleX(1f);
        root.setScaleY(1f);
        skel.updateWorldTransform();
	}
	
	public void update( float dT ) {
		totalTime += dT;
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
			if ( inAir ) {
				float alpha = 1 - animationDuration / jump.getDuration( );
				walk.mix( skel, animationDuration, false, alpha );
				animationDuration = walk.getDuration( ) * alpha;
			} else {
				walk.apply(skel, totalTime, true);
				animationDuration = walk.getDuration();
			}
			playerState = PlayerState.WALKING;
			timeLeft = animationDuration;
		} else if (moveLeft) {
			body.applyLinearImpulse(new Vector2(-0.05f, 0.0f),
					body.getWorldCenter());
			facingLeft = true;
			skel.setFlipX(facingLeft);
			if ( inAir ) {
				float alpha = 1 - animationDuration / jump.getDuration( );
				walk.mix( skel, animationDuration, false, alpha );
				animationDuration = walk.getDuration( ) * alpha;
			} else {
				walk.apply(skel, totalTime, true);
				animationDuration = walk.getDuration();
			}
			playerState = PlayerState.WALKING;
			timeLeft = animationDuration;
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
			body.applyLinearImpulse(new Vector2(0.0f, 3f),
					body.getWorldCenter());
			inAir = true;
			if ( moveLeft || moveRight ) {
				float alpha = 1 - animationDuration / walk.getDuration( );
				jump.mix( skel, animationDuration, false, alpha );
				animationDuration = jump.getDuration( ) * alpha;
				playerState = PlayerState.WALKJUMP;
			} else {
				jump.apply(skel, 0, true);
				animationDuration = jump.getDuration();
				playerState = PlayerState.JUMPING;
			}
			timeLeft = animationDuration;
		} else {
			doJump = false;
			inAir = false;
		}
		 timeLeft -= dT;
		switch(playerState){
		case WALKING:
			if ( timeLeft > 0 ) {
				walk.apply(skel, totalTime, true);
			} else {
				walk.apply(skel, animationDuration, false);
			}
			break;
		case JUMPING:
			if ( timeLeft > 0 ) {
				jump.apply(skel, jump.getDuration(), true);
			} else {
				jump.apply(skel, animationDuration, false);
			}
			break;
		case WALKJUMP:
			break;
		default:
			break;
		}
		//walk.apply(skel, totalTime, false);
        skel.updateWorldTransform();
        skel.update(dT);
	}
	
	public void draw( SpriteBatch batch ) {
		/*sprite.setPosition(
				Util.PIXELS_PER_METER * body.getPosition().x
						- sprite.getWidth() / 2,
				Util.PIXELS_PER_METER * body.getPosition().y
						- sprite.getHeight() / 2);
		sprite.draw(batch);*/
        skel.findBone("root").setX((Util.PIXELS_PER_METER * body.getPosition().x) - 16f);
        skel.findBone("root").setY((Util.PIXELS_PER_METER * body.getPosition().y) - 32f);
        skel.draw(batch);
	}
	
	public Vector2 getPosMeters() {
		return body.getPosition().sub(0, (32f)*(1.f / Util.PIXELS_PER_METER));
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
		jumperBodyDef.position.set(1.0f, 3.0f);

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
		jumperShape.dispose();
	}
}
