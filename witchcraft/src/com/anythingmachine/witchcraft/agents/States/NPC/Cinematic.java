package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;

public class Cinematic extends NPCState {
	
	public Cinematic(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		checkInBounds();
		
		checkGround();
		
		fixCBody();

		updateSkel(dt);
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] { };
	}


	@Override
	public void checkInBounds() {
//		if( WitchCraft.cam.inBounds(sm.phyState.body.getPos()) && sm.me.level == GamePlayManager.currentlevel) {
			sm.onscreen = true;
//			sm.setState(childState.name);
//			GamePlayManager.player.setState(PlayerStateEnum.CINEMATIC);
//		}
	}	
	
	@Override	
	public boolean transistionOut() {
		return true;
	}
	
	@Override
	public void transistionIn() {
		if (sm.state.name != this.name ) 
			this.parent = sm.state;
		else
			this.parent = sm.getState(NPCStateEnum.IDLE);
	}

	@Override
	public void setIdle() {
		sm.getState(NPCStateEnum.IDLE).transistionIn();
	}

}
