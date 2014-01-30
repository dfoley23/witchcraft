package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Dead extends State {
		public Dead (StateMachine sm, StateEnum name) {
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

}
