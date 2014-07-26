package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;

public class Attacking extends PlayerState {
		private boolean shotArrow;
		private Bone arrowBone;
		private Arrow arrow;
		
		public Attacking (PlayerStateMachine sm, PlayerStateEnum name) {
 			super(sm, name);
 			shotArrow = false;
 			arrow = new Arrow(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
 			arrowBone = sm.animate.findBone("right hand");
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			if (sm.animate.isSkin("archer")) {
				if (!shotArrow && sm.animate.isTImeOverThreeQuarters(0f)) {
					arrow.setPos(arrowBone.getWorldX() + (sm.facingleft ? -128 : 128),
							arrowBone.getWorldY(), 0);
					arrow.pointAtTarget(Util.addVecs(sm.phyState.body.getPos(), sm.facingleft ? -100 : 100, 0), 650);
					shotArrow = true;
				}
			}
			parent.updatePower(dt);

		}
		@Override
		public void setIdle() {
			if ( sm.animate.isTImeOverThreeQuarters(0)) {
				sm.animate.bindPose();
				sm.animate.setCurrent("idle", true);
				sm.setState(parent.name);
				sm.phyState.body.stop();
			}
		}
		
		@Override
		public void drawCape(Matrix4 cam) {
			parent.drawCape(cam);
		}

		@Override
		public void usePower() {
			
		}
		@Override
		public void nextPower() {
			
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
