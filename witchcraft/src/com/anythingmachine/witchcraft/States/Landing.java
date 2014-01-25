package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Landing extends State {

		public Landing (StateMachine sm, StateEnum name) {
 			super(sm, name);
		}

		@Override
		public boolean canCastSpell() {
			return false;
		}

}
