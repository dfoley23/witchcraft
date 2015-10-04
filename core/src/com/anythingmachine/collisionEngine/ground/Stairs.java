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
		return slantRight ? posy : end.y;
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
		return end.y+((1-percent)*height);
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
	public Vector2 getStartPos(){
		return new Vector2(posx, posy);
	}

	@Override
	public Vector2 getEndPos(){
		return end;
	}
	
	@Override
	public float getUpPosX() {
		if ( slantRight ) {
			return end.x;
		} 
		return posx;
	}

	@Override
	public float getDownPosX() {
		if ( slantRight ) {
			return posx;
		} 
		return end.x;
	}

	@Override
	public float getUpPosY() {
		if ( slantRight ) {
			return end.y;
		} 
		return posy;
	}

	@Override
	public float getDownPosY() {
		if ( slantRight ) {
			return posy;
		} 
		return end.y;	
	}

	@Override
	public boolean isBetween(boolean facingLeft, float x) {
		if ( facingLeft ) 
			return x > posx-12 && x < posx+width+12;
		else
			return x > posx-12 && x < posx+width+12;
	}
}
