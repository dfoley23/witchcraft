package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.ground.Curve;
import com.anythingmachine.witchcraft.ground.Ground;
import com.anythingmachine.witchcraft.ground.Platform;

public class Agent extends Entity {
	protected int curGroundSegment;
	protected Curve curCurve;
	protected boolean onElevatedSegment;
	protected Platform elevatedSegment;
	protected int elevatedSegmentIndex;
}
