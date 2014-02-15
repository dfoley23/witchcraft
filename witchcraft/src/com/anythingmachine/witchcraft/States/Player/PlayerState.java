package com.anythingmachine.witchcraft.States.Player;

import java.util.Random;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PlayerState {
	protected PlayerStateMachine sm;
	public PlayerStateEnum name;
	public PlayerState parent;
	private Random rand;
	private float windtimeout = 1.5f;

	public PlayerState(PlayerStateMachine sm, PlayerStateEnum name) {
		this.sm = sm;
		this.name = name;
		parent = this;
		rand = new Random();
	}
	
	public void nextPower() {
		sm.nextPower();
	}
	
	public boolean isAlertState() {
		return false;
	}
	
	public boolean isHighAlertState() {
		return false;
	}
	
	public void setParent(PlayerState parent) {
		this.parent = parent;
	}

	public void update(float dt) {
		checkGround();
		
		setInputSpeed();

		switchPower();

		usePower();
		
		updatePower(dt);
		
		sm.phyState.correctCBody(-8, 64, 0);

		setAttack();

		sm.animate.setFlipX(sm.facingleft);

		Cape cape = WitchCraft.player.cape;

		int windx = 0;		
		if ( windtimeout > 0 ) {
			windx = rand.nextInt(1500);
			if ( windx > 600 ) 
				windx = 0;
		} else if ( windtimeout < -1 ) {
			windtimeout = 1.5f;
		}
		windtimeout-=dt;
		
		cape.addWindForce(sm.facingleft ? windx : -windx, -400);

		cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
	}

	public void setAttack() {
	}

	public void draw(Batch batch) {
		sm.animate.draw(batch);
	}
	
	public void drawCape(Matrix4 cam) {
		WitchCraft.player.cape.draw(cam, 1f);
	}
	
	public void switchPower() {
		if (sm.input.isNowNotThen("SwitchPower") 
				|| (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower1") )
				|| (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower2") ) ) {
			nextPower();
			sm.uiFadein = 0f;
		}
	}
	
	public void usePower() {
		if (sm.input.is("UsePower")) {
			sm.usePower();
			sm.state.setParent(this);
		} 
	}
	
	public void updatePower(float dt) {
		
	}
	
	public void setFlying() {
	}

	public boolean canAnimate() {
		return true;
	}
	
	public void setIdle() {
		sm.setState(parent.name);
	}

	public void hitNPC(NonPlayer npc) {
		if ( sm.power == PlayerStateEnum.DUPESKINPOWER )
			sm.dupeSkin = npc.getSkin().getName();
	}

	public void land() {
		sm.grounded = true;
		sm.phyState.stop();
	}

	public void transistionIn() {
		
	}

	public void transistionOut() {
		
	}
	
	public void setDupeSkin() {
	}

	public void setWalk() {
		sm.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(this);
	}

	public void setRun() {
		sm.setState(PlayerStateEnum.RUNNING);
		sm.state.setParent(this);
	}

	public void setInputSpeed() {
		int axisVal = sm.input.axisRange2();
		if (axisVal > 0) {
			sm.facingleft = false;
			if (!sm.hitrightwall) {
				sm.hitleftwall = false;
				if (axisVal > 1) {
					setRun();
					sm.phyState.setXVel(Util.PLAYERRUNSPEED);
				} else {
					setWalk();
					sm.phyState.setXVel(Util.PLAYERWALKSPEED);
				}
			}
		} else if (axisVal < 0) {
			sm.facingleft = true;
			if (!sm.hitleftwall) {
				sm.hitrightwall = false;
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
		Vector3 pos = sm.phyState.getPos();
		if (sm.hitplatform) {
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
			sm.grounded = false;
			if (sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
//				if (pos.y < groundPoint) {
					sm.phyState.correctHeight(groundPoint);
					sm.state.land();
//				}
			} else {
				sm.hitplatform = false;
				sm.setState(PlayerStateEnum.FALLING);
				sm.state.setParent(sm.getState(PlayerStateEnum.IDLE));
			}
		}
	}

}
