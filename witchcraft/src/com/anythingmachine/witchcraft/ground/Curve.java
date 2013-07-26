package com.anythingmachine.witchcraft.ground;

import java.util.ArrayList;

import com.anythingmachine.gdxwrapper.PolygonRegionWrap;
import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.gdxwrapper.PolygonSpriteWrap;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Vector4;
import com.anythingmachine.witchcraft.ground.Ground.GroundType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Curve {
	private Vector2 p0;
	private Vector2 p1;
	private Vector2 p2;
	private Vector2 p3;
	private int zLayer;
	private GroundType type;
	private PolygonSpriteWrap sprite;
	private ArrayList<PolygonSpriteWrap> sprites;
	//private Body body;
	
	public Curve( Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, 
			int res, int zLayer, GroundType type, World world) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.type = type;
		this.zLayer = zLayer;
		generateShape(res, world);
	}
	
	public Vector2 firstPointOnCurve() {
		return p1;
	}
	
	public Vector2 lastPointOnCurve() {
		return p2;
	}
	
	public void draw( PolygonSpriteBatchWrap batch) {
		sprite.draw(batch);
	}
	
	/**
	 * finds the point on a curve in world space 
	 * according to a point dX in world space
	 * @param dX
	 * @return
	 */
	public Vector2 findPointOnHCurve(float dX){	
		Vector4 T = new Vector4();
		Vector4 T0 = new Vector4();
		Vector4 T1 = new Vector4();
		Vector4 T2 = new Vector4();
		Vector4 T3 = new Vector4();
		Vector2 point = new Vector2();
		Vector4 TintM = new Vector4();

		float s = (dX - p1.x) / (p2.x - p1.x);

		T.x = s*s*s; T.y = s*s; T.z = s; T.w = 1.0f;
		TintM.x = 0; TintM.y = 0; TintM.z = 0; TintM.w = 0;
		//the vector T times the cat mull rom matrix
		TintM.x += T.x * Util.catM[0]; TintM.x += T.y * Util.catM[1];
		TintM.x += T.z * Util.catM[2]; TintM.x += T.w * Util.catM[3];
		TintM.y += T.x * Util.catM[4]; TintM.y += T.y * Util.catM[5];
		TintM.y += T.z * Util.catM[6]; TintM.y += T.w * Util.catM[7];
		TintM.z += T.x * Util.catM[8]; TintM.z += T.y * Util.catM[9];
		TintM.z += T.z * Util.catM[10]; TintM.z += T.w * Util.catM[11];
		TintM.w += T.x * Util.catM[12]; TintM.w += T.y * Util.catM[13];
		TintM.w += T.z * Util.catM[14]; TintM.w += T.w * Util.catM[15];

		T0.x = TintM.x * p0.x; T0.y = TintM.x * p0.y; T0.z = TintM.x * -1f;
		T1.x = TintM.y * p1.x; T1.y = TintM.y * p1.y; T1.z = TintM.y * -1f;
		T2.x = TintM.z * p2.x; T2.y = TintM.z * p2.y; T2.z = TintM.z * -1f;
		T3.x = TintM.w * p3.x; T3.y = TintM.w * p3.y; T3.z = TintM.w * -1f;

		point.x = T0.x + T1.x + T2.x + T3.x;
		point.y = T0.y + T1.y + T2.y + T3.y;
		//point.z = T0.z + T1.z + T2.z + T3.z;

		return point;
	}

	/**
	 * builds a polygon with the top segment (a curve of the ground)
	 * and the bottom segment closing the polygon 
	 * @param res
	 */
	private void generateShape( int res, World world ) {
		Vector4 T = new Vector4();
		Vector4 T0 = new Vector4();
		Vector4 T1 = new Vector4();
		Vector4 T2 = new Vector4();
		Vector4 T3 = new Vector4();
		Vector4 TintM = new Vector4();
		Vector2 point = new Vector2();
		float[] polyPoints = new float[((res)*2)+6];
//		Vector2[] bodyPoints = new Vector2[res+3];
		int index = 0;
		
		for(int r=0; r<=res; ++r){
			float t = (float)r / (float)res;
			T.x = t*t*t; T.y = t*t; T.z = t; T.w = 1.0f;
			TintM.x = 0; TintM.y = 0; TintM.z = 0; TintM.w = 0;
			//the vector T times the cat mull rom matrix
			TintM.x += T.x * Util.catM[0]; TintM.x += T.y * Util.catM[1];
			TintM.x += T.z * Util.catM[2]; TintM.x += T.w * Util.catM[3];
			TintM.y += T.x * Util.catM[4]; TintM.y += T.y * Util.catM[5];
			TintM.y += T.z * Util.catM[6]; TintM.y += T.w * Util.catM[7];
			TintM.z += T.x * Util.catM[8]; TintM.z += T.y * Util.catM[9];
			TintM.z += T.z * Util.catM[10]; TintM.z += T.w * Util.catM[11];
			TintM.w += T.x * Util.catM[12]; TintM.w += T.y * Util.catM[13];
			TintM.w += T.z * Util.catM[14]; TintM.w += T.w * Util.catM[15];

			T0.x = TintM.x * p0.x; T0.y = TintM.x * p0.y; T0.z = TintM.x * zLayer;
			T1.x = TintM.y * p1.x; T1.y = TintM.y * p1.y; T1.z = TintM.y * zLayer;
			T2.x = TintM.z * p2.x; T2.y = TintM.z * p2.y; T2.z = TintM.z * zLayer;
			T3.x = TintM.w * p3.x; T3.y = TintM.w * p3.y; T3.z = TintM.w * zLayer;

			point.x = T0.x + T1.x + T2.x + T3.x;
			point.y = T0.y + T1.y + T2.y + T3.y;
			//point.z = T0.z + T1.z + T2.z + T3.z;

			//save the points for the polygon sprite
			polyPoints[index] = point.x;
			polyPoints[index+1] = point.y;
			index += 2;
			
			//save the points for the box2d body
//			bodyPoints[r] = new Vector2( point.x, point.y );
		}
		
		////add the final points that completes the base of the polygon
		polyPoints[index] = p2.x;
		polyPoints[index+1] = Util.offScreenGround;
		polyPoints[index+2] = p1.x;
		polyPoints[index+3] = Util.offScreenGround;
//		bodyPoints[res+1] = new Vector2( p2.x, Util.offScreenGround );
//		bodyPoints[res+2] = new Vector2( p1.x, Util.offScreenGround );
		
		//create the polygon sprite
		Texture desertTexture = new Texture(Gdx.files.internal("data/desertTexture.png")); 
		desertTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		PolygonRegionWrap polyReg = new PolygonRegionWrap(new TextureRegion(desertTexture), polyPoints );
		sprite = new PolygonSpriteWrap(polyReg);
		sprite.setOrigin(p1.x, p1.y);
		

		//create the body
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.position.set(0, 0);
//		// Tell the physics world to create the body
//		body = world.createBody(bodyDef);
//		PolygonShape shape = new PolygonShape();
//		//Make a list of points for the ground.
//		shape.set(bodyPoints);
//		body.createFixture(shape, 1.f);
	}
}
