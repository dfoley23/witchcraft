package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.physicsEngine.particleEngine.particles.Crow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ShapeCrow extends PlayerState {
	private Crow crow;
	private float time = 0;
	private float timeout = 14f;

	public ShapeCrow(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
		crow = new Crow(new Vector3(0, 0, 0), 0);
	}

	@Override
	public void transistionIn() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.phyState.body.setPos(pos.x-16, pos.y + 32);
		sm.phyState.body.setGravityVal(0);
		crow.setVel(sm.facingleft ? -Util.PLAYERFLYSPEED
				: Util.PLAYERFLYSPEED, 0, 0);
		sm.hitplatform = false;
		sm.elevatedSegment = null;
		sm.animate.updateSkel(0);
		crow.set3DPos(pos);
		crow.setStandTime(-1);
		crow.setFlipX(!sm.facingleft);
		sm.grounded = false;
		sm.hitroof = false;
	}

	@Override
	public boolean canAnimate() {
		return false;
	}

	@Override
	public void update(float dt) {
		checkGround();

		setInputSpeed();

		crow.update(dt);

		sm.phyState.body.set3DPos(crow.getPos());
		
		sm.phyState.correctCBody(32, 70, 0);

		updatePower(dt);
	}

	@Override
	public void draw(Batch batch) {
//		crow.set3DPos(sm.phyState.body.getPos());
		crow.draw(batch);
	}

	@Override
	public void drawCape(Matrix4 cam) {
	}

	@Override
	public void setInputSpeed() {
		if ((!sm.facingleft && sm.input.left())
				|| (sm.facingleft && sm.input.right())) {
			crow.flipXVel();
			sm.facingleft = !sm.facingleft;
			crow.setFlipX(!sm.facingleft);
		}
		if (sm.input.up() && !sm.hitroof) {
			crow.setVel(sm.facingleft ? -Util.PLAYERFLYSPEED : Util.PLAYERFLYSPEED, Util.PLAYERWALKSPEED, 0);
			sm.grounded = false;
			crow.setStand(false);
			sm.elevatedSegment = null;
			sm.hitplatform = false;
		} else if (sm.input.down() && !sm.grounded) {
			sm.hitroof = false;
			crow.setYVel(-Util.PLAYERWALKSPEED);
		}
	}

	@Override
	public void switchLevel(float x) {
		sm.hitleftwall = false;
		sm.hitrightwall = false;

		if ( x < 1000 ) 
			crow.setX(x);
		else
			crow.setX(x-128);
		
		sm.phyState.body.set3DPos(crow.getPos());		

		sm.phyState.correctCBody(32, 70, 0);
	}
	
	@Override
	public void land() {
//		crow.addPos(0, 8);
		sm.grounded = true;
		crow.setStand(true);
	}

	@Override
	protected void hitPlatform(Platform plat) {
//		if (plat.isBetween(sm.facingleft, sm.phyState.body.getX())) {
			if (plat.getHeight() - 64 < crow.getY()) {
				sm.hitplatform = true;
				sm.elevatedSegment = plat;
				land();
			} else {
				crow.stopOnY();
				sm.hitroof = true;
			}
//		}
	}

	@Override
	public void usePower() {
	}

	@Override
	public void setState(PlayerStateEnum newstate) {
		sm.setState(newstate);
	}

	@Override
	public void updatePower(float dt) {
		time += dt;
		if (time > timeout) {
			time = 0;
			sm.setState(PlayerStateEnum.FALLING);
		}
	}
	
	@Override
	public void nextPower() {

	}

	@Override
	public void setWalk() {

	}

	@Override
	public void setRun() {

	}

	@Override
	protected void hitWall(float sign) {
		crow.flipXVel();
		sm.facingleft = !sm.facingleft;
		crow.setFlipX(!sm.facingleft);
		sm.hitleftwall = sm.hitrightwall = false;
	}

	@Override
	public void transistionOut() {
		crow.setStandTime(-1);
		sm.phyState.body.setYVel(0);
		sm.phyState.body.setGravityVal(Util.GRAVITY * 4);
	}

	@Override
	public void checkGround() {
		if (crow.getStandTime() > 0) {
			land();
		}
	}

}
