package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.math.Vector2;

public class Door extends Entity {
	public Integer to_level;
	public Vector2 exitPos;
	
	public Door(String levelStr, String x, String y) {
		type = EntityType.DOOR;
		this.to_level = Integer.parseInt(levelStr);
		this.exitPos = new Vector2(Float.parseFloat(x), Float.parseFloat(y));
	}
	
	public Integer getToLevel(){
		return to_level;
	}
	
	
	public Vector2 getDoorExitPos(){
		return exitPos;
	}
}
