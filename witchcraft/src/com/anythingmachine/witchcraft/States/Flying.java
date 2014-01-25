package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.player.items.Cape;

public class Flying extends State {
	private float rotation;

	public Flying(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	@Override
	public void update(float dt) {

		checkGround();

		setInputSpeed();

		// sm.animate.setFlipX(sm.test("facingleft"));

		sm.phyState.correctCBody(-8, 64, sm.neck.getRotation());

		if (WitchCraft.ON_ANDROID) {
			sm.animate.setFlipY(sm.test("facingleft"));
			sm.animate.rotate(-rotation * Util.RAD_TO_DEG);
		} else {
			sm.animate.setFlipX(sm.test("facingleft"));
		}
		Cape cape = WitchCraft.player.cape;
		if (sm.test("facingleft")) {
			cape.addWindForce(500, 0);
		} else {
			cape.addWindForce(-500, 0);
		}

		if (sm.test("facingleft")) {
			cape.updatePos(sm.neck.getWorldX() + 25, sm.neck.getWorldY() + 7,
					true, false);
		} else {
			cape.updatePos(sm.neck.getWorldX() + 5, sm.neck.getWorldY() + 7,
					false, false);
		}
	}

	@Override
	public void setInputSpeed() {
		rotation = sm.input.axisDegree();
		int axisVal = sm.input.axisRange2();
		boolean facingleft = sm.test("facingleft");
		if (axisVal > 0) {
			sm.setTestVal("facingleft", false);
			facingleft = false;
			sm.setTestVal("hitleftwall", false);
		} else if (axisVal < 0) {
			facingleft = true;
			sm.setTestVal("facingleft", true);
			rotation = -rotation;
			sm.setTestVal("hitrightwall", false);
		}
		if ( facingleft && !sm.test("hitleftwall")) {
			if ( rotation == 0 ) 
				rotation = Util.HALF_PI;
			rotation = -rotation;
			sm.phyState.setVel(-Util.PLAYERFLYSPEED,
					(float) Math.sin(rotation) * Util.PLAYERFLYSPEED);
		} else 	if (!sm.test("hitrightwall")) {
			sm.phyState.setVel(Util.PLAYERFLYSPEED,
					-(float) Math.sin(rotation) * Util.PLAYERFLYSPEED);
		}

	}

	@Override
	public boolean canAnimate() {
		return false;
	}

	@Override
	public void land() {
		sm.animate.rotate(0);
		if (sm.test("facingleft")) {
			sm.animate.setFlipX(true);
			sm.animate.setFlipY(false);
		}
		sm.setState(StateEnum.LANDING);
	}

	@Override
	public void setIdle() {

	}

	@Override
	public void setJumping() {
		sm.phyState.setYVel(150f);
	}

	@Override
	public boolean canCastSpell() {
		return false;
	}

	@Override
	public void setRun() {
	}

	@Override
	public void setWalk() {
	}

}
