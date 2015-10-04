package com.anythingmachine.witchcraft.agents.States.NPC;

import java.util.Random;

import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.ground.LevelWall;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
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

		checkInBounds();

		takeAction(dt);

		fixCBody();

		updateSkel(dt);
	}

	public void updateSkel(float dt) {

		sm.animate.applyTotalTime(true, dt);

		sm.animate.setPos(sm.phyState.body.getPos(), 0, -12f);
		sm.animate.updateSkel(dt);
	}

	public void draw(Batch batch) {
		sm.animate.setFlipX(sm.facingleft);
		sm.animate.draw(batch);
	}

	public void checkInBounds() {
		sm.onscreen = true;
		if (!WitchCraft.cam.inscaledBounds(sm.phyState.body.getPos())) {
			sm.onscreen = false;
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

	public void checkAttack() {
		sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState.body.getX()
				&& sm.currentNode.getSet() == GamePlayManager.player.getAINode().getSet();
		if (sm.canseeplayer) {
			if (GamePlayManager.player.inAlert()) {
				setAttack();
			}
		}
	}

	public void hitStairs(Stairs stairs) {
		float ypos = sm.phyState.body.getY();
		float xpos = sm.phyState.body.getX();
		float lowestPoint = stairs.getDownPosY();
		float highestPoint = stairs.getUpPosY();
		boolean slantRight = stairs.slantRight();
		// up the stairs
		if (ypos + 16 > lowestPoint && ypos - 16 < lowestPoint
				&& ((slantRight && !sm.facingleft) || (!slantRight && sm.facingleft))
				&& (xpos > stairs.getDownPosX()) == sm.facingleft) {
			Random rand = new Random();
			int choice = rand.nextInt(4);
			if (choice > 2) {
				sm.elevatedSegment = stairs;
			}
		}
		// down the stairs
		else if (ypos + 16 > highestPoint && ypos - 16 < highestPoint
				&& ((slantRight && sm.facingleft) || (!slantRight && !sm.facingleft))) {
			if (!stairs.walkDown()) {
				Random rand = new Random();
				int choice = rand.nextInt(4);
				if (choice > 2) {
					sm.elevatedSegment = stairs;
					sm.phyState.body.setGravityVal(0);
				}
			} else {
				sm.elevatedSegment = stairs;
			}
		}
	}

	public void checkTarget() {

	}

	public void switchLevel(int level) {
		if (level - 1 > sm.me.level) {
			sm.phyState.body.setX(64);
			// sm.animate.updateSkel(0);
		} else {
			sm.phyState.body.setX(GamePlayManager.levels.get(level - 1) - 72);
		}
		sm.animate.setPos(sm.phyState.body.getPos(), 0, -12f);
		sm.animate.updateSkel(0);
		sm.me.level = level - 1;
		if (sm.me.level != GamePlayManager.currentlevel) {
			sm.setState(NPCStateEnum.INOTHERLEVEL);
			sm.state.setParent(this);
		}
	}

	public void fixCBody() {
		sm.phyState.correctCBody(0, 64, 0);
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
		if (this.parent != null) {
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
		if (!sm.phyState.body.isStable() && sm.elevatedSegment != null
				&& (sm.elevatedSegment.isBetween(sm.facingleft, pos.x))) {
			float groundPoint = sm.elevatedSegment.getHeight(pos.x);
			if (sm.phyState.body.getY() <= groundPoint + 7) {
				sm.phyState.body.setY(groundPoint);
			}
		}
		if ( pos.y < Util.BASEGROUNDY ) {
			sm.phyState.body.setY(Util.BASEGROUNDY);
		}
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
			sm.facingleft = !sm.facingleft;
			break;
		case AINODE:
			sm.currentNode = (AINode) other;
			break;
		case STAIRS:
			hitStairs((Stairs) other);
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

}
