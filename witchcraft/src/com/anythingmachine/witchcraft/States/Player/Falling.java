package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Vector3;

public class Falling extends PlayerState {

		public Falling (PlayerStateMachine sm, PlayerStateEnum name) {
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
		public void checkGround() {
			Vector3 pos = sm.phyState.getPos();
			sm.grounded = false;
			if (sm.hitplatform) {
				if (sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
					float groundPoint = sm.elevatedSegment.getHeight(pos.x);
					if (pos.y < groundPoint) {
						sm.phyState.correctHeight(groundPoint);
						sm.state.land();
					}
				}
			}
		}
		
		@Override
		public void transistionIn() {
			sm.animate.setCurrent("idle", true);
			sm.animate.bindPose();
		}
		
		@Override
		public void land() {
			super.land();
			sm.setState(PlayerStateEnum.IDLE);
		}
}
