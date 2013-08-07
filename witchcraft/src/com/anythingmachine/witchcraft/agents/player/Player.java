package com.anythingmachine.witchcraft.agents.player;

import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.Agent;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	private Cape cape;
	private Skeleton skel;
	private Animation jump;
	private Animation walk;
	private Animation idle;
	private Animation animation;
	private float totalTime = 0f;
	private PlayerState playerState;
	private Bone neck;
	public enum PlayerState { 
		IDLE, WALKING, JUMPING, FLYING, LANDING,
	}
	
	public Player( World world, Ground ground ) {
		createBody( world );
		playerState = PlayerState.IDLE;
		this.ground = ground;
		this.curGroundSegment = 0;
		curCurve = ground.getCurve(curGroundSegment);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/spine/player.atlas"));
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
        skel.setSkin("player");
        skel.setToBindPose();
        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x - 8f);
        root.setY(getPosPixels().y - 8f);
        root.setScaleX(0.5f);
        root.setScaleY(0.5f);
        skel.updateWorldTransform();

        neck = skel.findBone("neck");

		cape = new Cape(new Vector2(neck.getWorldX()+2, neck.getWorldY()-30), world, 3, 17);
	}
	
	public void update( float dT ) {
		float delta = Gdx.graphics.getDeltaTime();

		totalTime += delta;
		
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
			if( playerState == PlayerState.FLYING )
				playerState = PlayerState.LANDING;
		}
		
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
			if( !onGround ) {
				body.applyLinearImpulse(new Vector2(0.15f, 0.0f),
						body.getWorldCenter());
			} else {
				body.applyLinearImpulse(new Vector2(0.05f, 0.0f),
						body.getWorldCenter());				
			}
			if( facingLeft ) 
				cape.flipPinSprite();
			facingLeft = false;
			skel.setFlipX(facingLeft);
			if ( onGround && !doJump && playerState != PlayerState.WALKING )
			{
				totalTime = 0;
		        skel.setToBindPose(); 
		        Bone root = skel.getRootBone();
		        root.setScaleX(0.5f);
		        root.setScaleY(0.5f);
				playerState = PlayerState.WALKING;
				animation = walk;
			}
		} else if (moveLeft) {
			if( !onGround ) {
				body.applyLinearImpulse(new Vector2(-0.15f, 0.0f),
						body.getWorldCenter());
			} else {
				body.applyLinearImpulse(new Vector2(-0.05f, 0.0f),
						body.getWorldCenter());				
			}
			if( !facingLeft ) 
				cape.flipPinSprite();
			facingLeft = true;
			skel.setFlipX(facingLeft);
			if( onGround && !doJump && playerState != PlayerState.WALKING )
			{
				totalTime = 0;
		        skel.setToBindPose(); 
		        Bone root = skel.getRootBone();
		        root.setScaleX(0.5f);
		        root.setScaleY(0.5f);
				playerState = PlayerState.WALKING;
				animation = walk;
			}
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
		if (doJump ) {//&& (Math.abs(body.getLinearVelocity().y) < 1e-9 || onGround)) {
			body.setGravityScale(0.5f);
			body.applyLinearImpulse(new Vector2(0.0f,0.2f),
					body.getWorldCenter());
			if (onGround && playerState != PlayerState.JUMPING) {
		        skel.setToBindPose(); 
		        Bone root = skel.getRootBone();
		        root.setScaleX(0.5f);
		        root.setScaleY(0.5f);
				playerState = PlayerState.JUMPING;
				animation = jump;
				totalTime = 0;
			} 
		} else {
			//body.setGravityScale(1f);
			doJump = false;
			inAir = false;
		}


        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x - 8f);
        root.setY(getPosPixels().y - 32f);
		if ( playerState != PlayerState.FLYING ) {
			if ( totalTime > animation.getDuration() && !moveLeft && !moveRight) {
				totalTime = 0;
				playerState = PlayerState.IDLE;
				animation = idle;
		        skel.setToBindPose(); 
		        root.setX(getPosPixels().x - 8f);
		        root.setY(getPosPixels().y - 32f);
		        root.setScaleX(0.5f);
		        root.setScaleY(0.5f);
				cape.setGravityScale(1f);
			} else if( onGround || playerState != PlayerState.JUMPING || totalTime < 0.95f ) {
				animation.apply(skel, totalTime, true);
			} else {
				playerState = PlayerState.FLYING;
//				if(!cape.isCapeRotated()) 
//					cape.rotateCape(neck.getRotation()*Util.RAD_TO_DEG);
			}
		}
        skel.updateWorldTransform();
        skel.update(delta);
        if ( facingLeft ) {
        	if ( !onGround ) {
            	cape.movePinTowardsPos(neck.getWorldX()+6, neck.getWorldY()-17);
            } else {
            	cape.movePinTowardsPos(neck.getWorldX()+2, neck.getWorldY()-30);
            }
		} else {
			if ( !onGround ) {
				cape.movePinTowardsPos(neck.getWorldX()-16, neck.getWorldY()-17);
			} else {
				cape.movePinTowardsPos(neck.getWorldX()-15, neck.getWorldY()-30);
			}
        }
	}
	
	public void draw( SpriteBatch batch ) {
        skel.draw(batch);
        cape.draw(batch);
	}
	
	public void drawCape( ShapeRenderer renderer ) {
		cape.drawShape(renderer);
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
}
