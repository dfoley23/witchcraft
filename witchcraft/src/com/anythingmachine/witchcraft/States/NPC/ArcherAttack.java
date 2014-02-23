package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;

public class ArcherAttack extends NPCState {
	private Arrow arrow;
	private Bone arrowBone;
	private boolean shotArrow;

	public ArcherAttack(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		arrow = new Arrow(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
		arrowBone = sm.animate.findBone("right hand");
	}

	@Override
	public void update(float dt) {
		checkGround();
		sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body.getX();
		sm.animate.setFlipX(sm.facingleft);
		if (sm.animate.getTime() > sm.animate.getCurrentAnimTime() * 0.75 && !shotArrow) {
			shotArrow = true;
			arrow.setPos(arrowBone.getWorldX()
					+ (sm.facingleft ? -128 : 128),
					arrowBone.getWorldY(), 0);
			arrow.pointAtTarget(GamePlayManager.player.getPosPixels(), 650);
		} else if ( !shotArrow ){
			Vector3 target = GamePlayManager.player.getPosPixels();
			Vector3 pos = sm.phyState.body.getPos();
			Vector3 dir = new Vector3(target.x - pos.x, target.y - pos.y, 0);
			float costheta = Util.dot(dir, new Vector3(1, 0, 0)) / dir.len();
			sm.animate.rotate((float) Math.acos(costheta));
		} else {
			if ( arrow.isStable() ) {
				transistionIn();
			}
		}
		if (GamePlayManager.player.dead() ) {
			super.setIdle();
		}
	}

	@Override
	public void checkInBounds() {
		
	}
	
	@Override 
	public void draw(Batch batch) {
		super.draw(batch);
		arrow.draw(batch);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("drawbow", true);
		sm.phyState.body.stop();
		shotArrow = false;
	}

	@Override
	public void takeAction(Action action) {

	}

	@Override
	public void setIdle() {
		if (sm.animate.atEnd()) {
			super.setIdle();
		}
	}

}
