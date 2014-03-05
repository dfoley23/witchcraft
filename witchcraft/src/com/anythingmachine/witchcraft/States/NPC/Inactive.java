package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Inactive extends NPCState {
	public NPCState childState;
	
	public Inactive(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		aiChoiceTime += dt;

		childState.checkTarget();
		
		if ( sm.me.npctype.canAttack() )
			checkAttack();
		else
			checkInBounds();
		
		checkGround();

		takeAction(dt);
		
		fixCBody();
		
	}

	@Override
	public void setChildState(NPCStateEnum state) {
		childState = sm.getState(state);
	}

	@Override
	public void draw(Batch batch) {

	}
	
	@Override
	public void checkAttack() {
		if (WitchCraft.cam.inBigBounds(sm.phyState.body.getPos())) {
			checkInBounds();
			sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState.body
					.getX();
			if (sm.canseeplayer) {
				if (GamePlayManager.player.inHighAlert()) {
					childState = sm.getState(NPCStateEnum.ATTACKING);
				} else if (GamePlayManager.player.inAlert()) {
					sm.state.setAlert();
				}
			}
		}
	}

	@Override
	public void checkInLevel() {
		sm.onscreen = true;
		super.checkInLevel();
		sm.onscreen = false;
	}
	
	@Override
	public void setAttack() {

	}

	@Override
	public void setAlert() {

	}

	@Override
	public void checkInBounds() {
		if( WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			sm.onscreen = true;
			sm.setState(childState.name);
		}
	}

	@Override
	public void takeAction(float dt) {
		if (aiChoiceTime > sm.behavior.getActionTime()) {
			takeAction(sm.behavior.ChooseAction(childState));
			aiChoiceTime = 0;
		}
	}
	
	@Override
	public void setTalking(NonPlayer npc) {
		sm.npc = npc;
		childState = sm.getState(NPCStateEnum.TALKING);
		childState.transistionIn();
	}
	
	@Override
	public void setIdle() {
		sm.phyState.body.stop();
		childState = sm.getState(NPCStateEnum.IDLE);
		childState.transistionIn();
	}
		
	@Override
	public void takeAction(Action action) {
		if ( action != null )  {
			sm.behavior.takeAction(action);
			childState = sm.getState(action.getAIState());
			childState.transistionIn();
		}
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		return childState.getPossibleActions();
	}
	
	@Override
	public void setParent(NPCState p) {
		childState = p;
		System.out.println(sm.me.npctype+" "+p.name);
	}
	
	@Override	
	public void transistionIn() {
		sm.onscreen = false;
	}

	@Override	
	public boolean transistionOut() {
		return sm.onscreen;
	}

	@Override
	public void immediateTransOut() {
		
	}

}
