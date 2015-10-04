package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;

public class Door extends Entity {
	public Integer to_level;
	
	public Door(String levelStr) {
		type = EntityType.DOOR;
		this.to_level = Integer.parseInt(levelStr);
	}
	
	public Integer getToLevel(){
		return to_level;
	}
}
