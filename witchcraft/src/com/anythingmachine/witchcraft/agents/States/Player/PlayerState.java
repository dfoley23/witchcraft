package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Pointer;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.anythingmachine.witchcraft.ground.LevelWall;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;

public class PlayerState {
	protected PlayerStateMachine sm;
	public PlayerStateEnum name;
	public PlayerState parent;
	public float soundtimeout = 2;

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
		if ( this.parent != null ) {
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

		int windx = 0;		
		if ( sm.windtimeout > 0 ) {
			windx = sm.rand.nextInt(1500);
			if ( windx > 720 ) {
//				if ( windx > 1480 && soundtimeout < 1f) {
////					GamePlayManager.currentsound.stop();
////					GamePlayManager.currentsound = (Sound) WitchCraft.assetManager.get("data/sounds/wind.wav");
////					GamePlayManager.currentsound.play(0.5f);
//				    byte[] buf = new byte[ 1 ];;
//				    AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
//				    try { 
//				    SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
//				    sdl = AudioSystem.getSourceDataLine( af );
//				    sdl.open( af );
//				    sdl.start();
//				    for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
//				        double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
//				        buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
//				        sdl.write( buf, 0, 1 );
//				    }
//				    sdl.drain();
//				    sdl.stop();
//				    sdl.close();
//				    } catch(Exception e) {
//				    	
//				    }
//				}
//				windx = 0;
//			} else {
//				if ( GamePlayManager.currentlevel < 2 && soundtimeout < 0) {
////					GamePlayManager.currentsound.stop();
////					GamePlayManager.currentsound = (Sound) WitchCraft.assetManager.get("data/sounds/crickets.ogg");
////					GamePlayManager.currentsound.play(0.5f);
//				    byte[] buf = new byte[ 1 ];;
//				    AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
//				    try { 
//				    SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
//				    sdl = AudioSystem.getSourceDataLine( af );
//				    sdl.open( af );
//				    sdl.start();
//				    for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
//				        double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
//				        buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
//				        sdl.write( buf, 0, 1 );
//				    }
//				    sdl.drain();
//				    sdl.stop();
//				    sdl.close();
//				    } catch(Exception e) {
//				    	
//				    }
//
//					soundtimeout = 2.5f;
//				} else {
//					soundtimeout-=dt;
//				}
			}
		} else if ( sm.windtimeout < -1 ) {
			sm.windtimeout = 1.5f;
		}
		sm.windtimeout-=dt;
		
		cape.addWindForce(sm.facingleft ? windx : -windx, -400);

		cape.updatePos(sm.neck.getWorldX() + 12, sm.neck.getWorldY()-8);
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
				|| (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower1") )
				|| (WitchCraft.ON_ANDROID && sm.input.isNowNotThen("SwitchPower2") ) ) {
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
		if ( sm.power == PlayerStateEnum.DUPESKINPOWER )
			sm.dupeSkin = npc.getSkin().getName();
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
					sm.phyState.body.setXVel(Util.PLAYERRUNSPEED);
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
					sm.phyState.body.setXVel(-Util.PLAYERRUNSPEED);
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
		if (sm.hitplatform) {
			// System.out.println(pos);
//			if (pos.x > sm.curCurve.lastPointOnCurve().x
//					&& sm.curGroundSegment + 1 < WitchCraft.ground
//							.getNumCurves()) {
//				sm.curGroundSegment++;
//				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
//			} else if (pos.x < sm.curCurve.firstPointOnCurve().x
//					&& sm.curGroundSegment - 1 >= 0) {
//				sm.curGroundSegment--;
//				sm.curCurve = WitchCraft.ground.getCurve(sm.curGroundSegment);
//			}
//			Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
//					sm.curGroundSegment, pos.x);
//			sm.setTestVal("grounded", false);
////			if (pos.y <= groundPoint.y) {
//				sm.phyState.correctHeight(groundPoint.y);
//				sm.state.land();
////			}
//		} else {
			sm.grounded = false;
			if (sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
				float groundPoint = sm.elevatedSegment.getHeight(pos.x);
//				if (pos.y < groundPoint) {
					sm.phyState.body.setY(groundPoint);
					sm.state.land();
//				}
			} else {
				Gdx.app.log("falling off platform", ""+sm.elevatedSegment);
				sm.hitplatform = false;
				sm.setState(PlayerStateEnum.FALLING);
				sm.state.setParent(sm.getState(PlayerStateEnum.IDLE));
			}
		}
	}
	
	protected void hitWall(float sign) {
		sm.phyState.body.stopOnX();
		sm.phyState.body.setX(sm.phyState.body.getX() - (sign * 16));
	}
	
	protected void hitPlatform(Platform plat) {
		if (plat.isBetween(sm.facingleft, sm.phyState.body.getX())) {
			if (plat.getHeight() - 35 < sm.phyState.body.getY()) {
				sm.hitplatform = true;
				sm.elevatedSegment = plat;
				sm.state.land();
			} else {
				sm.phyState.body.stopOnY();
				sm.hitroof = true;
			}
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
			plat = (Platform) other;
			// System.out.println("hello staris");
			if (sm.input.is("UP")
					|| (plat.getHeight(pos.x) < (pos.y + 4) && plat
							.getHeight(pos.x) > plat.getHeightLocal() * 0.35f
							+ plat.getPos().y)) {
				// System.out.println("up stairs");
				if (plat.isBetween(sm.facingleft, pos.x)) {
					if (plat.getHeight(pos.x) - 35 < pos.y) {
						sm.hitplatform = true;
						sm.elevatedSegment = plat;
						sm.state.checkGround();
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
