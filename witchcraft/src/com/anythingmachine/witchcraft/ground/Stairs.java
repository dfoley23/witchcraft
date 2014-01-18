package com.anythingmachine.witchcraft.ground;

import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.math.Vector2;

public class Stairs extends Platform {
	private Vector2 end;
	
	public Stairs( float x, float y, int w, int h) {
		super(x, y, w, h);
		type = EntityType.STAIRS;
	}

	public Stairs( Vector2 start, Vector2 end) {
		super(start, end);
		this.end = end.cpy();
		type = EntityType.STAIRS;
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
	
	@Override
	public boolean isBetween(boolean facingLeft, float x) {
		if ( facingLeft ) 
			return x > posx-4 && x < posx+width+34;
		else
			return x > posx-68 && x < posx+width+4;			
	}
}
