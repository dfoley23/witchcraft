package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Idle extends State {

		public Idle (StateMachine sm, StateEnum name) {
 			super(sm, name);
		}

		@Override
		public void transistionIn() {
			sm.phyState.stop();
			sm.animate.bindPose();
			sm.animate.setCurrent("idle", true);			
		}
}
