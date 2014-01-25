package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.player.items.Cape;

public class Flying extends State {

		public Flying(StateMachine sm, StateEnum name) {
			super(sm, name);
		}

		@Override
		public void update(float dt) {
		
			checkGround();			
			setInputSpeed();
		
			
			sm.animate.setFlipX(sm.test("facingleft"));
			
			setInputSpeed();
			sm.phyState.correctCBody(-8, 64, Util.HALF_PI);

			Cape cape = WitchCraft.player.cape;
			if (sm.test("facingleft")) {
				cape.addWindForce(-500, 0);
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
			int axisVal = sm.input.axisRange2();
			if ( axisVal > 0 ) {
				sm.setTestVal("facingleft", false);
				if (!sm.test("hitrightwall") ) {
					sm.setTestVal("hitleftwall", false);
					if ( axisVal > 1 ) {
						sm.phyState.setXVel(Util.PLAYERFLYSPEED);
					} else {
						sm.phyState.setXVel(Util.PLAYERRUNSPEED);
					}
				}
			} else if ( axisVal < 0 ) {
				sm.setTestVal("facingleft", true);
				if (!sm.test("hitleftwall")){
					sm.setTestVal("hitrightwall", false);
					if ( axisVal < -1 ) {
						sm.phyState.setXVel(-Util.PLAYERFLYSPEED);
					} else {
						sm.phyState.setXVel(-Util.PLAYERRUNSPEED);				
					}
				}
			}
		}

		@Override
		public boolean canAnimate() {
			return true;
		}

		@Override
		public void land() {
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
