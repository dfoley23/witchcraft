package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.badlogic.gdx.math.Vector2;

public class State {
	protected StateMachine sm;
	public StateEnum name;

	public State(StateMachine sm, StateEnum name) {
		this.sm = sm;
		this.name = name;
	}

	public void update(float dt) {

		checkGround();
		
		setInputSpeed();
		
		sm.phyState.correctCBody(-8, 64, 0);

		setAttack();
		
		sm.animate.setFlipX(sm.test("facingleft"));
		
		Cape cape = WitchCraft.player.cape;
		
		cape.addWindForce(0, -400);

		if (sm.test("facingleft")) {
			cape.updatePos(
					sm.neck.getWorldX() + 25,
					sm.neck.getWorldY(), true, false);
		} else {
			cape.updatePos(
					sm.neck.getWorldX() + 7,
					sm.neck.getWorldY(), false, false);
		}

	}

	public void setAttack() {
		if ( sm.input.is("attack") ) {
			if (sm.animate.isSkin("archer")) {
				sm.setState(StateEnum.ATTACKING);
				sm.animate.setCurrent("drawbow", true);
			} else if (sm.animate.isSkin("knight")) {
				sm.setState(StateEnum.ATTACKING);
				sm.animate.setCurrent("swordattack", true);
			}
		}
	}
	
	public boolean canAnimate() {
		return true;
	}

	public void setInputSpeed() {
		int axisVal = sm.input.axisRange2();
		if (!sm.testORtest("usingpower", "dupeskin")) {
			if (axisVal > 0) {
				sm.setTestVal("facingleft", false);
				if (!sm.test("hitrightwall")) {
					sm.setTestVal("hitleftwall", false);
					if (axisVal > 1) {
						setRun();
						sm.phyState.setXVel(Util.PLAYERRUNSPEED);
					} else {
						setWalk();
						sm.phyState.setXVel(Util.PLAYERWALKSPEED);
					}
				}
			} else if (axisVal < 0) {
				sm.setTestVal("facingleft", true);
				if (!sm.test("hitleftwall")) {
					sm.setTestVal("hitrightwall", false);
					if (axisVal < -1) {
						setRun();
						sm.phyState.setXVel(-Util.PLAYERRUNSPEED);
					} else {
						setWalk();
						sm.phyState.setXVel(-Util.PLAYERWALKSPEED);
					}
				}
			} else {
				setIdle();
			}
		} else {
		}
	}

	public void setFlying() {
	}

	public void setIdle() {
		if ( !sm.testORtest("usingpower", "dupeskin") ) {
			sm.animate.bindPose();
			sm.animate.setCurrent("idle", true);
			sm.setState(StateEnum.IDLE);
			sm.phyState.stop();
		}
	}

	public void hitNPC(NonPlayer npc) {
		if (sm.test("dupeskin")) {
			sm.animate.switchSkin(npc.getSkin().getName());
			sm.animate.bindPose();
			sm.setTestVal("usingdupeskin", true);
			sm.setTestVal("dupeskin", false);
		}
	}

	public void land() {
		sm.setTestVal("grounded", true);
		sm.phyState.stop();
	}

	public void setJumping() {
		if ( !sm.testtesttestOR("usingpower", "dupeskin", "usingdupeskin") ) {
			sm.animate.bindPose();
			sm.animate.setCurrent("jump", true);
			sm.setState(StateEnum.JUMPING);
			sm.phyState.setYVel(150f);
		}
	}

	public boolean canCastSpell() {
		return !sm.testORtest("usingpower", "dupeskin");
	}

	public void setWalk() {
		if (!sm.testORtest("usingpower", "dupeskin")) {
			sm.setState(StateEnum.WALKING);
			sm.animate.bindPose();
			sm.animate.setCurrent("walk", true);
		}
	}

	public void setRun() {
		if (!sm.testORtest("usingpower", "dupeskin")) {
			sm.setState(StateEnum.RUNNING);
			sm.animate.bindPose();
			sm.animate.setCurrent("run", true);
		}
	}

	protected void checkGround() {
		Vector2 pos = sm.phyState.getPos();
		if (!sm.test("hitplatform")) {
			// System.out.println(pos);
			if (pos.x > sm.curCurve.lastPointOnCurve().x
					&& sm.curGroundSegment + 1 < WitchCraft.ground.getNumCurves()) {
				sm.curGroundSegment++;
				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
			} else if (pos.x < sm.curCurve.firstPointOnCurve().x
					&& sm.curGroundSegment - 1 >= 0) {
				sm.curGroundSegment--;
				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
			}
			Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
					sm.curGroundSegment, pos.x);
			sm.setTestVal("grounded", false);
			if (pos.y <= groundPoint.y) {
				sm.phyState.correctHeight(groundPoint.y);
				sm.state.land();
			}
		} else {
			sm.setTestVal("grounded", false);
			if (sm.elevatedSegment.isBetween(sm.test("facingleft"), pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
				if (pos.y < groundPoint) {
					sm.phyState.correctHeight(groundPoint);
					sm.state.land();
				}
			} else {
				sm.setTestVal("hitplatform", false);
			}
		}
	}

}
