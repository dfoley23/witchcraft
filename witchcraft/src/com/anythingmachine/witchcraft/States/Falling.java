package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.math.Vector2;

public class Falling extends State {

		public Falling (StateMachine sm, StateEnum name) {
 			super(sm, name);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
//			sm.phyState.body.addVel(0, Util.GRAVITY, 0);
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
		public void setCastSpell() {
			
		}
		
		@Override
		public void setRun() {
		}
		
		@Override
		public void setJumping() {
			
		}
		
		@Override
		public void checkGround() {
			Vector2 pos = sm.phyState.getPos();
			sm.setTestVal("grounded", false);
			if (sm.test("hitplatform")) {
				if (sm.elevatedSegment.isBetween(sm.test("facingleft"), pos.x)) {
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
			sm.setState(StateEnum.LANDING);
		}
}
