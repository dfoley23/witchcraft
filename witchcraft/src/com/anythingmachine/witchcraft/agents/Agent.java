package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ground.Curve;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Agent extends Entity {
	protected int curGroundSegment;
	protected Curve curCurve;
	protected Platform elevatedSegment;
	protected int elevatedSegmentIndex;
	protected boolean facingLeft;
	protected boolean onGround;
	protected AnimationManager animate;
	protected KinematicParticle body;
	
	protected void checkGround() {
		Vector3 pos = body.getPos();
		if (pos.x > curCurve.lastPointOnCurve().x) {
			curGroundSegment++;
			if (curGroundSegment >= WitchCraft.ground.getNumCurves()) {
				body.setVel(-50, 0, 0);
				facingLeft = !facingLeft;
			}
			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		} else if (pos.x < curCurve.firstPointOnCurve().x) {
			curGroundSegment--;
			if (curGroundSegment == 0) {
				body.setVel(50, 0, 0);
				facingLeft = !facingLeft;
			}
			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		}
		animate.setFlipX(facingLeft);
		Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
				curGroundSegment, pos.x);
		if (pos.y < groundPoint.y) {
			correctHeight(groundPoint.y);
			onGround = true;
		}
	}
	
	public void correctHeight(float y) {
		body.setPos(body.getPos().x, y, 0f);
	}

}
