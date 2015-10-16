package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.collisionEngine.Entity;

public class ActionWall extends Entity {
	private int level;
	
	public ActionWall ( int level ) {
		this.level = level;
		this.type = EntityType.ACTIONWALL;
	}
	
	public int getLevel(){
		return level;
	}
}
