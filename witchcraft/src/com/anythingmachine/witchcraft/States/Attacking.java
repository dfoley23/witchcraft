package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;

public class Attacking extends State {
		private boolean shotArrow;
		private Bone arrowBone;
		private Arrow arrow;
		
		public Attacking (StateMachine sm, StateEnum name) {
 			super(sm, name);
 			shotArrow = false;
 			arrow = new Arrow(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
 			arrowBone = sm.animate.findBone("right hand");
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			if (sm.animate.isSkin("archer")) {
				boolean facingleft = sm.test("facingleft");
				if (!shotArrow && sm.animate.isTImeOverThreeQuarters(0f)) {
					arrow.setPos(arrowBone.getWorldX() + (facingleft ? -128 : 128),
							arrowBone.getWorldY(), 0);
					arrow.pointAtTarget(Util.addVecs(sm.phyState.getPos(), new Vector2(facingleft ? -100 : 100, 0)), 650);
					shotArrow = true;
				}
			}

		}
		@Override
		public void setIdle() {
			if ( sm.animate.isTImeOverThreeQuarters(0)) {
				sm.animate.bindPose();
				sm.animate.setCurrent("idle", true);
				sm.setState(parent);
				sm.phyState.stop();
			}
		}

		@Override
		public void setInputSpeed() {
			setIdle();
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
