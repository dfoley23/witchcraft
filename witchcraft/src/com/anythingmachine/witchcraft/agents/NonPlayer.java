package com.anythingmachine.witchcraft.agents;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.State;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.aiengine.UtilityAI.AIState;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.Gdx;
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
	private State playerState;
	private UtilityAI behavior;
	private float aiChoiceTime = 0.f;
	private AIState state = AIState.IDLE;
	
	public NonPlayer( String name, Vector2 pos, World world, Ground ground ) {
		createBody( world, pos );
		playerState = State.IDLE;
		this.ground = ground;
		this.curGroundSegment = 0;
		ArrayList<Vector2> points = ground.getCurveBeginPoints();
		for(int i=0; i< points.size(); i++) {
			if( pos.x > points.get(i).x ) {
				this.curGroundSegment = i;
			}
		}
			
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

       	skel.setSkin(name);
        skel.setToBindPose();
        Bone root = skel.getRootBone();
        root.setX(getPosPixels().x);
        root.setY(getPosPixels().y - 64f);
        root.setScaleX(0.6f);
        root.setScaleY(0.7f);
        skel.updateWorldTransform();
        skel.setFlipX(true);
        
        Random rand = new Random();
        //ai goals and actions
        behavior = new UtilityAI();
        behavior.addGoal(new Goal("Eat", rand.nextInt(5)+1));
        behavior.addGoal(new Goal("Sleep", rand.nextInt(5)+1));
        behavior.addGoal(new Goal("Hunt", rand.nextInt(20)+7));
        Action action = new Action("eatBread", AIState.IDLE);
        action.addAction(-5f, "Eat" );
        action.addAction(1f, "Sleep" );
        action.addAction(3f, "Hunt" );
        behavior.addAction(action);
        action = new Action("eatMeat",AIState.IDLE);
        action.addAction(-7f, "Eat" );
        action.addAction(2f, "Sleep" );
        action.addAction(3f, "Hunt" );
        behavior.addAction(action);
        action = new Action("sleepInHouse",AIState.IDLE);
        action.addAction(3f, "Eat" );
        action.addAction(-7f, "Sleep" );
        action.addAction(5f, "Hunt" );
        behavior.addAction(action);
        action = new Action("sleepInBed",AIState.IDLE);
        action.addAction(3f, "Eat" );
        action.addAction(-6f, "Sleep" );
        action.addAction(5f, "Hunt" );
        behavior.addAction(action);
        action = new Action("standIdle",AIState.IDLE);
        action.addAction(2f, "Eat" );
        action.addAction(1f, "Sleep" );
        action.addAction(-5f, "Hunt" );
        behavior.addAction(action);
        action = new Action("walkLeft",AIState.WALKINGLEFT);
        action.addAction(2f, "Eat" );
        action.addAction(1f, "Sleep" );
        action.addAction(-8f, "Hunt" );
        behavior.addAction(action);
        action = new Action("walkRight",AIState.WALKINGRIGHT);
        action.addAction(2f, "Eat" );
        action.addAction(1f, "Sleep" );
        action.addAction(-4f, "Hunt" );
        behavior.addAction(action);
	}
	
	public void update( float dT ) {
		float delta = Gdx.graphics.getDeltaTime();


        Bone root = skel.getRootBone();
        
		aiChoiceTime += dT;
		if( aiChoiceTime > 7 ) {
			AIState old = state;
			state = behavior.ChooseAction();
			aiChoiceTime = 0;
			switch(state) {
			case WALKINGLEFT:
				animation = walk;
				facingLeft = true;
				if( old != AIState.WALKINGLEFT) {
					totalTime = 0;
					skel.setToBindPose(); 
					root.setX(getPosPixels().x);
					root.setY(getPosPixels().y - 64f);
					root.setScaleX(0.6f);
					root.setScaleY(0.7f);
				}
				break;
			case WALKINGRIGHT:
				animation = walk;
				facingLeft = false;
				if( old != AIState.WALKINGRIGHT) {
					totalTime = 0;
			        skel.setToBindPose(); 
			        root.setX(getPosPixels().x);
			        root.setY(getPosPixels().y - 64f);
			        root.setScaleX(0.6f);
			        root.setScaleY(0.7f);
					}
				break;
			case IDLE:
				animation = idle;
				if( old != AIState.IDLE) {
					totalTime = 0;
			        skel.setToBindPose(); 
			        root.setX(getPosPixels().x);
			        root.setY(getPosPixels().y - 64f);
			        root.setScaleX(0.6f);
			        root.setScaleY(0.7f);
					}
				break;
			default:
				break;
			}
		}
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

        root.setX(getPosPixels().x);
        root.setY(getPosPixels().y - 64f);
		//if ( animation != null ) {
//			if ( totalTime > animation.getDuration() ) {//&& !inAir && !moveLeft && !moveRight) {
//				totalTime = 0;
//		        skel.setToBindPose(); 
//		        root.setX(getPosPixels().x);
//		        root.setY(getPosPixels().y - 64f);
//		        root.setScaleX(0.6f);
//		        root.setScaleY(0.7f);
//			} else 
			{ 
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
	
	private void createBody( World world, Vector2 pos ) {

		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(pos.x, pos.y);

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
