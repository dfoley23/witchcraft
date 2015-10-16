package com.anythingmachine.cinematics;

import java.util.ArrayList;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class CinematicTrigger extends Entity {
	private ArrayList<CinematicAction> actions;
	private Body collisionBody;
	private boolean destroybody = false;
	private boolean hasEnded = false;

	public CinematicTrigger() {
		actions = new ArrayList<CinematicAction>();
	}
	
	public CinematicTrigger addAction(CinematicAction action) {
		actions.add(action);
		return this;
	}
	
	public void update(float dt) {
		if ( destroybody) 
			collisionBody.getWorld().destroyBody(collisionBody);
		destroybody = false;
		hasEnded = true;
		for(CinematicAction a: actions) {
			if( !a.isEnded() ) {
				if( a.isStarted(dt) ) {
					a.update(dt);
					System.out.println(a.toString()+" has begun");
				} else {
				System.out.println(a.toString()+" has not begun");
				}
				hasEnded = false;
			} else {
				System.out.println(a.toString()+" has ended");				
			}
		}
		if ( hasEnded ) {
			actions.clear();
			GamePlayManager.sleepCinematic();
		}
	}
	
	public boolean isEnded() {
		return hasEnded;
	}

	@Override
	public void destroyBody() {
		GamePlayManager.world.destroyBody(this.collisionBody);
	}
	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		destroybody = true;
		GamePlayManager.setCinematic(this);
	}

	public CinematicTrigger buildBody(float x, float y, float width, float height) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(new Vector2(x * Util.PIXEL_TO_BOX, y * Util.PIXEL_TO_BOX));
		collisionBody = GamePlayManager.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width * Util.PIXEL_TO_BOX, height * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_TRIGGERS;
		fixture.filter.maskBits = Util.CATEGORY_PLAYER;
		collisionBody.createFixture(fixture);
		collisionBody.setUserData(this);
		shape.dispose();

		return this;
	}
	
	public void transistionOut() {
		
	}

}
