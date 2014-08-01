package com.anythingmachine.physicsEngine;

import com.anythingmachine.physicsEngine.particleEngine.particles.Particle;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;

public class Rope extends Spring {

	public Rope(Particle other, float restLength, float k, float damper) {
		super(other, restLength, k, damper);
	}

	public Vector3 accel(Vector3 pos, Vector3 vel) {
		Vector3 otherpos = other.getPos();
		Vector3 diff = new Vector3(pos.x - otherpos.x, pos.y - otherpos.y,
				pos.z - otherpos.z);
		// System.out.println(diff);
		float mag = (float) Math.sqrt((double) (diff.x * diff.x + diff.y
				* diff.y + diff.z * diff.z));
		Vector3 unitDir = new Vector3(diff.x / mag, diff.y / mag, diff.z / mag);
		Vector3 result = new Vector3(0, 0, 0);

		if ( diff.x < 0 ) { diff.x = -diff.x; }
		if ( diff.y < 0 ) { diff.y = -diff.y; }
		if ( diff.z < 0 ) { diff.z = -diff.z; }
		if (diff.x >= restLength)
			result.x = (-springK * (mag - restLength) * Util.dot(unitDir,
					new Vector3(1f, 0f, 0f))) - vel.x * damper;
		if (diff.y >= restLength)
			result.y = (-springK * (mag - restLength) * Util.dot(unitDir,
					new Vector3(0f, 1f, 0f))) - vel.y * damper;
		if (diff.z >= restLength)
			result.z = (-springK * (mag - restLength) * Util.dot(unitDir,
					new Vector3(0f, 0f, 1f))) - vel.z * damper;
		return result;
	}

}
