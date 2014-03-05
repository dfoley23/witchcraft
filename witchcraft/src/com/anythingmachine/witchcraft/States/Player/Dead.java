package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.math.Vector3;

public class Dead extends PlayerState {
		private float fadeout;

		public Dead (PlayerStateMachine sm, PlayerStateEnum name) {
 			super(sm, name);
		}

		@Override
		public void update(float dt) {
			sm.phyState.correctCBody(-8, 64, 0);
			
			checkGround();
			
			if ( sm.animate.testOverTime(0, .75f) ) {
				fadeout-=dt;
			}
			GamePlayManager.player.cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
			if ( fadeout < -3 ) {
				super.setIdle();
			}
			
			addWindToCape(dt);
		}

		@Override
		public boolean canAnimate() {
			if ( sm.animate.testOverTime(0, .75f) ) {
				return false;
			}
			return true;
		}
			
		@Override
		public void setInputSpeed() {
			
		}
		
		@Override
		public void switchPower() {
			
		}
		
		@Override
		public void usePower() {
			
		}
		
		@Override
		public void setFlying() {
		}
		
		@Override
		public void setRun() {
		}

		@Override
		public void setWalk() {
		}

		@Override
		public void setIdle() {
			
		}
		
		@Override
		protected void checkGround() {
			if ( sm.hitplatform || sm.grounded ) {
				Vector3 pos = sm.phyState.body.getPos();
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
				if (pos.y < groundPoint+16 ) 
					sm.phyState.body.setY(groundPoint);
			}
		}

		@Override
		public void transistionIn() {
			sm.animate.bindPose();
			sm.phyState.body.stopOnX();
			sm.animate.setCurrent("ded", true);
			GamePlayManager.player.cape.addWindForce(0, -400);
			fadeout = 1;
			setParent(sm.getState(PlayerStateEnum.IDLE));
		}

}
