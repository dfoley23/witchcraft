package com.anythingmachine.collisionEngine;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

public class Entity extends Object {
	public EntityType type;

	public Entity() {
		type = EntityType.ENTITY;
	}
	
	public Entity setType(EntityType type) {
		this.type = type;
		return this;
	}
	
	public void handleContact(Contact contact, boolean isFixture1){
			
	}
	
	public void endContact(Contact contact, boolean isFixture1) {
		
	}
	
	/**
	 * particle functions
	 */
	public void stop() {
	}

	public void setX(float x) {
	}

	public void stopOnX() {
	}

	public void stopOnY() {
	}

	public void setY(float y) {
	}

	public void setPos(float x, float y, float z) {
	}

	public void setPos(Vector2 target) {
	}

	public void addPos(float x, float y) {
	}

	public void setGravityVal(float val) {
	}

	public void setVel(float x, float y, float z) {
	}

	public void setYVel(float y) {
	}

	public void setXVel(float x) {
	}

	public void addVel(float x, float y, float z) {
	}
	public Vector3 getPos() {
		return Vector3.Zero;
	}
	public void start() {
	}
	
	/**
	 * agent functions
	 */

	public void setParentState() {
		
	}
	public void faceLeft(boolean val) {
		
	}
	
	public void setStateByValue(String strvalue) {
	}
	
	public void setParentByValue(String strvalue) {
		
	}
	
	public void setAnimation(String anim, boolean val) {
	}

	public void switchBloodSword() {
	}

	public void setTalking(NonPlayer npc) {
	}


}
