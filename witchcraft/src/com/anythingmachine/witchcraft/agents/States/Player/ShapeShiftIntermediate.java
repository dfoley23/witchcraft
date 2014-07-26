package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.animations.SpriteAnimation;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ShapeShiftIntermediate extends PlayerState {
	private SpriteAnimation shift;
	private PlayerStateEnum end;

	public ShapeShiftIntermediate(PlayerStateMachine sm, PlayerStateEnum name,
			PlayerStateEnum end) {
		super(sm, name);
		this.end = end;
	}

	@Override
	public boolean canAnimate() {
		return false;
	}

	@Override
	public void update(float dt) {
		shift.update(dt);
		if (shift.atEnd()) {
			sm.setState(end);
			sm.state.setParent(sm.getState(PlayerStateEnum.IDLE));
		}
	}

	@Override
	public void draw(Batch batch) {

		shift.setFramePos(Util.addVecs(sm.phyState.body.getPos(), new Vector3(0, 48, 0)));
		shift.draw(batch);
	}

	@Override
	public void drawCape(Matrix4 cam) {
		
	}
	
	@Override
	public void transistionIn() {
		shift = new SpriteAnimation(10f / 60f, false);
		sm.phyState.body.setGravityVal(0);
		if (end == PlayerStateEnum.SHAPECROWPOWER) {
			shift.addFrame("data/world/otherart.atlas", "ss-1-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-2-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-3-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-4-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-5-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-6-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-7-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-8-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-9-crow", -0.4f);
			shift.addFrame("data/world/otherart.atlas", "ss-10-crow", -0.4f);
			shift.setPos(Util.addVecs(sm.phyState.body.getPos(), new Vector3(0, 48, 0)));
			sm.phyState.body.setVel(sm.facingleft ? -20 : 20, 50, 0);
		} else {

		}
	}

	@Override
	public void setInputSpeed() {
	}

	@Override
	public void land() {
	}

	@Override
	public void usePower() {
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
	public void transistionOut() {
		shift = null;
	}

	@Override
	public void checkGround() {
	}

}
