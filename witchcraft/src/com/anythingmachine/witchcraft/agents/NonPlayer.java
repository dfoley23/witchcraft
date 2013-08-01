package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.player.Player.PlayerState;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
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

public class NonPlayer extends Agent {
	private Body body;
	private boolean facingLeft;
	private boolean inAir;
	private Skeleton skel;
	private Animation walk;
	private Animation idle;
	private Animation animation;
	private float totalTime = 0f;
	private PlayerState playerState;
	
	public NonPlayer( World world, Ground ground ) {
		createBody( world );
		playerState = PlayerState.IDLE;
		this.ground = ground;
		this.curGroundSegment = 7;
		curCurve = ground.getCurve(curGroundSegment);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/spine/knight.atlas"));
        SkeletonBinary sb = new SkeletonBinary(atlas);
        SkeletonData sd = sb.readSkeletonData(Gdx.files
                .internal("data/spine/knight.skel"));

        walk = sb.readAnimation(
                Gdx.files.internal("data/spine/knight-walk.anim"), sd);

        idle = sb.readAnimation(
                Gdx.files.internal("data/spine/knight-idle.anim"), sd);
        animation = idle;
        skel = new com.esotericsoftware.spine.Skeleton(sd);

        skel.setToBindPose();
        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x - 8f);
        root.setY(getPosPixels().y - 8f);
        root.setScaleX(0.6f);
        root.setScaleY(0.7f);
        skel.updateWorldTransform();
        skel.setFlipX(true);
	}
	
	public void update( float dT ) {
		float delta = Gdx.graphics.getDeltaTime();
		
		totalTime += delta;
		
				
		Vector2 pos = body.getPosition().mul(Util.PIXELS_PER_METER).sub(0.0f, 50f);
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


        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x - 8f);
        root.setY(getPosPixels().y - 32f);
		//if ( animation != null ) {
			if ( totalTime > animation.getDuration() ) {//&& !inAir && !moveLeft && !moveRight) {
				totalTime = 0;
				playerState = PlayerState.IDLE;
				animation = idle;
		        skel.setToBindPose(); 
		        root.setX(getPosPixels().x - 8f);
		        root.setY(getPosPixels().y - 32f);
		        root.setScaleX(0.6f);
		        root.setScaleY(0.7f);
			} else { 
				animation.apply(skel, totalTime, true);
			}
		//}
        skel.updateWorldTransform();
        skel.update(delta);

	}
	
	public void draw( SpriteBatch batch ) {
        skel.draw(batch);
	}
	
	public Vector2 getPosMeters() {
		return body.getPosition().sub(0, (64f)*(1.f / Util.PIXELS_PER_METER));
	}
	
	public Vector2 getPosPixels() {
		return body.getPosition().mul(Util.PIXELS_PER_METER);
	}
	public int getCurSegment() {
		return curGroundSegment;
	}
	
	public void correctHeight(float y) {
		//body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
		body.setTransform(body.getPosition().x, (y+50f)*(1.f / Util.PIXELS_PER_METER), 0.0f);
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
		jumperBodyDef.position.set(14.0f, 3.0f);

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
