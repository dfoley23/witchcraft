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
		checkInLevel();
		checkGround();
		checkInBounds();
		sm.facingleft = sm.phyState.body.getVelX() < 0;
		sm.animate.setFlipX(sm.facingleft);
		
		fixCBody();		
	}
	
	
	@Override
	public void checkTarget() {
		if ( Util.subVecs(sm.phyState.body.getPos(), target).len() < 128) {
			switch(name) {
			case GOINGTOEAT:
				sm.state.setChildState(NPCStateEnum.EATING);
				break;
			case GOINGTOSLEEP:
				sm.state.setChildState(NPCStateEnum.SLEEPING);
				break;
			case GOINGTOWORK:
				sm.state.setChildState(NPCStateEnum.WORKING);
				break;
			case GOINGTOPATROL:
				sm.state.setChildState(NPCStateEnum.PATROLLING);
				break;
			default:
				break;
			}
		}
	}
	@Override
	public void setGoingTo() {
		sm.phyState.body.setPos(target);
	}

	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] {};
	}

	@Override
	public void transistionIn() {
		sm.facingleft = target.x < sm.phyState.body.getX();
		sm.getState(NPCStateEnum.WALKING).transistionIn();
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
