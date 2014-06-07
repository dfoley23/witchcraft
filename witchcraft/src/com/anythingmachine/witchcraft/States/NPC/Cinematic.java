package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Player.PlayerStateEnum;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class Cinematic extends Inactive {
	
	public Cinematic(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		checkInBounds();
		
		checkGround();
		
		fixCBody();
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return new ActionEnum[] { };
	}


	@Override
	public void checkInBounds() {
		if( WitchCraft.cam.inBounds(sm.phyState.body.getPos()) && sm.me.level == GamePlayManager.currentlevel) {
			sm.onscreen = true;
			sm.setState(childState.name);
			GamePlayManager.player.setState(PlayerStateEnum.CINEMATIC);
		}
	}	
	
	@Override	
	public boolean transistionOut() {
		return true;
	}


}
