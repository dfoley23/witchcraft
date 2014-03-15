package com.anythingmachine.witchcraft.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.animations.SpriteAnimation;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class ArrowDead extends PlayerState {
	private float fadeout;
	private SpriteAnimation bloodpool;
	private boolean atEnd = false;

	public ArrowDead(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void update(float dt) {
		sm.phyState.correctCBody(-8, 64, 0);

		bloodpool.update(dt);
		checkGround();

		if (sm.animate.testOverTime(0, .75f)) {
			fadeout -= dt;
		}
		GamePlayManager.player.cape.updatePos(sm.neck.getWorldX() + 12,
				sm.neck.getWorldY() - 8);

		if (fadeout < -3) {
			super.setIdle();
		}

		addWindToCape(dt);
	}

	@Override
	public void draw(Batch batch) {
		bloodpool.draw(batch);
		sm.animate.draw(batch);
	}

	@Override
	public boolean canAnimate() {
		if (sm.animate.testOverTime(0, .75f)) {
			if ( bloodpool.getSize() == 0 ) 
				atEnd = true;
			return false;
		}
		return true;
	}

	@Override
	public void setInputSpeed() {

	}

	@Override
	public void switchPower() {

	}

	@Override
	public void usePower() {

	}

	@Override
	public void setFlying() {
	}

	@Override
	public void setRun() {
	}

	@Override
	public void setWalk() {
	}

	@Override
	public void setIdle() {

	}

	@Override
	protected void checkGround() {
		if (sm.hitplatform || sm.grounded) {
			Vector3 pos = sm.phyState.body.getPos();
			float groundPoint = sm.elevatedSegment.getHeight(pos.x);
			if (pos.y < groundPoint + 16) {
				sm.phyState.body.setY(groundPoint);
				if (atEnd) {
					for (int i = 1; i < 13; i++) {
						String img = "morph" + i;
						System.out.println(img);
						bloodpool.addFrame("data/world/otherart.atlas", img,
								-0.65f);
					}
					bloodpool.setPos(Util.addVecs(sm.phyState.body.getPos(),
							new Vector3(16, 7, 0)));
					atEnd = false;
				}
			}
		}
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.phyState.body.stopOnX();
		sm.animate.setCurrent("ded", true);
		sm.animate.setRegion("torso", "torso",
				sm.killedbehind ? "witch-arrowtorsoback"
						: "witch-arrowtorsofront", true, 148, 96);
		fadeout = 1;
		setParent(sm.getState(PlayerStateEnum.IDLE));
		bloodpool = new SpriteAnimation(10f / 60f, false);
		atEnd = false;
	}

	@Override
	public void transistionOut() {
		sm.animate.setRegion("torso", "torso", "witch-torso", true, 70, 96);
		bloodpool = null;
	}
}
