package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class Sleeping extends NPCState {
	private float time;
	private float timeout = 7;
	
	public Sleeping(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		time = 0;
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.stop();
		time = 0;
	}
	
	@Override
	public void update(float dt) {
		checkGround();
		
		time += dt;
		if ( time > timeout ) {
			sm.setState(NPCStateEnum.IDLE);
		}
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		if ( time > timeout ) {
			return new ActionEnum[] {ActionEnum.SLEEP};
		}
		return new ActionEnum[] {};
	}

}
