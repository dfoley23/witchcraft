package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Pointer;
import com.anythingmachine.Util.Util;
import com.anythingmachine.agents.npcs.NonPlayer;
import com.anythingmachine.agents.player.items.Cape;
import com.anythingmachine.agents.player.items.InventoryObject;
import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.ground.Door;
import com.anythingmachine.collisionEngine.ground.LevelWall;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.StairTrigger;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

public class PlayerState {
	protected PlayerStateMachine sm;
	public PlayerStateEnum name;
	public PlayerState parent;

	public PlayerState(PlayerStateMachine sm, PlayerStateEnum name) {
		this.sm = sm;
		this.name = name;
		parent = this;
	}

	public void nextPower() {
		sm.nextPower();
	}

	public boolean isAlertState() {
		return false;
	}

	public boolean isHighAlertState() {
		return false;
	}

	public void setParent(PlayerState parent) {
		this.parent = parent;
	}

	public void transistionToParent() {
		if (this.parent != null) {
			this.sm.state.setState(parent.name);
		} else {
			sm.state.setState(PlayerStateEnum.IDLE);
		}
	}

	public void update(float dt) {
		if (!sm.hitstairs) {
			sm.state.checkGround();

			sm.state.checkInteract();

			sm.state.setInputSpeed();

			sm.state.switchPower();

			sm.state.usePower();

			sm.state.updatePower(dt);
		} else {
			sm.setState(PlayerStateEnum.ONSTAIRS);
		}

		sm.hitplatform = false;
		sm.hitstairs = false;

		sm.phyState.correctCBody(0, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		sm.state.addWindToCape(dt);
	}

	public void addWindToCape(float dt) {
		Cape cape = GamePlayManager.player.cape;

		cape.addWindForce(-GamePlayManager.windx, -400);

		cape.rotate(0);

		cape.updatePos(sm.facingleft ? sm.neck.getWorldX() - 12 : sm.neck.getWorldX() + 12, sm.neck.getWorldY() - 8);

		cape.flip(sm.facingleft);
	}

	public void setAttack() {
	}

	public void draw(Batch batch) {
		sm.animate.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
		GamePlayManager.player.cape.draw(cam, 1f);
	}

	public void switchPower() {
		if (sm.input.isNowNotThen("SwitchPower") || (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower1"))
				|| (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower2"))) {
			nextPower();
			sm.uiFadein = 0f;
		}
	}

	public void usePower() {
		if (sm.input.is("UsePower")) {
			sm.usePower();
			sm.state.setParent(this);
		}
	}

	public void updatePower(float dt) {

	}

	public void checkInteract() {
		if (sm.input.isNowNotThen("Interact")) {
			if (sm.currentDoor != null) {
				GamePlayManager.level = sm.currentDoor.getToLevel();
				GamePlayManager.exitDoorPos = sm.currentDoor.getDoorExitPos();
			}
		}
	}

	public void setFlying() {
	}

	public boolean canAnimate() {
		return true;
	}

	public void setIdle() {
		sm.setState(PlayerStateEnum.IDLE);
	}

	public void hitNPC(NonPlayer npc) {
		sm.npc = npc;
	}

	public void land() {
		sm.grounded = true;
		sm.phyState.body.stop();
	}

	public void transistionIn() {

	}

	public void transistionOut() {

	}

	public void setDupeSkin() {
	}

	public void setWalk() {
		sm.state.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(this);
	}

	public void setRun() {
		sm.state.setState(PlayerStateEnum.RUNNING);
		sm.state.setParent(this);
	}

	public void setInputSpeed() {
		// int axisVal = sm.input.axisRange2();
		if (sm.input.isNowNotThen("Right")) {
			sm.facingleft = false;
			if (!sm.hitrightwall) {
				sm.hitleftwall = false;
				setWalk();
				sm.phyState.body.setXVel(Util.PLAYERWALKSPEED);
			}
		} else if (sm.input.isNowNotThen("Left")) {
			sm.facingleft = true;
			if (!sm.hitleftwall) {
				sm.hitrightwall = false;
				setWalk();
				sm.phyState.body.setXVel(-Util.PLAYERWALKSPEED);
			}
		} else {
			setIdle();
		}
	}

	protected void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.grounded = false;
		float groundPoint = sm.currentPlatform == null ? pos.y : sm.currentPlatform.getHeight(pos.x);
		if (groundPoint >= GamePlayManager.lowestPlatInLevel.getDownPosY()) {
			if (sm.currentPlatform != null && sm.currentPlatform.isStairs()
					&& ((pos.x + 8 > sm.currentPlatform.getStartPos().x && sm.facingleft)
							|| (pos.x - 8 < sm.currentPlatform.getEndPos().x && !sm.facingleft))) {
				sm.phyState.body.setY(groundPoint);
				sm.currentStairs = sm.currentPlatform;
				sm.hitstairs = true;
				sm.state.land();
				// sm.state.setState(PlayerStateEnum.ONSTAIRS);
			} else if (sm.currentPlatform != null && !sm.currentPlatform.isStairs()
					&& sm.currentPlatform.isBetween(sm.facingleft, pos.x)) {
				sm.hitplatform = true;
				sm.phyState.body.setY(groundPoint);
				sm.state.land();
			} else {
				sm.phyState.body.setY(groundPoint);
				sm.state.land();
				setFalling();
			}
			if (sm.phyState.body.getY() < GamePlayManager.lowestPlatInLevel.getDownPosY()) {
				sm.currentPlatform = GamePlayManager.lowestPlatInLevel;
				sm.phyState.body.setY(sm.currentPlatform.getDownPosY());
				sm.hitplatform = true;
			}
		} else {
			sm.currentPlatform = GamePlayManager.lowestPlatInLevel;
			sm.phyState.body.setY(sm.currentPlatform.getDownPosY());
			sm.hitplatform = true;
		}
	}

	protected void setFalling() {
		if (!sm.hitplatform && !sm.hitstairs) {
			sm.state.setState(PlayerStateEnum.FALLING);
		}
	}

	protected void setDead() {
		sm.setState(PlayerStateEnum.DEAD);
	}

	public void setState(PlayerStateEnum newstate) {
		if (this.name == sm.state.name) {
			sm.setState(newstate);
			sm.state.setParent(this);
		} else {
			sm.state.setState(newstate);
			sm.state.setParent(this);
		}
	}

	protected void hitWall(float sign) {
		sm.phyState.body.stopOnX();
		sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
	}

	protected void hitPlatform(Platform plat) {
		float platHeight = plat.getHeight();
		float posy = sm.phyState.body.getY();
		// platform is below player
		if (platHeight - 32 < posy) {
			// if not hitting stairs or hitting both platform and stairs
			// but input is walking down
			if (sm.currentStairs == null || (platHeight + 4 > sm.currentStairs.getUpPosY() && !sm.input.is("down"))
					|| (posy - 8 < sm.currentStairs.getDownPosY() && sm.currentStairs.slantRight() == sm.facingleft)
					|| (posy + 8 > sm.currentStairs.getUpPosY() && sm.currentStairs.slantRight() != sm.facingleft)
					|| (!sm.input.is("down") && !sm.input.is("UP"))) {
				sm.hitplatform = true;
				sm.currentPlatform = plat;
				land();
			}
		} else if (sm.currentPlatform != null && !sm.currentPlatform.isStairs()) {
			sm.phyState.body.stopOnY();
			sm.hitroof = true;
		}
	}

	protected void hitStairs(Stairs stairs) {
		Vector3 pos = sm.phyState.body.getPos();
		Vector2 stairsStart = stairs.getStartPos();
		Vector2 stairsEnd = stairs.getEndPos();
		float lowestPoint = stairs.getDownPosY();
		float highestPoint = stairs.getUpPosY();
		boolean slantRight = stairs.slantRight();
		// walk up stairs
		if (sm.input.up() && pos.y - 8 < lowestPoint && pos.y + 8 > lowestPoint
				&& ((pos.x - 16 < stairsStart.x && !sm.facingleft && slantRight)
						|| (pos.x + 16 > stairsEnd.x && sm.facingleft && !slantRight))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
			// sm.setState(PlayerStateEnum.ONSTAIRS);
		}
		// walk down stairs
		else if (pos.y + 16 > highestPoint && (sm.input.down() || stairs.forceWalkDown())
				&& ((pos.x - 8 < stairsEnd.x && slantRight && sm.facingleft)
						|| (pos.x + 16 > stairsStart.x && !slantRight && !sm.facingleft))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
			// sm.setState(PlayerStateEnum.ONSTAIRS);
		}
	}

	protected void upStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// walk up stairs
		if (sm.input.up() && ((!sm.facingleft && slantRight) || (sm.facingleft && !slantRight))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
			// sm.state.setState(PlayerStateEnum.ONSTAIRS);
		} else if ((sm.facingleft && slantRight) || (!sm.facingleft && !slantRight)) {
			sm.hitstairs = false;
			sm.currentStairs = null;
			sm.phyState.body.setY(stairs.getDownPosY());
		}
	}

	protected void downStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// down stairs
		if ((sm.input.down() || stairs.forceWalkDown())
				&& ((slantRight && sm.facingleft) || (!slantRight && !sm.facingleft))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
			// sm.state.setState(PlayerStateEnum.ONSTAIRS);
		} else {

		}
	}

