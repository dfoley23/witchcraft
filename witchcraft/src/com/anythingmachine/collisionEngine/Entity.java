package com.anythingmachine.collisionEngine;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
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
	

}
