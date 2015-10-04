package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.ground.Door;
import com.anythingmachine.collisionEngine.ground.LevelWall;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Pointer;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.agents.player.items.InventoryObject;
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
			this.sm.setState(parent.name);
		} else {
			sm.setState(PlayerStateEnum.IDLE);
		}
	}

	public void update(float dt) {
		checkGround();

		checkInteract();

		setInputSpeed();

		switchPower();

		usePower();

		updatePower(dt);

		sm.phyState.correctCBody(0, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		addWindToCape(dt);
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
		if (sm.input.is("Interact")) {
			Gdx.app.debug("open door", ""+sm.currentDoor.getToLevel());
			if (sm.currentDoor != null) {
				GamePlayManager.level = sm.currentDoor.getToLevel();
			}
		}
	}

	public void setFlying() {
	}

	public boolean canAnimate() {
		return true;
	}

	public void setIdle() {
		sm.setState(parent.name);
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
		sm.setState(PlayerStateEnum.WALKING);
		sm.state.setParent(this);
	}

	public void setRun() {
		sm.setState(PlayerStateEnum.RUNNING);
		sm.state.setParent(this);
	}

	public void setInputSpeed() {
		int axisVal = sm.input.axisRange2();
		if (axisVal > 0) {
			sm.facingleft = false;
			if (!sm.hitrightwall) {
				sm.hitleftwall = false;
				if (axisVal > 1) {
					setRun();
					sm.phyState.body.setXVel(Util.DEV_MODE ? Util.DEBUGSPED : Util.PLAYERRUNSPEED);
				} else {
					setWalk();
					sm.phyState.body.setXVel(Util.PLAYERWALKSPEED);
				}
			}
		} else if (axisVal < 0) {
			sm.facingleft = true;
			if (!sm.hitleftwall) {
				sm.hitrightwall = false;
				if (axisVal < -1) {
					setRun();
					sm.phyState.body.setXVel(Util.DEV_MODE ? -Util.DEBUGSPED : -Util.PLAYERRUNSPEED);
				} else {
					setWalk();
					sm.phyState.body.setXVel(-Util.PLAYERWALKSPEED);
				}
			}
		} else {
			setIdle();
		}
	}

	protected void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.grounded = false;
		float groundPoint = sm.currentPlatform.getHeight(pos.x);
		Vector2 stairsStart = sm.currentPlatform.getStartPos();
		Vector2 stairsEnd = sm.currentPlatform.getEndPos();
		if (sm.currentPlatform.isStairs()
				&& ((pos.x + 8 > stairsStart.x && sm.facingleft) || (pos.x - 8 < stairsEnd.x && !sm.facingleft))) {
			sm.phyState.body.setY(groundPoint);
			sm.currentStairs = sm.currentPlatform;
			sm.state.land();
		} else if (!sm.currentPlatform.isStairs() && sm.hitplatform
				&& sm.currentPlatform.isBetween(sm.facingleft, pos.x)) {
			sm.phyState.body.setY(groundPoint);
			sm.state.land();
			sm.currentStairs = null;
		} else {
			sm.hitplatform = false;
			sm.currentStairs = null;
			sm.hitstairs = false;
			setFalling();
		}
	}

	protected void setFalling() {
		if (!sm.hitplatform && !sm.hitstairs) {
			setState(PlayerStateEnum.FALLING);
		}
	}

	protected void setDead() {
		sm.setState(PlayerStateEnum.DEAD);
	}

	public void setState(PlayerStateEnum newstate) {
		sm.setState(newstate);
		sm.state.setParent(this);
	}

	protected void hitWall(float sign) {
		sm.phyState.body.stopOnX();
		sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
	}

	protected void hitPlatform(Platform plat) {
		float platHeight = plat.getHeight();
		// platform is below player
		if (platHeight - 32 < sm.phyState.body.getY()) {
			// if not hitting stairs or hitting both platform and stairs
			// but input is walking down
			if (sm.currentStairs == null || ( platHeight < sm.phyState.body.getY() ) ) {
				sm.hitstairs = false;
				sm.hitplatform = true;
				sm.currentPlatform = plat;
				sm.currentStairs = null;
				land();
			}
		} else if (!sm.currentPlatform.isStairs()) {
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
		if (sm.input.up() && lowestPoint - 16 < pos.y && ((pos.x - 16 < stairsStart.x && !sm.facingleft && slantRight)
				|| (pos.x + 16 > stairsEnd.x && sm.facingleft && !slantRight))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
		}
		// walk down stairs
		else if (highestPoint - 7 < pos.y && (sm.input.down() || stairs.walkDown())
				&& ((pos.x - 16 < stairsEnd.x && slantRight && sm.facingleft)
						|| (pos.x + 16 > stairsStart.x && !slantRight && !sm.facingleft))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
		}
	}

	public void switchLevel(float x) {
		sm.hitleftwall = false;
		sm.hitrightwall = false;
		sm.phyState.body.setX(x);
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
			} else {
				sm.hitrightwall = true;
			}
			hitWall(sign);
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			hitPlatform(plat);
			break;
		case STAIRS:
			Stairs stairs = (Stairs) other;
			hitStairs(stairs);
			break;
		case DOOR:
			Door door = (Door) other;
			sm.currentDoor = door;
			System.out.println(door);
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
		// Entity other;
		// if (isFixture1) {
		// other = (Entity) contact.getFixtureB().getBody().getUserData();
		// } else {
		// other = (Entity) contact.getFixtureA().getBody().getUserData();
		// }
		// switch (other.type) {
		// case STAIRS:
		// sm.hitstairs = false;
		// setFalling();
		// break;
		// default:
		// break;
		// }
	}

}
