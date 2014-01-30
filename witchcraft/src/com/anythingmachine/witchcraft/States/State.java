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
	public StateEnum parent;

	public State(StateMachine sm, StateEnum name) {
		this.sm = sm;
		this.name = name;
		parent = StateEnum.IDLE;
	}

	public void setParent(StateEnum parent) {
		this.parent = parent;
	}

	public void update(float dt) {

		checkGround();

		setInputSpeed();

		sm.phyState.correctCBody(-8, 64, 0);

		setAttack();

		sm.animate.setFlipX(sm.test("facingleft"));

		Cape cape = WitchCraft.player.cape;

		cape.addWindForce(0, -400);

		cape.updatePos(sm.neck.getWorldX() + 14, sm.neck.getWorldY()-8);
	}

	public void setAttack() {
	}

	public boolean canAnimate() {
		return true;
	}

	public void setFlying() {
	}

	public void setIdle() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.setState(parent);
		sm.phyState.stop();
	}

	public void hitNPC(NonPlayer npc) {
		sm.dupeSkin = npc.getSkin().getName();
	}

	public void land() {
		sm.setTestVal("grounded", true);
		sm.phyState.stop();
	}

	public void setJumping() {
		if (isPlayer()) {
			sm.animate.bindPose();
			sm.animate.setCurrent("jump", true);
			sm.setState(StateEnum.JUMPING);
			sm.phyState.setYVel(150f);
		}
	}

	public void transistionIn() {
		
	}

	public void setDupeSkin() {
	}

	public boolean isPlayer() {
		return parent != StateEnum.DUPESKIN;
	}

	public void setCastSpell() {
		sm.setState(StateEnum.CASTSPELL);
		sm.state.setParent(this.name);
		sm.animate.bindPose();
		sm.animate.setCurrent("castspell", true);
	}

	public void setWalk() {
		sm.setState(StateEnum.WALKING);
		sm.state.setParent(this.name);
		sm.animate.bindPose();
		sm.animate.setCurrent("walk", true);
	}

	public void setRun() {
		sm.setState(StateEnum.RUNNING);
		sm.state.setParent(this.name);
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
	}

	public void setInvi() {
		if (isPlayer()) {
			sm.setTestVal("invi", true);
			sm.animate.switchSkin("invi");
			sm.animate.bindPose();
		}
	}

	public void setInputSpeed() {
		int axisVal = sm.input.axisRange2();
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
	}

	protected void checkGround() {
		Vector2 pos = sm.phyState.getPos();
		if (sm.test("hitplatform")) {
			// System.out.println(pos);
//			if (pos.x > sm.curCurve.lastPointOnCurve().x
//					&& sm.curGroundSegment + 1 < WitchCraft.ground
//							.getNumCurves()) {
//				sm.curGroundSegment++;
//				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
//			} else if (pos.x < sm.curCurve.firstPointOnCurve().x
//					&& sm.curGroundSegment - 1 >= 0) {
//				sm.curGroundSegment--;
//				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
//			}
//			Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
//					sm.curGroundSegment, pos.x);
//			sm.setTestVal("grounded", false);
////			if (pos.y <= groundPoint.y) {
//				sm.phyState.correctHeight(groundPoint.y);
//				sm.state.land();
////			}
//		} else {
			sm.setTestVal("grounded", false);
			if (sm.elevatedSegment.isBetween(sm.test("facingleft"), pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
//				if (pos.y < groundPoint) {
					sm.phyState.correctHeight(groundPoint);
					sm.state.land();
//				}
			} else {
				sm.setTestVal("hitplatform", false);
				sm.setState(StateEnum.FALLING);
			}
		}
	}

}
