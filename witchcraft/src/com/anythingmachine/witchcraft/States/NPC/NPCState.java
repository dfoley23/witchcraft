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

		checkInLevel();

		checkInBounds();

		checkGround();

		checkAttack();

		takeAction(dt);

		fixCBody();

	}

	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.facingleft);
		sm.animate.draw(batch);
	}

	public void checkInBounds() {
		if (!WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}

	public void setGoingTo() {

	}

	public void checkAttack() {
		sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState.body
				.getX();
		if (sm.canseeplayer) {
			if (GamePlayManager.player.inHighAlert()) {
				sm.state.setAttack();
			} else if (GamePlayManager.player.inAlert()) {
				sm.state.setAlert();
			}
		}
	}

	public void checkTarget() {

	}

	public void switchLevel(int level) {
		checkInLevel();
	}

	public void fixCBody() {
		sm.phyState.collisionBody.setTransform(
				Util.addVecsToVec2(sm.phyState.body.getPos(), -8, 64).scl(
						Util.PIXEL_TO_BOX), 0);
	}

	public void setChildState(NPCStateEnum state) {
		sm.setState(state);
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

	public void checkInLevel() {
		float x = sm.phyState.body.getX();
		if (sm.npc.level != GamePlayManager.currentlevel) {
			sm.setState(NPCStateEnum.INOTHERLEVEL);
			sm.state.setParent(this);
		} else if (x > GamePlayManager.levels.get(sm.npc.level)) {
			if (sm.npc.level != GamePlayManager.levels.size() - 1) {
				sm.npc.level += 1;
				sm.phyState.body.setX(0);
				sm.setState(NPCStateEnum.INOTHERLEVEL);
				sm.state.setParent(this);
			} else {
				sm.hitrightwall = true;
				sm.phyState.body.stopOnX();
			}
		} else if (x <= 0) {
			if (sm.npc.level != 0) {
				sm.npc.level -= 1;
				float activelevelwidth = GamePlayManager.levels
						.get(sm.npc.level);
				sm.phyState.body.setX(activelevelwidth);
				sm.setState(NPCStateEnum.INOTHERLEVEL);
				sm.state.setParent(this);
			} else {
				sm.hitleftwall = true;
				sm.phyState.body.stopOnX();
			}
		}
	}

	public void setIdle() {
		sm.setState(NPCStateEnum.IDLE);
	}

	public ActionEnum[] getPossibleActions() {
		return ActionEnum.values();
	}

	public void takeAction(Action action) {
		if (action != null) {
			sm.behavior.takeAction(action);
			sm.setState(action.getAIState());
		}
	}

	public void setWalk() {
		sm.getState(NPCStateEnum.WALKING).transistionIn();
		// sm.state.setParent(this);
	}

	public void setRun() {
		sm.getState(NPCStateEnum.RUNNING).transistionIn();
		// sm.state.setParent(this);
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
		if (sm.elevatedSegment != null
				&& sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
			float groundPoint = sm.elevatedSegment.getHeight(pos.x);
			sm.phyState.body.setPos(pos.x, groundPoint - 10, 0);
			// sm.setTestVal("grounded", true);
		}
	}

}
