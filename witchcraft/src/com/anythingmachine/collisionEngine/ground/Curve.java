package com.anythingmachine.collisionEngine.ground;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.collisionEngine.ground.Ground.GroundElemType;
import com.anythingmachine.collisionEngine.ground.Ground.GroundType;
import com.anythingmachine.gdxwrapper.PolygonRegionWrap;
import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.gdxwrapper.PolygonSpriteWrap;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Vector4;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Curve {
	private Vector2 p0;
	private Vector2 p1;
	private Vector2 p2;
	private Vector2 p3;
	private int zLayer;
	private ArrayList<GroundElem> groundElems;
	private PolygonSpriteWrap sprite;
	//private Body body;
	
	public Curve( Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, 
			int res, int zLayer, GroundType type) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.zLayer = zLayer;
		groundElems = new ArrayList<GroundElem>();
		Texture texture = new Texture(Gdx.files.internal("data/desertTexture.png")); 
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		switch ( type ) {
		case DESERT:
			//generateRocks(res);
			//generateGrass(res);
			break;
		case GRASS:
			generateGrass(res);
			break;
		}
		generateShape(res, texture);
	}
	
	public Vector2 firstPointOnCurve() {
		return p1;
	}
	
	public Vector2 lastPointOnCurve() {
		return p2;
	}
	
	public void draw( PolygonSpriteBatchWrap batch ) {
		sprite.draw(batch);
	}
	
	public void drawGroundElems( SpriteBatch batch ) {
		for( GroundElem g: groundElems ) {
			g.draw(batch);
		}
	}
	
	/**
	 * finds the point on a curve in world space 
	 * according to a point dX in world space
	 * @param dX
	 * @return
	 */
	public Vector2 findPointOnHCurve(float dX){	
		if( dX < p1.x )
			return p1;
		else if ( dX > p2.x ) 
			return p2;
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
	private void generateShape( int res, Texture texture ) {
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
		PolygonRegionWrap polyReg = new PolygonRegionWrap(new TextureRegion(texture), polyPoints );
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
	
	private void generateRocks(int res){
		float diff = p2.x - p1.x;
		float inc = diff / (float)res;
		float dX = p1.x;
		Random rand = new Random();
		for( int i=0; i< res; i++ ) {
			Vector2 point = findPointOnHCurve(dX);
			float xNoise = rand.nextInt(10);
			float yNoise = -6;//rand.nextInt(40) * -1;
			Vector2 pos = new Vector2(point.x+xNoise, point.y+yNoise);
			float size = Math.max(rand.nextFloat(), 0.2f) * 0.01f;
			float degree = 0;//(float)rand.nextInt(360);
			Color color = Color.DARK_GRAY;
			switch(rand.nextInt(4)){
			case 0:
				break;
			case 1:
				//color = new Color(104.f/255.f, 56.f/255.f, 15.f/255.f, 1f);
				break;
			case 2:
				//color = new Color(104.f/255.f, 19.f/255.f, 4.f/255.f, 1f);
				break;
			case 3:
				//color = new Color(103.f/255.f, 15.f/255.f, 1.f/255.f, 1f);
				break;
			default:
				break;
			}
			this.groundElems.add(new GroundElem(pos,size,degree,color, GroundElemType.ROCK));
			point = findPointOnHCurve(dX);
			xNoise = 0;//rand.nextInt(10);
			yNoise = -6;
			pos = new Vector2(point.x+xNoise, point.y+yNoise);
			size = Math.max(rand.nextFloat(), 0.2f) * 0.01f;
			degree = 0;//(float)rand.nextInt(360);
			color = Color.DARK_GRAY;
			switch(rand.nextInt(4)){
			case 0:
				break;
			case 1:
				//color = new Color(204.f/255.f, 136.f/255.f, 85.f/255.f, 1f);
				break;
			case 2:
				//color = new Color(204.f/255.f, 119.f/255.f, 34.f/255.f, 1f);
				break;
			case 3:
				//color = new Color(153.f/255.f, 85.f/255.f, 17.f/255.f, 1f);
				break;
			default:
				break;
			}
			this.groundElems.add(new GroundElem(pos,size,degree,color, GroundElemType.ROCK));
			dX += inc;
		}
	}
	
	private void generateGrass(int res){
		float diff = p2.x - p1.x;
		float inc = diff / (float)res;
		float dX = p1.x;
		Random rand = new Random();
		for( int i=0; i< res; i++ ) {
			Vector2 point = findPointOnHCurve(dX);
			float xNoise = rand.nextInt(10);
			float yNoise = -3;//rand.nextInt(40) * -1;
			Vector2 pos = new Vector2(point.x+xNoise, point.y+yNoise);
			float size = Math.max(rand.nextFloat(), 0.2f) * 0.03f;
			float degree = 0;//(float)rand.nextInt(360);
			Color color = Color.WHITE;
			switch(rand.nextInt(4)){
			case 0:
				break;
			case 1:
				//color = new Color(104.f/255.f, 56.f/255.f, 15.f/255.f, 1f);
				break;
			case 2:
				//color = new Color(104.f/255.f, 19.f/255.f, 4.f/255.f, 1f);
				break;
			case 3:
				//color = new Color(103.f/255.f, 15.f/255.f, 1.f/255.f, 1f);
				break;
			default:
				break;
			}
			this.groundElems.add(new GroundElem(pos,size,degree,color, GroundElemType.GRASS));
			point = findPointOnHCurve(dX);
			xNoise = rand.nextInt(10);
			yNoise = -3;//rand.nextInt(40) * -1;
			pos = new Vector2(point.x+xNoise, point.y+yNoise);
			size = Math.max(rand.nextFloat(), 0.2f) * 0.05f;
			degree = 0;//(float)rand.nextInt(360);
			color = Color.WHITE;
			switch(rand.nextInt(4)){
			case 0:
				break;
			case 1:
				//color = new Color(104.f/255.f, 56.f/255.f, 15.f/255.f, 1f);
				break;
			case 2:
				//color = new Color(104.f/255.f, 19.f/255.f, 4.f/255.f, 1f);
				break;
			case 3:
				//color = new Color(103.f/255.f, 15.f/255.f, 1.f/255.f, 1f);
				break;
			default:
				break;
			}
			this.groundElems.add(new GroundElem(pos,size,degree,color, GroundElemType.GRASS));
			dX += inc;
		}
	}
}
