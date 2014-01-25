package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Falling extends State {

		public Falling (StateMachine sm, StateEnum name) {
 			super(sm, name);
		}

		@Override
		public void setWalk() {
		}

		@Override
		public boolean canCastSpell() {
			return false;
		}

		@Override
		public void setRun() {
		}

}
