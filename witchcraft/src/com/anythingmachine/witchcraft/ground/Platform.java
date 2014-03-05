package com.anythingmachine.witchcraft.ground;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;

public class Platform extends Entity {
	protected float posx;
	protected float posy;
	protected float width;
	protected float height;
	protected String name;
	
	public Platform( String name, float x, float y, int w, int h) {
		this.name = name;
		this.posx = x;
		this.posy = y;
		this.width = w;
		this.height = h;
		type = EntityType.PLATFORM;
	}

	public Platform( String name, Vector2 start, Vector2 end) {
		this.name = name;		
		this.posx = start.x;
		this.posy = end.y<start.y ? end.y : start.y;
		this.width = end.x-start.x;
		this.height = Math.abs(end.y-start.y);
		type = EntityType.PLATFORM;
	}
	
	public float getHeight(float x) {
		return posy+height;
	}
	
	public float getHeight() {
		return posy+height;
	}
	
	public Vector2 getPos() {
		return new Vector2(posx, posy);
	}
	
	public String getName() {
		return name;
	}

	public float getHeightLocal() {
		return height;
	}
	
	public float getWidth() {
		return posx+width+34;
	}
	
	public boolean isBetween(boolean facingLeft, float x) {
		return x > posx && x < posx+width+3;			
	}

}
