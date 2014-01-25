package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Jumping extends State {

		public Jumping(StateMachine sm, StateEnum name) {
			super(sm, name);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			
			sm.animate.applyTotalTime(true, dt);
			if ( sm.animate.testOverTime(0f, 0.95f) ) {
				setFlying();
			}
		}
		
		@Override
		public void setJumping() {
			sm.phyState.setYVel(150f);
		}
		
		@Override 
		public void setIdle() {
			
		}
		
		@Override
		public void setFlying() {
			sm.setState(StateEnum.FLYING);
		}
		
		@Override
		public boolean canCastSpell() {
			return false;
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

}
