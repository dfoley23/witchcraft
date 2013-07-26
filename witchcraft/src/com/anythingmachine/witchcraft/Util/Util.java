package com.anythingmachine.witchcraft.Util;



public class Util {

	public static final float PIXELS_PER_METER = 60.0f;
	public static final float BOX_TO_PIXEL = 256f;
	public static final float PIXEL_TO_BOX = 1 / BOX_TO_PIXEL;
	public static final float DEG_TO_RAD = 0.0174532925199432957f;
	public static final float RAD_TO_DEG = 57.295779513082320876f;
	public static final float PI = 3.141592653589793f;
	public static final float TWO_PI = 2 * PI;
	public static final float HALF_PI = PI / 2;
	public static final float FOURTH_PI = PI / 4;
	public static final float MIN_VALUE = 0x0.000002P-126f; // 1.4e-45f

	/**
	 * Collision Categories and masks for every object that needs them
	 */
	public static final short CATEGORY_PLAYER = 0x0002;
	public static final short CATEGORY_SUBPLAYER = 0x0004;
	public static final short CATEGORY_PLATFORMS = 0x0010;
	public static final short CATEGROY_HAZARD = 0x0100;
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
	
	public static final int[] cps = {
		150,425,
		230,425,
		310,425,
		390,425,
		230,425,
		310,425,
		390,425,
		470,425,
		310,425,
		390,425,
		470,425,
		560,425,
		390,425,
		470,425,
		560,405,
		640,393,
		470,425,
		560,405,
		640,393,
		720,425,
		560,405,
		640,393,
		720,411,
		800,425,
		640,393,
		720,411,
		800,425,
		880,425,
		720,387,
		800,425,
		880,414,
		960,425,
		800,425,
		880,414,
		960,425,
		1040,425,
		880,416,
		960,425,
		1040,415,
		1120,405,
		960,425,
		1040,415,
		1120,404,
		1200,390,
		1040,411,
		1120,404,
		1200,391,
		1280,405,
		1120,405,
		1200,391,
		1280,406,
		1360,425,
		1200,396,
		1280,406,
		1360,425,
		1440,425,
		1280,410,
		1360,425,
		1440,425,
		1520,425,
		1360,425,
		1440,425,
		1520,425,
		1600,425,
		1440,425,
		1520,425,
		1600,438,
		1680,425,
		1520,425,
		1600,438,
		1680,425,
		1760,410,
		1600,440,
		1680,425,
		1760,408,
		1840,425,
		1680,425,
		1760,408,
		1840,440,
		1920,425,
		1760,425,
		1840,440,
		1920,425,
		2000,425,
		1840,436,
		1920,425,
		2000,425,
		2080,425,
		1920,425,
		2000,425,
		2080,425,
		2160,425,
	};
/*		*/
	
	public static final int VERTEX_SIZE = 2 + 1 + 2;
}
