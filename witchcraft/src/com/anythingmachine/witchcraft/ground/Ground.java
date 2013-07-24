package com.anythingmachine.witchcraft.ground;

import java.util.ArrayList;

import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
	ArrayList<Curve> curves;
	World world;
	
	public enum GroundType {
		VERYGREEN, DEADGRASS, GRASS, DESERT, MUD, OLDBRICK, NICEBRICK 
	}
	
	public Ground(World world){
		this.world = world;
		curves = new ArrayList<Curve>();
	}
	
	public void draw( PolygonSpriteBatchWrap batch ) {
		for ( Curve c: curves ) {
			c.draw(batch);
		}
	}
	
	public Vector2 findPointOnCurve( int index, float dX ) {
		return curves.get(index).findPointOnHCurve(dX);
	}
	
	public Curve getCurve( int index ) {
		return curves.get(index);
	}
	public void addCurve( Curve curve ) {
		curves.add( curve );
	}
	
	public void createCurve( Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, int res, int zLayer, GroundType type) {
		curves.add(new Curve(p0, p1, p2, p3, res, zLayer, type, world));
	}
}
