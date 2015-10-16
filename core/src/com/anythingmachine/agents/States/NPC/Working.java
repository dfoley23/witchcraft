package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.agents.States.Transistions.ActionEnum;
import com.anythingmachine.aiengine.NPCStateMachine;

public class Working extends NPCState {

    public Working(NPCStateMachine sm, NPCStateEnum name) {
	super(sm, name);
    }

    @Override
    public void transistionIn() {
	sm.animate.bindPose();
	sm.animate.setCurrent("hammer", true);
	sm.phyState.body.stop();
	sm.phyState.body.setStable(true);
    }

    @Override
    public boolean transistionOut() {
	sm.phyState.body.setStable(false);
	return true;
    }

    @Override
    public ActionEnum[] getPossibleActions() {
	return new ActionEnum[] { ActionEnum.TURN, ActionEnum.WALK };
    }

    @Override
    public void setIdle() {
	sm.animate.bindPose();
	sm.animate.setCurrent("idle", true);
	sm.phyState.body.stop();
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
