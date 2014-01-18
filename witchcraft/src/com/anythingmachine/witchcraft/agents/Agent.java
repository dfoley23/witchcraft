package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ground.Curve;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.esotericsoftware.spine.Skin;

public class Agent extends Entity {
	protected int curGroundSegment;
	protected Curve curCurve;
	protected Platform elevatedSegment;
	protected int elevatedSegmentIndex;
	protected boolean facingLeft;
	protected boolean onGround;
	protected KinematicParticle body;
	protected Body collisionBody;
	protected Fixture feetFixture;
	protected Fixture hitRadius;
	
	
	public void correctHeight(float y) {
		body.setPos(body.getPos().x, y, 0f);
	}
	

}
