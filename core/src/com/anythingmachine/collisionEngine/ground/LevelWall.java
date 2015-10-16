package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.collisionEngine.Entity;

public class LevelWall extends Entity {
	private int level;
	
	public LevelWall ( int level ) {
		this.level = level;
		this.type = EntityType.LEVELWALL;
	}
	
	public int getLevel(){
		return level;
	}
}