	public void switchLevel(Vector2 newPos) {
		sm.hitleftwall = false;
		sm.hitrightwall = false;
		sm.phyState.body.setX(newPos.x);
		sm.phyState.body.setY(newPos.y);
		sm.currentDoor = null;
		sm.phyState.correctCBody(0, 64, 0);
	}

	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		Vector3 vel = sm.phyState.body.getVel();
		float sign;
		switch (other.type) {
		case NONPLAYER:
			NonPlayer npc = (NonPlayer) other;
			sm.state.hitNPC(npc);
			break;
		case WALL:
			sign = Math.signum(vel.x);
			if (sign == -1) {
				sm.hitleftwall = true;
			} else if (sign == 1) {
				sm.hitrightwall = true;
			}
			hitWall(sign);
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			hitPlatform(plat);
			break;
		case STAIRUPTRIGGER:
			StairTrigger stairDT = (StairTrigger) other;
			upStairs(stairDT.getStairs());
			break;
		case STAIRDOWNTRIGGER:
			StairTrigger stairTT = (StairTrigger) other;
			downStairs(stairTT.getStairs());
			break;
		case STAIRS:
			Stairs stairs = (Stairs) other;
			hitStairs(stairs);
			break;
		case DOOR:
			Door door = (Door) other;
			sm.currentDoor = door;
			break;
		case LEVELWALL:
			LevelWall wall = (LevelWall) other;
			GamePlayManager.level = wall.getLevel();
			break;
		case AINODE:
			sm.currentNode = (AINode) other;
			break;
		case ACTIONWALL:
			// if( sm.input.is("ACTION") ) { }
			// ActionWall door = (ActionWall) other;
			// GamePlayManager.level = wall.getLevel();
			break;
		case INVENTORYOBJECT:
			GamePlayManager.player.addToInventory((InventoryObject) other);
			break;
		case SWORD:
			npc = (NonPlayer) ((Pointer) other).obj;
			if (npc.isCritcalAttacking()) {
				sm.killedbehind = npc.getX() < sm.phyState.body.getX();
				setDead();
				npc.switchBloodSword();
			}
			break;
		case ARROW:
			Arrow arrow = (Arrow) other;
			sm.killedbehind = arrow.getVelX() < 0 == sm.facingleft;
			setState(PlayerStateEnum.ARROWDEAD);
			break;
		}
	}

	public void endContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
		}
		switch (other.type) {
		case NONPLAYER:
			break;
		case WALL:
			break;
		case PLATFORM:
			break;
		case STAIRUPTRIGGER:
			break;
		case STAIRS:
			break;
		case DOOR:
			sm.currentDoor = null;
			break;
		case LEVELWALL:
			break;
		case AINODE:
			break;
		case ACTIONWALL:
			break;
		case INVENTORYOBJECT:
			GamePlayManager.player.addToInventory((InventoryObject) other);
			break;
		case SWORD:
			break;
		case ARROW:
			break;
		}
	}

}
