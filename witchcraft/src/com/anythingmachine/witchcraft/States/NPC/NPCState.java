package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.graphics.g2d.Batch;
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

		checkInBounds();
		
		checkAttack();
		
		takeAction(dt);

		checkGround();

		fixCBody();
	}
	
	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.facingleft);
		sm.animate.draw(batch);
	}
	
	public void checkInBounds() {
		if( !WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}
	
	public void setGoingTo() {
		
	}
	
	public void checkAttack() {
		if (sm.active) {
			sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState
					.body.getX();
			if (sm.canseeplayer) {
				if (GamePlayManager.player.inHighAlert()) {
					sm.state.setAttack();
				} else if (GamePlayManager.player.inAlert()) {
					sm.state.setAlert();
				}
			}
		}
	}
	
	public void checkTarget() {
		
	}
	
	public void fixCBody() {
		sm.phyState.collisionBody.setTransform(Util.addVecsToVec2(sm.phyState.body.getPos(), -8, 64).scl(Util.PIXEL_TO_BOX), 0);
	}
	
	public void setAttack() {
		sm.setState(sm.me.npctype.getAttackState());
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
		sm.setState(NPCStateEnum.IDLE);
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
		sm.state.setParent(this);
	}
	
	public void setRun() {
		sm.setState(NPCStateEnum.RUNNING);
		sm.state.setParent(this);
	}

	public void setParent(NPCState p) {
		parent = p;
	}
	
	public void transistionIn() {
		
	}
	
	public boolean transistionOut() {
		return true;
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
