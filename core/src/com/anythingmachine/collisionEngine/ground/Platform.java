package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Platform extends Entity {
	protected float posx;
	protected float posy;
	protected float width;
	protected float height;
	protected String name;
	protected boolean slantRight = false;;
	
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
		this.posy = start.y;
		this.width = end.x-start.x;
		this.height = Math.abs(end.y-start.y);
		type = EntityType.PLATFORM;
	}
	
	public float getHeight(float x) {
		return posy+height;
	}
	
	public boolean isStairs() {
		return false;
	}
	
	public float getUpPosX() {
		return posy+height;
	}

	public float getDownPosX() {
		return posy+height;
	}

	public float getUpPosY() {
		return posy;
	}

	public float getDownPosY() {
		return posy;	
	}

	public void holdDownToWalkDown() {
	}
	
	public boolean walkDown() {
		return true;
	}

	public boolean slantRight() {
		return slantRight;
	}
	
	public float getXPos(float y) {
		return 0;
	}

	public float getHeight() {
		return posy+height;
	}
	
	public Vector2 getStartPos(){
		return new Vector2(posx, posy);
	}

	public Vector2 getEndPos(){
		return new Vector2(posx+width, posy);
	}

	@Override
	public Vector3 getPos() {
		return new Vector3(posx, posy, 0);
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
		return x > posx-8 && x < posx+width+2;
	}

}
