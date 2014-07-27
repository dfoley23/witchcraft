package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;

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
