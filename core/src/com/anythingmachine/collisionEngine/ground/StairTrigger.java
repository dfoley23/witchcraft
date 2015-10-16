package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class StairTrigger extends Entity {
	private Stairs parentStairs;
	private Body collisionBody;
	
	public StairTrigger(Stairs parent, EntityType triggerType, Vector2 pos){
		this.parentStairs = parent;
		this.type = triggerType;
		buildBody(pos);
	}

	public Stairs getStairs(){
		return parentStairs;
	}
	
	private void buildBody(Vector2 pos){
		BodyDef startDef = new BodyDef();
		startDef.type = BodyType.StaticBody;
		startDef.position.set(new Vector2(pos.x*Util.PIXEL_TO_BOX, pos.y*Util.PIXEL_TO_BOX));
		collisionBody = GamePlayManager.world.createBody(startDef);
		CircleShape shape = new CircleShape();
		shape.setRadius(8 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_STAIRS;
		fixture.filter.maskBits = Util.CATEGORY_FEET;
		Fixture startStairTrigger = collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();

	}
}
