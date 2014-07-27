package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;

public class Stairs extends Platform {
	private Vector2 end;
	
	public Stairs( String name, float x, float y, int w, int h) {
		super(name, x, y, w, h);
		type = EntityType.STAIRS;
	}

	public Stairs( String name, Vector2 start, Vector2 end) {
		super(name, start, end);
		this.end = end.cpy();
		type = EntityType.STAIRS;
	}
	
	public boolean slantRight() {
		return end.y > posy;
	}
	
	@Override
	public float getHeight(float x) {
		float percent = (x-posx)/(end.x-posx);
		percent = end.y==posy ? 1-percent: percent;
		//System.out.println(percent);
		return posy+((percent)*height);
	}
	
	@Override
	public float getHeight() {
		return posy;
	}
	
	public float getUpPos() {
		if ( slantRight() ) {
			return posx;
		} 
		return end.x;
	}
	
	public float getDownPos() {
		if ( slantRight() ) {
			return end.x;
		} 
		return posx;	
	}
	@Override
	public boolean isBetween(boolean facingLeft, float x) {
		if ( facingLeft ) 
			return x > posx-6 && x < posx+width+45;
		else
			return x > posx-68 && x < posx+width+12;			
	}
}
