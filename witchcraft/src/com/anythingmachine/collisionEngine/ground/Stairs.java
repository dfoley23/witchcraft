package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;

public class Stairs extends Platform {
	private Vector2 end;
	private boolean walkDown = true;
	
	public Stairs( String name, float x, float y, int w, int h) {
		super(name, x, y, w, h);
		type = EntityType.STAIRS;
		slantRight = end.y > posy;
	}

	public Stairs( String name, Vector2 start, Vector2 end) {
		super(name, start, end);
		this.end = end.cpy();
		slantRight = end.y > posy;
		type = EntityType.STAIRS;
	}
			
	@Override
	public float getHeight() {
		return end.y > posy ? posy : end.y;
	}

	@Override
	public void holdDownToWalkDown() {
		walkDown = false;
	}
	
	@Override
	public boolean walkDown() {
		return walkDown;
	}
	
	@Override	
	public boolean isStairs() {
		return true;
	}

	@Override
	public float getHeight(float x) {
		float percent = (x-posx)/(width);
		//System.out.println(percent);
		if ( slantRight() ) {
		    percent = percent>1f ? 1: percent;
		    return posy+((percent)*height);
		} 
		float newY = posy-((percent)*height);
		return newY > posy ? posy : newY;
	}

	@Override
	public float getXPos(float y) {
		float percent;
		if ( slantRight() ) {
			percent = (y-posy)/(height);
			return posx+((percent)*width);			
		} 
		percent = (y-end.y)/(height);
		return posx+((1-percent)*width);			
		//System.out.println(percent);
	}
	
	@Override
	public float getUpPosX() {
		if ( slantRight() ) {
			return end.x-12;
		} 
		return posx+12;
	}

	@Override
	public float getDownPosX() {
		if ( slantRight() ) {
			return posx+16;
		} 
		return end.x-12;
	}

	@Override
	public float getUpPosY() {
		if ( slantRight() ) {
			return end.y;
		} 
		return posy;
	}

	@Override
	public float getDownPosY() {
		if ( slantRight() ) {
			return posy;
		} 
		return end.y;	
	}

	@Override
	public boolean isBetween(boolean facingLeft, float x) {
		if ( facingLeft ) 
			return x > posx-12 && x < posx+width+12;
		else
			return x > posx-4 && x < posx+width+3;			
	}
}
