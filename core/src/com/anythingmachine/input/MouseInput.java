package com.anythingmachine.input;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Pointer;
import com.anythingmachine.Util.Util;
import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.agents.States.Player.PlayerStateEnum;
import com.anythingmachine.agents.npcs.NonPlayer;
import com.anythingmachine.agents.player.items.InventoryObject;
import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.ground.Door;
import com.anythingmachine.collisionEngine.ground.LevelWall;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.StairTrigger;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class MouseInput extends Entity {
	private Body mouseBody;
	
	public MouseInput() {
		type = EntityType.MOUSEINPUT;
		buildBody();
	}
		
	public void updateTarget(float x, float y) {
		mouseBody.setTransform(new Vector2(x*Util.PIXEL_TO_BOX,y*Util.PIXEL_TO_BOX), 0);
	}
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		float sign;
		switch (other.type) {
		case NONPLAYER:
			other.handleMouseContact();
			break;
		case WALL:
			break;
		case PLATFORM:
			break;
		case STAIRUPTRIGGER:
			break;
		case STAIRDOWNTRIGGER:
			break;
		case STAIRS:
			break;
		case DOOR:
			break;
		case LEVELWALL:
			break;
		case AINODE:
			break;
		case ACTIONWALL:
			break;
		case INVENTORYOBJECT:
			break;
		case SWORD:
			break;
		case ARROW:
			break;
		}
	}

	public void endContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		switch (other.type) {
		case NONPLAYER:
			other.endMouseContact();
			break;
		case WALL:
			break;
		case PLATFORM:
			break;
		case STAIRUPTRIGGER:
			break;
		case STAIRS:
			break;
		case DOOR:
			break;
		case LEVELWALL:
			break;
		case AINODE:
			break;
		case ACTIONWALL:
			break;
		case INVENTORYOBJECT:
			break;
		case SWORD:
			break;
		case ARROW:
			break;
		}
	}

    
	private void buildBody() {
		//this body is joint A and is ignored
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		def.position.set(new Vector2(0, 0));
		mouseBody = GamePlayManager.world.createBody(def);

		//this body is jointed to the above body and is physically pulled towards the mouse
		BodyDef def2 = new BodyDef();
		def2.type = BodyType.DynamicBody;
		def2.position.set(new Vector2(0, 0));
		Body collisionBody = GamePlayManager.world.createBody(def2);
		CircleShape shape = new CircleShape();
		shape.setRadius(8 * Util.PIXEL_TO_BOX);
		FixtureDef fixDef2 = new FixtureDef();
		fixDef2.shape = shape;
		fixDef2.isSensor = true;
		fixDef2.density = 1f;
		fixDef2.filter.categoryBits = Util.CATEGORY_EVERYTHING;
		fixDef2.filter.maskBits = Util.CATEGORY_EVERYTHING;
		collisionBody.createFixture(fixDef2);
		collisionBody.setUserData(this);
		shape.dispose();
		
		WeldJointDef jDef = new WeldJointDef();
		jDef.bodyA = mouseBody;
		jDef.bodyB = collisionBody;
		jDef.collideConnected = false;
		jDef.type = JointType.WeldJoint;
		jDef.referenceAngle = 0;
		jDef.dampingRatio = 0;
		GamePlayManager.world.createJoint(jDef);
	}
	
}
