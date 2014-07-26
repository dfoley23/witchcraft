package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public class Idle extends PlayerState {

		public Idle (PlayerStateMachine sm, PlayerStateEnum name) {
 			super(sm, name);
		}

		@Override
		public void transistionIn() {
			sm.phyState.body.stop();
			sm.animate.bindPose();
			sm.animate.setCurrent("idle", true);			
		}
}
