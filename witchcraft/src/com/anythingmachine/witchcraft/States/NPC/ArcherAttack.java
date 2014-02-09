package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;

public class ArcherAttack extends NPCState {
	private Arrow arrow;
	private Bone arrowBone;

	public ArcherAttack(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		arrow = new Arrow(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
		arrowBone = sm.animate.findBone("right hand");
	}

	@Override
	public void update(float dt) {
		checkGround();
		if (WitchCraft.player.getX() < sm.phyState.getX()) {
			sm.setTestVal("facingleft", true);
		} else {
			sm.setTestVal("facingleft", false);
		}
		sm.animate.setFlipX(sm.test("facingleft"));
		if (sm.animate.getTime() > sm.animate.getCurrentAnimTime() * 0.75f) {
			arrow.setPos(arrowBone.getWorldX()
					+ (sm.test("facingleft") ? -128 : 128),
					arrowBone.getWorldY(), 0);
			arrow.pointAtTarget(WitchCraft.player.getPosPixels(), 650);
		} else {
			Vector3 target = WitchCraft.player.getPosPixels();
			Vector3 pos = sm.phyState.getPos();
			Vector3 dir = new Vector3(target.x - pos.x, target.y - pos.y, 0);
			float costheta = Util.dot(dir, new Vector3(1, 0, 0)) / dir.len();
			sm.animate.rotate((float) Math.acos(costheta));
		}
	}

	@Override
	public void transistionIn() {
		if (arrow.isStable() ) {
			sm.animate.bindPose();
			sm.animate.setCurrent("drawbow", true);
		} else {
			super.setIdle();
		}
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
