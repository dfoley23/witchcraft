package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector2;

public class GoingToQuickly extends GoingTo {

	public GoingToQuickly(NPCStateMachine sm, NPCStateEnum name, Vector2 target) {
		super(sm, name, target);
	}
	
	@Override
	public void transistionIn() {
		sm.facingleft = target.x < sm.phyState.getX();
		sm.animate.bindPose();
		sm.animate.setCurrent("run", true);
		sm.phyState.setVel(sm.facingleft ? -Util.PLAYERRUNSPEED : Util.PLAYERRUNSPEED, 0);
	}
}
