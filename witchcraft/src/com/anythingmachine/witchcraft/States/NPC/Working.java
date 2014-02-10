package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class Working extends NPCState {

	public Working(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.stop();
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] {ActionEnum.TURN, ActionEnum.WALK};
	}

	@Override
	public void setIdle() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.stop();
	}
	
	@Override
	public void setWalk() {
		sm.setState(NPCStateEnum.WALKING);
		sm.state.setParent(this);
	}
	
	@Override
	public void setRun() {
		sm.setState(NPCStateEnum.WALKING);
		sm.state.setParent(this);
	}

}
