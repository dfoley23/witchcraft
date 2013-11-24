package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.math.Vector3;

public class Derivative {

	public Vector3 dp;
	public Vector3 dv;
	
	public Derivative() {
		dp = new Vector3(0, 0, 0);
		dv = new Vector3(0, 0, 0);
	}
}
