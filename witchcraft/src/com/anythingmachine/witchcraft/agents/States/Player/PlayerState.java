package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

public class PlayerState {
	protected PlayerStateMachine sm;
	public PlayerStateEnum name;
	public PlayerState parent;

	public PlayerState(PlayerStateMachine sm,
	PlayerStateEnum name) {
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

		setInputSpeed();

		switchPower();

		usePower();

		updatePower(dt);

		sm.phyState.correctCBody(-8, 64, 0);

		sm.animate.setFlipX(sm.facingleft);

		addWindToCape(dt);
	}

	public void addWindToCape(float dt) {
		Cape cape = GamePlayManager.player.cape;

		cape.addWindForce(-GamePlayManager.windx, -400);

		cape.rotate(0);

		cape
		.updatePos(sm.facingleft
		? sm.neck.getWorldX() - 12
		: sm.neck.getWorldX() + 12, sm.neck.getWorldY() - 8);

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
		if (sm.input.isNowNotThen("SwitchPower")
		|| (WitchCraft.ON_ANDROID && sm.input
		.isNowNotThen("SwitchPower1"))
		|| (WitchCraft.ON_ANDROID && sm.input
		.isNowNotThen("SwitchPower2"))) {
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
					sm.phyState.body.setXVel(Util.DEV_MODE
					? Util.DEBUGSPED
					: Util.PLAYERRUNSPEED);
				} else {
					setWalk();
					sm.phyState.body
					.setXVel(Util.PLAYERWALKSPEED);
				}
			}
		} else if (axisVal < 0) {
			sm.facingleft = true;
			if (!sm.hitleftwall) {
				sm.hitrightwall = false;
				if (axisVal < -1) {
					setRun();
					sm.phyState.body.setXVel(Util.DEV_MODE
					? -Util.DEBUGSPED
					: -Util.PLAYERRUNSPEED);
				} else {
					setWalk();
					sm.phyState.body
					.setXVel(-Util.PLAYERWALKSPEED);
				}
			}
		} else {
			setIdle();
		}
	}

	protected void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		if (sm.hitplatform || sm.hitstairs) {
			// System.out.println(pos);
			// if (pos.x > sm.curCurve.lastPointOnCurve().x
			// && sm.curGroundSegment + 1 < WitchCraft.ground
			// .getNumCurves()) {
			// sm.curGroundSegment++;
			// sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
			// } else if (pos.x < sm.curCurve.firstPointOnCurve().x
			// && sm.curGroundSegment - 1 >= 0) {
			// sm.curGroundSegment--;
			// sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
			// }
			// Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
			// sm.curGroundSegment, pos.x);
			// sm.setTestVal("grounded", false);
			// // if (pos.y <= groundPoint.y) {
			// sm.phyState.correctHeight(groundPoint.y);
			// sm.state.land();
			// // }
			// } else {
			sm.grounded = false;
			float groundPoint =
			sm.elevatedSegment.getHeight(pos.x);
			if (sm.elevatedSegment.isStairs()) {
				sm.phyState.body.setX(sm.elevatedSegment
				.getXPos(groundPoint));
				sm.phyState.body.setY(groundPoint);
				sm.state.land();
			} else if (sm.elevatedSegment.isBetween(
				sm.facingleft, pos.x)) {
				sm.phyState.body.setY(groundPoint);
				sm.state.land();
			} else {
				sm.hitplatform = false;
				sm.hitstairs = false;
				setFalling();
			}
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
		sm.phyState.body.setX(sm.phyState.body.getX()
		- (sign * 16));
	}

	protected void hitPlatform(Platform plat) {
		float posx = sm.phyState.body.getX();
		// if (sm.elevatedSegment == null || !sm.elevatedSegment.isStairs()
		// || sm.elevatedSegment.slantRight() != sm.facingleft
		// || sm.elevatedSegment.getUpPos() < posx != sm.facingleft
		// || !sm.input.is("down"))
		{
			// if (plat.isBetween(sm.facingleft, posx)) {
			if (plat.getHeight() - 16 < sm.phyState.body
			.getY()) {
				sm.hitstairs = false;
				sm.hitplatform = true;
				sm.elevatedSegment = plat;
				land();
			} else if (!sm.elevatedSegment.isStairs()) {
				sm.phyState.body.stopOnY();
				sm.hitroof = true;
			}
		}
		// }
	}

	public void switchLevel(float x) {
		sm.hitleftwall = false;
		sm.hitrightwall = false;
		sm.phyState.body.setX(x);
		sm.phyState.correctCBody(-8, 64, 0);
	}

	public void handleContact(Contact contact,
	boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other =
			(Entity) contact
			.getFixtureB()
			.getBody()
			.getUserData();
		} else {
			other =
			(Entity) contact
			.getFixtureA()
			.getBody()
			.getUserData();
		}
		Vector3 pos = sm.phyState.body.getPos();
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
			if ((sm.input.up()
			&& stairs.getDownPos() < pos.x == sm.facingleft && (sm.facingleft == !stairs
			.slantRight()))
			|| ((stairs.getHeight() < pos.y)
			&& stairs.slantRight() == sm.facingleft && (sm.input
			.down() || stairs.walkDown()))) {
				// System.out.println("up stairs");
				// if (stairs.isBetween(sm.facingleft, pos.x)) {
				// if (plat.getHeight(pos.x) - 35 < pos.y)
				// {
				sm.hitstairs = true;
				sm.hitplatform = false;
				sm.elevatedSegment = stairs;
				// }
				// }
			}
			break;
		case LEVELWALL:
			LevelWall wall = (LevelWall) other;
			GamePlayManager.level = wall.getLevel();
			break;
		case AINODE:
		    sm.currentNode = (AINode)other;
		    break;
		case ACTIONWALL:
			// if( sm.input.is("ACTION") ) { }
			// ActionWall door = (ActionWall) other;
			// GamePlayManager.level = wall.getLevel();
			break;
		case SWORD:
			npc = (NonPlayer) ((Pointer) other).obj;
			if (npc.isCritcalAttacking()) {
				sm.killedbehind =
				npc.getX() < sm.phyState.body.getX();
				setDead();
				npc.switchBloodSword();
			}
			break;
		case ARROW:
			Arrow arrow = (Arrow) other;
			sm.killedbehind =
			arrow.getVelX() < 0 == sm.facingleft;
			setState(PlayerStateEnum.ARROWDEAD);
			break;
		}
	}

	public void endContact(Contact contact,
	boolean isFixture1) {
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
