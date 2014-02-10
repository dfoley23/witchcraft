package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.math.Vector3;

public class NPCState {
	protected NPCStateMachine sm;
	public NPCStateEnum name;	
	public NPCState parent;
	protected float aiChoiceTime;
	
	public NPCState(NPCStateMachine sm, NPCStateEnum name) {
		this.sm = sm;
		this.name = name;
	}
	
	public void update(float dt) {
		aiChoiceTime += dt;

		takeAction(dt);

		checkGround();

		sm.phyState.collisionBody.setTransform(Util.addVecsToVec2(sm.phyState.body.getPos(), -8, 64).scl(Util.PIXEL_TO_BOX), 0);

	}
	
	public void setAttack() {
		sm.setState(NPCStateEnum.ATTACKING);
	}

	public void setAlert() {
		sm.setState(NPCStateEnum.ALARMED);
	}
	
	public void takeAction(float dt) {
		if (aiChoiceTime > sm.behavior.getActionTime()) {
			sm.state.takeAction(sm.behavior.ChooseAction(sm.state));
			aiChoiceTime = 0;
		}
	}
		
	public void setTalking(NonPlayer npc) {
		sm.npc = npc;
		sm.setState(NPCStateEnum.TALKING);
	}
	
	public void setIdle() {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		sm.phyState.stop();
	}
	
	public ActionEnum[] getPossibleActions() {
		return ActionEnum.values();
	}
	
	public void takeAction(Action action) {
		if ( action != null )  {
			sm.behavior.takeAction(action);
			sm.setState(action.getAIState());
		}
	}
	
	public void setWalk() {
		sm.setState(NPCStateEnum.WALKING);
	}
	
	public void setRun() {
		sm.setState(NPCStateEnum.RUNNING);
	}

	public void setParent(NPCState p) {
		parent = p;
	}
	
	public void transistionIn() {
		
	}
	
	public void transistionOut() {
	}
	
	public void immediateTransOut() {
		
	}

	public void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		if (sm.elevatedSegment != null && sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
			float groundPoint = sm.elevatedSegment.getHeight(pos.x);
				sm.phyState.body.setPos(pos.x, groundPoint-10, 0);
//				sm.setTestVal("grounded", true);
		} 
	}

}
