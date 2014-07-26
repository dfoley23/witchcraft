package com.anythingmachine.witchcraft.Util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Util {

	public static final boolean DEV_MODE = false;
	
	private Util() {
		throw new AssertionError();
	}

	public static short[] triangulateRect(short w, short h, short offset) {
		short[] indices = new short[(w-1)*(h-1)*6];
		for (short i = 0; i < (h-1); i++) {
			for (short j = 0; j < (w-1); j++) {
				indices[(i*(w-1)*6)+(j*6)] = (short) (((i*w)+(j)));//*offset);
				indices[(i*(w-1)*6)+(j*6)+1] = (short) (((i*w)+(j)+w));//*offset);
				indices[(i*(w-1)*6)+(j*6)+2] = (short) (((i*w)+(j)+w+1));//*offset);

				indices[(i*(w-1)*6)+(j*6)+3] = (short) (((i*w)+(j)));//*offset));
				indices[(i*(w-1)*6)+(j*6)+4] = (short) (((i*w)+(j)+w+1));//*offset);
				indices[(i*(w-1)*6)+(j*6)+5] = (short) (((i*w)+(j)+1));//*offset);
			}
		}
		return indices;
	}

	public static float dot(Vector3 v, Vector3 u) {
		return v.x * u.x + v.y * u.y + v.z * u.z;
	}

	public static Vector3 addVecs(Vector3 v, Vector3 u) {
		return new Vector3(v.x + u.x, v.y + u.y, v.z + u.z);
	}
	public static Vector3 addVecs(Vector3 v, float x, float y) {
		return new Vector3(v.x + x, v.y + y, 0);
	}

	public static Vector2 addVecsToVec2(Vector3 v, float x, float y) {
		return new Vector2(v.x + x, v.y + y);
	}
	public static Vector2 addVecs(Vector2 v, Vector2 u) {
		return new Vector2(v.x + u.x, v.y + u.y);
	}

	public static Vector3 subVecs(Vector3 v, Vector3 u) {
		return new Vector3(v.x - u.x, v.y - u.y, v.z - u.z);
	}

	public static Vector3 subVecs(Vector3 v, Vector2 u) {
		return new Vector3(v.x - u.x, v.y - u.y, 0);
	}

	public static Vector3 addVec(Vector3 v, float s) {
		return new Vector3(v.x + s, v.y + s, v.z + s);
	}

	public static Vector3 sclVec(Vector3 v, float s) {
		return new Vector3(v.x * s, v.y * s, v.z * s);
	}

	public static Vector2 sclVecTo2(Vector3 v, float s) {
		return new Vector2(v.x * s, v.y * s);
	}

//	public static int loadShader(String filename, int type) {
//		StringBuilder shaderSource = new StringBuilder();
//		int shaderID = 0;
//
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(filename));
//			String line;
//			while ((line = reader.readLine()) != null) {
//				shaderSource.append(line).append("\n");
//			}
//			reader.close();
//		} catch (IOException e) {
//			System.err.println("Could not read file.");
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
////		shaderID = GL20.glCreateShader(type);
////		GL20.glShaderSource(shaderID, shaderSource);
////		GL20.glCompileShader(shaderID);
//
//		return shaderID;
//	}
//	
//	public static int readShaderString(String file, int type) {
//		int shaderID = 0;
////
////		shaderID = GL20.glCreateShader(type);
////		GL20.glShaderSource(shaderID, file);
////		GL20.glCompileShader(shaderID);
////		System.out.println(GL20.glGetShaderInfoLog(shaderID, 255));
//		return shaderID;
//	}
	
	public static final float PIXELS_PER_METER = 60.0f;
	public static final float PIXEL_TO_BOX = 1 / PIXELS_PER_METER;
	public static final float DEG_TO_RAD = 0.0174532925199432957f;
	public static final float RAD_TO_DEG = 57.295779513082320876f;
	public static final float PI = 3.141592653589793f;
	public static final float TWO_PI = 2 * PI;
	public static final float HALF_PI = PI / 2.f;
	public static final float FOURTH_PI = PI / 4;
	public static final float SIXTH_PI = PI / 6;
	public static final float MIN_VALUE = 0x0.000002P-126f; // 1.4e-45f

	public static final int GRAVITY = -70;
	public static final int FIRESPEED = 40;
	public static final float PLAYERWALKSPEED = 215f;
	public static final float PLAYERRUNSPEED = 350f;
	public static final float PLAYERFLYSPEED = 500f;

	/**
	 * Collision Categories and masks for every object that needs them
	 */
	public static final short CATEGORY_PLAYER = 0x0002;
	public static final short CATEGORY_NPCMASK = 0x0004;
	public static final short CATEGORY_NPC = 0x0008;
	public static final short CATEGORY_ITEMS = 0x0010;
	public static final short CATEGORY_PARTICLES= 0x0020;
	public static final short CATEGORY_TRIGGERS = 0x0040;
	public static final short CATEGORY_ENVIRONMENT = 0x0080;
	public static final short CATEGORY_IGNORE = 0x1000;
	public static final short CATEGORY_NOTHING = 0x0000;
	public static final short CATEGORY_EVERYTHING = -1;
	public static final float [] catM = { 
			-0.5f, 1.0f, -0.5f, 0.0f,
			1.5f, -2.5f, 0.0f, 1.0f,
			-1.5f, 2.0f, 0.5f, 0.0f,
			0.5f, -0.5f, 0.0f, 0.0f, 
			};
	
	public static final float offScreenGround = 0f;
	
	public static final int curveLength = 80;
	
	public static final int VERTEX_SIZE = 2 + 1 + 2;

	public enum EntityType {
		PLATFORM, STAIRS, ENTITY, SWORD, ARROW, PLAYER, FIRE, PARTICLE,NONPLAYER, WALL,LEVELWALL,
	}

}
