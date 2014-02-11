package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

public class Dead extends PlayerState {
		private float fadeout;

		public Dead (PlayerStateMachine sm, PlayerStateEnum name) {
 			super(sm, name);
		}

		@Override
		public void update(float dt) {
			checkGround();
			if ( sm.animate.testOverTime(0, .75f) ) {
				fadeout-=dt;
			}
			WitchCraft.player.cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
			if ( fadeout < -3 ) {
				super.setIdle();
			}
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
		public void transistionIn() {
			sm.animate.bindPose();
			sm.phyState.stopOnX();
			sm.animate.setCurrent("ded", true);
			fadeout = 1;
			setParent(sm.getState(PlayerStateEnum.IDLE));
		}

}
