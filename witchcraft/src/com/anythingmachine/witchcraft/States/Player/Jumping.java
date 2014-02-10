package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.Util.Util;

public class Jumping extends PlayerState {

		public Jumping(PlayerStateMachine sm, PlayerStateEnum name) {
			super(sm, name);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
						
			if ( sm.animate.testOverTime(0f, 0.95f) ) {
				setFlying();
			}
		}
		
		@Override
		public boolean isHighAlertState() {
			return false;
		}

		@Override
		public void transistionIn() {
			sm.animate.bindPose();
			sm.animate.setCurrent("jump", true);
			sm.phyState.setYVel(150f);
		}
				
		@Override
		public void usePower() {
			if( sm.input.is("UsePower"))
				sm.phyState.setYVel(150f);			
		}
		
		@Override 
		public void setIdle() {
			
		}
		
		@Override
		public void setInputSpeed() {
			int axisVal = sm.input.axisRange2();
			if (axisVal > 0) {
				sm.facingleft = sm.hitleftwall = false;
			} else if (axisVal < 0) {
				sm.facingleft = true;
				sm.hitrightwall = false;
			}
			if ( sm.facingleft && !sm.hitleftwall) {
				sm.phyState.setXVel(-Util.PLAYERFLYSPEED*0.75f);
			} else 	if (!sm.facingleft && !sm.hitrightwall) {
				sm.phyState.setXVel(Util.PLAYERFLYSPEED*0.75f);
			}

		}

		@Override
		public void setFlying() {
			sm.setState(PlayerStateEnum.FLYING);
		}
		
		
		@Override
		public void switchPower() {
			
		}
				
		@Override
		public void setAttack() {
			
		}
		@Override
		public void setWalk() {
		}
		@Override
		public void setRun() {
		}
		@Override
		public void checkGround() {
		}

}
