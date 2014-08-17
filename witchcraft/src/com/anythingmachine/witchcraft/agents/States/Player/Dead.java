package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.animations.SpriteAnimation;
import com.anythingmachine.physicsEngine.particleEngine.particles.TexturedParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.player.items.Cape;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class Dead extends PlayerState {
	private float fadeout;
	private TexturedParticle head;
	private SpriteAnimation bloodpool;
	private boolean headhitground;
	private float caperotation;

	public Dead(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}

	@Override
	public void update(float dt) {
		sm.phyState.correctCBody(-8, 64, 0);

		bloodpool.update(dt);
		checkGround();
		if (sm.animate.testOverTime(0, .75f)) {
			fadeout -= dt;
		} else {
			addWindToCape(dt);
		}
		if (fadeout < -3) {
			super.setIdle();
		}

	}

	@Override
	public boolean canAnimate() {
		if (sm.animate.testOverTime(0, .75f)) {
			return false;
		}
		return true;
	}

	@Override
	public void draw(Batch batch) {
		bloodpool.draw(batch);
		sm.animate.draw(batch);
		head.draw(batch);
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
	public void addWindToCape(float dt) {
		Cape cape = GamePlayManager.player.cape;
		
		cape.addWindForce(-GamePlayManager.windx, -400);
		
		caperotation = ( sm.animate.getTime() / sm.animate.getCurrentAnimTime() );
		caperotation = sm.facingleft? caperotation*80: -caperotation*80;
		
		cape.rotate(caperotation);

		cape.updatePos(sm.facingleft? sm.neck.getWorldX() : sm.neck.getWorldX() -24, sm.neck.getWorldY()-8);
		
		cape.flip(sm.facingleft);
	}

	@Override
	protected void checkGround() {
		if (sm.hitplatform || sm.grounded) {
			Vector3 pos = sm.phyState.body.getPos();
			float groundPoint = sm.elevatedSegment.getHeight(pos.x);
			if (pos.y < groundPoint + 16)
				sm.phyState.body.setY(groundPoint);
			if (head.getY() < groundPoint + 4) {
				head.setY(groundPoint);
				head.setVel(0, 0, 0);
				if (!headhitground) {
					for (int i = 1; i < 13; i ++) {
						String img = "morph" + i;
						bloodpool.addFrame("data/world/otherart.atlas", img, -0.65f, false);
					}
					bloodpool.setPos(Util.addVecs(head.getPos(), new Vector3(36, 22, 0)));
					headhitground = true;
				}
			}

		}
	}

	@Override
	public void transistionIn() {
		sm.animate.bindPose();
		sm.phyState.body.stopOnX();
		sm.animate.setCurrent("ded", true);
		sm.animate.setRegion("hood", "hood", "witch-headstump", false, 1, 1);
		GamePlayManager.player.cape.addWindForce(0, -400);
		fadeout = 1;
		setParent(sm.getState(PlayerStateEnum.IDLE));
		Sprite sprite = WitchCraft.assetManager.get(
				"data/spine/characters.atlas", TextureAtlas.class)
				.createSprite("witch-cuthead");
		sprite.scale(-0.4f);
		head = new TexturedParticle(Util.addVecs(sm.phyState.body.getPos(),
				new Vector3(sm.facingleft ? 16 : -16, 64, 0)),
				EntityType.PARTICLE, sprite,
				new Vector3(0, Util.GRAVITY * 2, 0));
		head.setVel(sm.facingleft ? -60 : 60, 90, 0);
		GamePlayManager.rk4System.addParticle(head);
		headhitground = false;
		bloodpool = new SpriteAnimation(10f / 60f, false);
	}

	@Override
	public void transistionOut() {
		bloodpool = null;
		head.destroy();
		sm.animate.setRegion("hood", "hood", "witch-hood", false, 1, 1);
	}

}
