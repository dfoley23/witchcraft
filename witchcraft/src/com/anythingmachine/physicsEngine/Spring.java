package com.anythingmachine.physicsEngine;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;

public class Spring {
	private Particle other;
	private float restLength;
	private float damper;
	private float springK;
	
	public Spring( Particle other, float restLength, float k, float damper) {
		this.other = other;
		this.restLength = restLength;
		this.damper = damper;
		this.springK = k;
	}
	
	public Vector3 accel(Vector3 pos, Vector3 vel) {
		Vector3 otherpos = other.getPos();
		Vector3 diff = new Vector3(pos.x - otherpos.x, pos.y - otherpos.y, pos.z - otherpos.z);
		//System.out.println(diff);
		float mag =(float) Math.sqrt((double)(diff.x*diff.x + diff.y*diff.y +diff.z*diff.z));
		Vector3 unitDir = new Vector3(diff.x/mag, diff.y/mag, diff.z/mag);
		Vector3 result =  new Vector3(0, 0, 0);

		result.x = (-springK * (mag-restLength)*Util.dot(unitDir, new Vector3(1f,0f,0f)))-vel.x*damper;
	    result.y = (-springK * (mag-restLength)*Util.dot(unitDir, new Vector3(0f,1f,0f)))-vel.y*damper;
		result.z = (-springK * (mag-restLength)*Util.dot(unitDir, new Vector3(0f,0f,1f)))-vel.z*damper;
		return result;
	}
	
	public Particle getOther() {
		return other;
	}
}
