package com.anythingmachine.collisionEngine.ground;

import java.util.ArrayList;

import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
	private ArrayList<Curve> curves;
	private ArrayList<Platform> elevatedSegments;
	private int numCurves = 0;
	
	public enum GroundType {
		VERYGREEN, DEADGRASS, GRASS, DESERT, MUD, OLDBRICK, NICEBRICK 
	}

	public enum GroundElemType { 
		ROCK, GRASS, BRICK
	}
	
	public Ground(World world){
		curves = new ArrayList<Curve>();
		elevatedSegments = new ArrayList<Platform>();
	}
	
	public void draw( PolygonSpriteBatchWrap batch, int startCurve, int numCurves ) {
		startCurve = Math.max( startCurve, 0);		
		int endCurve = startCurve + numCurves+2;
		endCurve = Math.min(endCurve, curves.size());
		for ( int i=startCurve; i< endCurve; i++ ) {
			curves.get(i).draw(batch);
		}
	}
	
	public ArrayList<Vector2> getCurveBeginPoints() {
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		for(Curve c: curves) {
			points.add(c.firstPointOnCurve());
		}
		return points;
	}
	
	public void drawGroundElems( SpriteBatch batch, int startCurve, int numCurves ) {
		startCurve = Math.max( startCurve, 0);		
		int endCurve = startCurve + numCurves+1;
		endCurve = Math.min(endCurve, curves.size());
		for ( int i=startCurve; i< endCurve; i++ ) {
			curves.get(i).drawGroundElems(batch);
		}
	}
	
	public void drawDebugCurve(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Line);
		for( Curve c: curves ) {
			renderer.line(c.firstPointOnCurve().x, c.firstPointOnCurve().y,
					c.firstPointOnCurve().x, c.firstPointOnCurve().y+150);
		}
		renderer.end();
	}
	
	public Vector2 findPointOnCurve( int index, float dX ) {
		return curves.get(index).findPointOnHCurve(dX);
	}
	
	public float getElevatedSegHeight( int index ) {
		return elevatedSegments.get(index).getHeight();
	}

	public Platform getElevatedSegment(int index) {
		return elevatedSegments.get(index);
	}
	
	public int getNumCurves() {
		return numCurves;
	}
	
	public Curve getCurve( int index ) {
		if ( index >= curves.size() ) {
			return curves.get(curves.size()-1);
		} else if ( index < 0 ) {
			return curves.get(0);
		}
		return curves.get(index);
	}
	
	public void addCurve( Curve curve ) {
		curves.add( curve );
		numCurves = curves.size()-1;
	}
	
	public void createCurve( Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, int res, int zLayer, GroundType type) {
		curves.add(new Curve(p0, p1, p2, p3, res, zLayer, type));
		numCurves = curves.size()-1;
	}
	
	public void readCurveFile(String filename, float dx, float dy) {
		FileHandle handle = Gdx.files.internal(filename);
		String file = handle.readString();
		String[] lines = file.split("\n");
		int size = lines.length;
		System.out.println(size);
		for ( int i=0; i<size; i+=4) {
			String[] points = lines[i].split(",");
			Vector2 p0 = new Vector2(Float.parseFloat(points[0])+dx, Float.parseFloat(points[1])+dy);
			points = lines[i+1].split(",");
			Vector2 p1 = new Vector2(Float.parseFloat(points[0])+dx, Float.parseFloat(points[1])+dy);
			points = lines[i+2].split(",");
			Vector2 p2 = new Vector2(Float.parseFloat(points[0])+dx, Float.parseFloat(points[1])+dy);
			points = lines[i+3].split(",");
			Vector2 p3 = new Vector2(Float.parseFloat(points[0])+dx, Float.parseFloat(points[1])+dy);
			this.createCurve(p0, p1, p2, p3, 10, -1, GroundType.DESERT);
		}	
	}
}
