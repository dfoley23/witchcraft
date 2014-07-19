package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.ground.LevelWall;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

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

		checkGround();

		if (sm.me.npctype.canAttack())
			checkAttack();
		else
			checkInBounds();

		takeAction(dt);

		fixCBody();

		updateSkel(dt);
	}

	public void updateSkel(float dt) {
		float delta = Gdx.graphics.getDeltaTime();

		sm.animate.applyTotalTime(true, delta);

		sm.animate.setPos(sm.phyState.body.getPos(), -8f, 0f);
		sm.animate.updateSkel(dt);
	}

	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.facingleft);
		sm.animate.draw(batch);
	}

	public void checkInBounds() {
		if (sm.inlevel
				&& !WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}

	public void setCinematic() {
		sm.setState(NPCStateEnum.CINEMATIC);
	}
	
	public void setGoingTo(float dt) {

	}

	public void handleNPCContact(NonPlayer npc) {
		sm.hitnpc = true;
		// sm.npc = (NonPlayer) other;
	}

	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}

		Vector3 pos = sm.phyState.body.getPos();
		Vector3 vel = sm.phyState.body.getVel();
		switch (other.type) {
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(sm.facingleft, pos.x)) {
				if (plat.getHeight() - 32 < pos.y)
					sm.elevatedSegment = plat;
			}
			break;
		case WALL:
			if (vel.x < 0) {
				sm.phyState.body.setXVel(-vel.x);
				sm.hitleftwall = true;
			} else {
				sm.phyState.body.setXVel(-vel.x);
				sm.hitrightwall = true;
			}
			break;
		case LEVELWALL:
			LevelWall wall = (LevelWall) other;
			sm.state.switchLevel(wall.getLevel());
			break;
		case NONPLAYER:
			handleNPCContact((NonPlayer) other);
			break;
		}
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
		if (level - 1 > sm.me.level) {
			sm.phyState.body.setX(64);
		} else {
			sm.phyState.body.setX(GamePlayManager.levels.get(level - 1) - 64);
		}
		sm.me.level = level - 1;
		if (sm.me.level != GamePlayManager.currentlevel) {
			sm.setState(NPCStateEnum.INOTHERLEVEL);
			sm.state.setParent(this);
		}
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
		if (GamePlayManager.currentlevel != sm.me.level) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INOTHERLEVEL);
			sm.state.setParent(temp);
		}
	}

	public void transistionToParent() {
		if ( this.parent != null ) {
			this.sm.setState(parent.name);
		} else {
			sm.setState(NPCStateEnum.IDLE);
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
