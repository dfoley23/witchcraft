package com.anythingmachine.witchcraft;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.physics.box2d.Contact;

public class Entity extends Object {
	public EntityType type;

	public Entity() {
		type = EntityType.ENTITY;
	}
	
	public void handleContact(Contact contact, boolean isFixture1){
			
	}
	
	public void endContact(Contact contact, boolean isFixture1) {
		
	}
	

}
