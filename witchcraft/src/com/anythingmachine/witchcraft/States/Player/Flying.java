package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.badlogic.gdx.math.Vector3;

public class Flying extends Jumping {
	private float rotation;
	private float hitrooftimeout = 0.5f;
	private float time = 0f;

	public Flying(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public boolean canAnimate() {
		return false;
	}

	@Override
	public void transistionIn(){
		
	}
	
	@Override
	public void update(float dt) {
		checkGround();
		
		setInputSpeed();

		usePower();

		if (WitchCraft.ON_ANDROID) {
			if (sm.test("facingleft")) {
				sm.phyState.correctCBody(-8, 64, -rotation
						+ (120 * Util.DEG_TO_RAD));
				sm.animate.rotate((-rotation * Util.RAD_TO_DEG) + 120);
			} else {
				sm.phyState.correctCBody(-8, 64, rotation
						+ (120 * Util.DEG_TO_RAD));
				sm.animate.rotate((rotation * Util.RAD_TO_DEG) + 120);
			}
			sm.phyState.body.addVel(0, Util.GRAVITY, 0);
		} else {
			sm.phyState.correctCBody(-8, -64, -Util.HALF_PI);
			sm.phyState.body.addVel(0, Util.GRAVITY, 0);
		}

		sm.animate.setFlipX(sm.test("facingleft"));

		time += dt;
		if (time > hitrooftimeout) {
			sm.setTestVal("hitroof", false);
			time = 0;
		}

		Cape cape = WitchCraft.player.cape;
		if (sm.test("facingleft")) {
			cape.addWindForce(500, 0);
		} else {
			cape.addWindForce(-500, 0);
		}

		cape.updatePos(sm.neck.getWorldX() + 14, sm.neck.getWorldY());
	}

	@Override
	public void setInputSpeed() {
		rotation = -sm.input.axisDegree();
		int axisVal = sm.input.axisRange2();
		boolean facingleft = sm.test("facingleft");
		if (axisVal > 0) {
			sm.setTestVal("facingleft", false);
			facingleft = false;
			sm.setTestVal("hitleftwall", false);
		} else if (axisVal < 0) {
			facingleft = true;
			sm.setTestVal("facingleft", true);
			sm.setTestVal("hitrightwall", false);
		}
		if (facingleft && !sm.test("hitleftwall")) {
			if (rotation == 0)
				rotation = Util.PI;
			float x = -Util.PLAYERFLYSPEED;
			float y = (float) Math.sin(rotation) * Util.PLAYERFLYSPEED;
			sm.phyState.body.setVel(x, y, 0);
		} else if (!facingleft && !sm.test("hitrightwall")) {
			rotation += Util.PI;
			float x = Util.PLAYERFLYSPEED;
			float y = (float) -Math.sin(rotation) * Util.PLAYERFLYSPEED;
			sm.phyState.body.setVel(x, y, 0);
		} else {
			sm.phyState.stop();
		}

	}

	@Override
	public void land() {
		sm.animate.rotate(0);
		if (sm.test("facingleft")) {
			sm.animate.setFlipX(true);
		}
		sm.setState(PlayerStateEnum.IDLE);
	}

	@Override
	public void usePower() {
		if (sm.input.is("UsePower")) {
			if (!sm.test("hitroof")) {
				sm.phyState.setYVel(150f);
			} else {
				time += WitchCraft.dt;
				if (time > hitrooftimeout) {
					sm.setTestVal("hitroof", false);
					time = 0;
				}
			}
		}
	}

	@Override
	public void checkGround() {
		Vector3 pos = sm.phyState.getPos();
		sm.setTestVal("grounded", false);
		if (sm.test("hitplatform")) {
			if (sm.elevatedSegment.isBetween(sm.test("facingleft"), pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
				if (pos.y < groundPoint) {
					sm.phyState.correctHeight(groundPoint);
					sm.state.land();
				}
			}
		}
	}

}
