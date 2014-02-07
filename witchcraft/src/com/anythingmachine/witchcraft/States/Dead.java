package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Dead extends PlayerState {
		public Dead (PlayerStateMachine sm, PlayerStateEnum name) {
 			super(sm, name);
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
		public void transistionIn() {
			sm.animate.bindPose();
			sm.phyState.stopOnX();
			sm.animate.setCurrent("dead", true);
		}

}
