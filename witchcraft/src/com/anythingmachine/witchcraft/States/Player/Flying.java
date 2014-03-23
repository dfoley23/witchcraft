package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.anythingmachine.witchcraft.Util.Pointer;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.LevelWall;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

public class Flying extends Jumping {
	private float rotation;
	private float hitrooftimeout = 1f;
	private float time = 0f;

	public Flying(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public boolean canAnimate() {
		return false;
	}

	@Override
	public void transistionIn() {
		GamePlayManager.currentsound.stop();
		GamePlayManager.currentsound = (Sound) WitchCraft.assetManager.get("data/sounds/wind.wav");
		GamePlayManager.currentsound.loop(1);
	}

	@Override
	public void transistionOut() {
		GamePlayManager.currentsound.stop();
		GamePlayManager.currentsound = (Sound) WitchCraft.assetManager.get("data/sounds/crickets.ogg");
	}

	@Override
	public void update(float dt) {
		checkGround();

		setInputSpeed();

		usePower();

		if (WitchCraft.ON_ANDROID) {
			if (sm.facingleft) {
				sm.animate.rotate((-rotation * Util.RAD_TO_DEG) + 120);
			} else {
				sm.animate.rotate((rotation * Util.RAD_TO_DEG) + 120);
			}
			sm.phyState.body.addVel(0, Util.GRAVITY, 0);
		} else {
			sm.phyState.body.addVel(0, Util.GRAVITY, 0);
		}
		sm.phyState.correctCBody(-8, 32, sm.facingleft ? Util.HALF_PI: -Util.HALF_PI);

		sm.animate.setFlipX(sm.facingleft);

		if (sm.hitroof) {
			time += dt;
			if (time > hitrooftimeout) {
				sm.hitroof = false;
				time = 0;
			} 
			sm.phyState.body.setYVel(Util.GRAVITY);
		}

		Cape cape = GamePlayManager.player.cape;
		cape.addWindForce(-sm.phyState.body.getVelX(), -sm.phyState.body.getVelY());

		cape.updatePos(sm.neck.getWorldX() + 14, sm.neck.getWorldY());
	}

	@Override
	public void setInputSpeed() {
		rotation = -sm.input.axisDegree();
		int axisVal = sm.input.axisRange2();
		if (axisVal > 0 && !sm.hitrightwall) {
			sm.facingleft = sm.hitleftwall = false;
		} else if (axisVal < 0 && !sm.hitleftwall) {
			sm.facingleft = true;
			sm.hitrightwall = false;
		}
		if (sm.facingleft && !sm.hitleftwall) {
			if (rotation == 0)
				rotation = Util.PI;
			float x = -Util.PLAYERFLYSPEED;
			float y = (float) Math.sin(rotation) * Util.PLAYERFLYSPEED;
			sm.phyState.body.setVel(x, y, 0);
		} else if (!sm.facingleft && !sm.hitrightwall) {
			rotation += Util.PI;
			float x = Util.PLAYERFLYSPEED;
			float y = (float) -Math.sin(rotation) * Util.PLAYERFLYSPEED;
			sm.phyState.body.setVel(x, y, 0);
		} else {
			sm.phyState.body.stop();
		}

	}

	@Override
	public void land() {
		sm.animate.rotate(0);
		if (sm.facingleft) {
			sm.animate.setFlipX(true);
		}
		sm.setState(PlayerStateEnum.IDLE);
	}

	@Override
	public void usePower() {
		if (sm.input.is("UsePower")) {
			if (!sm.hitroof) {
				sm.phyState.body.setYVel(150f);
			} else {
				time += WitchCraft.dt;
				if (time > hitrooftimeout) {
					sm.hitroof = false;
					time = 0;
				}
			}
		}
	}

	@Override
	public boolean isHighAlertState() {
		return true;
	}

	@Override
	public void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.grounded = false;
		if (sm.hitplatform) {
			if (sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
				if (pos.y < groundPoint) {
					sm.phyState.body.setY(groundPoint);
					sm.state.land();
				}
			}
		}
	}
	
	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		Entity other;
		if (isFixture1) {
			other = (Entity) contact.getFixtureB().getBody().getUserData();
		} else {
			other = (Entity) contact.getFixtureA().getBody().getUserData();
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
			sm.phyState.body.stopOnX();
			if (sign == -1) {
				sm.hitleftwall = true;
			} else {
				sm.hitrightwall = true;
			}
			sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
			break;
		case PLATFORM:
			Platform plat = (Platform) other;
			if (plat.isBetween(sm.facingleft, pos.x)) {
				if (plat.getHeight() - 35 < pos.y) {
					sm.hitplatform = true;
					sm.elevatedSegment = plat;
					sm.state.land();
				} else {
					sm.phyState.body.stopOnY();
					sm.hitroof = true;
				}
			}
			break;
		case STAIRS:
			plat = (Platform) other;
			// System.out.println("hello staris");
			if (sm.input.is("UP")
					|| (plat.getHeight(pos.x) < (pos.y + 4) && plat
							.getHeight(pos.x) > plat.getHeightLocal() * 0.35f
							+ plat.getPos().y)) {
				// System.out.println("up stairs");
				if (plat.isBetween(sm.facingleft, pos.x)) {
					if (plat.getHeight(pos.x) - 12 < pos.y) {
						sm.hitplatform = true;
						sm.elevatedSegment = plat;
						sm.state.land();
					}
				}
			}
			break;
		case LEVELWALL:
			LevelWall wall = (LevelWall) other;
			GamePlayManager.switchLevel(wall.getLevel());
			break;
		case SWORD:
			npc = (NonPlayer) ((Pointer) other).obj;
			if (npc.isCritcalAttacking()) {
				sm.killedbehind = npc.getX() < sm.phyState.body.getX();
				sm.setState(PlayerStateEnum.DEAD);
				npc.switchBloodSword();
			}
			break;
		case ARROW:
			Arrow arrow = (Arrow) other;
			sm.killedbehind = arrow.getVelX() < 0 == sm.facingleft;
			sm.setState(PlayerStateEnum.ARROWDEAD);
			break;
		}
	}

	
}
