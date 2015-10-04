package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Vector3;

public class Falling extends SharedState {

	public Falling(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void setIdle() {

	}

	@Override
	public void setAttack() {

	}

	@Override
	public void setWalk() {
	}

	@Override
	public void switchPower() {

	}

	@Override
	public void usePower() {

	}

	@Override
	public void setRun() {
	}

	@Override
	public void updatePower(float dt) {

	}

	@Override
	public void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		if (sm.hitplatform || sm.hitstairs && sm.currentPlatform != null) {
		    // System.out.println(pos);
		    // if (pos.x > sm.curCurve.lastPointOnCurve().x
		    // && sm.curGroundSegment + 1 < WitchCraft.ground
		    // .getNumCurves()) {
		    // sm.curGroundSegment++;
		    // sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
		    // } else if (pos.x < sm.curCurve.firstPointOnCurve().x
		    // && sm.curGroundSegment - 1 >= 0) {
		    // sm.curGroundSegment--;
		    // sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
		    // }
		    // Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
		    // sm.curGroundSegment, pos.x);
		    // sm.setTestVal("grounded", false);
		    // // if (pos.y <= groundPoint.y) {
		    // sm.phyState.correctHeight(groundPoint.y);
		    // sm.state.land();
		    // // }
		    // } else {
		    sm.grounded = false;
		    float groundPoint = sm.currentPlatform.getHeight(pos.x);
		    if (sm.hitstairs && sm.currentPlatform.isStairs()) {
			// sm.phyState.body.setX(sm.elevatedSegment
			// .getXPos(groundPoint));
			sm.phyState.body.setY(groundPoint);
			sm.state.land();
			transistionToParent();
		    } else if (sm.hitplatform && sm.currentPlatform.isBetween(sm.facingleft, pos.x)) {
			sm.phyState.body.setY(groundPoint);
			sm.state.land();
			transistionToParent();
		    }
		}
	}

	@Override
	public void transistionIn() {
		super.transistionIn();
		sm.animate.setCurrent("idle", true);
		sm.animate.bindPose();
	}

	@Override
	public void land() {
		sm.setState(parent.name);
	}
}
