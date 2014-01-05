package com.anythingmachine.witchcraft.ground;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;

public class Platform extends Entity {
	private float posx;
	private float posy;
	private float width;
	private float height;
	
	public Platform( float x, float y, int w, int h) {
		this.posx = x;
		this.posy = y;
		this.width = w;
		this.height = h;
		type = EntityType.PLATFORM;
	}

	public Platform( Vector2 start, Vector2 end) {
		this.posx = start.x;
		this.posy = start.y;
		this.width = end.x-start.x;
		this.height = end.y-start.y;
		type = EntityType.PLATFORM;
	}
	
	public float getHeight() {
		return posy+height;
	}
	
	public boolean isBetween(float x) {
		return x > posx && x < posx+width;
	}
}
