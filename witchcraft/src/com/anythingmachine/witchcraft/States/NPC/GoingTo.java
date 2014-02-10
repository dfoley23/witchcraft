package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.math.Vector2;

public class GoingTo extends NPCState {
	protected Vector2 target;
	
	public GoingTo(NPCStateMachine sm, NPCStateEnum name, Vector2 target) {
		super(sm, name);
		this.target = target;
	}
	
	@Override
	public void update(float dt) {
		checkGround();
		sm.facingleft = sm.phyState.getVelX() < 0;
		sm.animate.setFlipX(sm.facingleft);
		
		if ( Util.subVecs(sm.phyState.getPos(), target).len() < 128) {
			switch(name) {
			case GOINGTOEAT:
				sm.setState(NPCStateEnum.EATING);
				break;
			case GOINGTOSLEEP:
				sm.setState(NPCStateEnum.SLEEPING);
				break;
			case GOINGTOWORK:
				sm.setState(NPCStateEnum.WORKING);
				break;
			case GOINGTOPATROL:
				sm.setState(NPCStateEnum.PATROLLING);
				break;
			default:
				break;
			}
		}
		sm.phyState.collisionBody.setTransform(Util.addVecsToVec2(sm.phyState.body.getPos(), -8, 64).scl(Util.PIXEL_TO_BOX), 0);
		
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] {};
	}

	@Override
	public void transistionIn() {
		sm.facingleft = target.x < sm.phyState.getX();
		sm.setState(NPCStateEnum.WALKING);
		sm.state.setParent(this);
	}
	
	@Override
	public void setIdle() {
		sm.setState(NPCStateEnum.IDLE);
		sm.state.setParent(this);
	}

	@Override
	public void takeAction(float dt) {
	}
	
	@Override
	public void takeAction(Action action) {
		
	}
	
}
