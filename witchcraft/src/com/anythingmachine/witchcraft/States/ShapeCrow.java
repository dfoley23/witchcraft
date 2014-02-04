package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.ParticleEngine.Crow;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ShapeCrow extends State {
	private Crow crow;
	private float time = 0;
	private float timeout = 7f;

	public ShapeCrow(StateMachine sm, StateEnum name) {
		super(sm, name);
		crow = new Crow(new Vector3(0, 0, 0), 0);
	}

	@Override
	public void transistionIn() {
		Vector3 pos = sm.phyState.getPos();
		sm.phyState.body.setPos(pos.x, pos.y + 128, 0);
		sm.phyState.body.setGravityVal(0);
		crow.setPos(pos);
		crow.setFlipX(!sm.test("facingleft"));
	}

	@Override
	public void transistionOut() {
		sm.phyState.body.setGravityVal(Util.GRAVITY * 3);
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

		crow.setFlipX(!sm.test("facingleft"));

		sm.phyState.correctCBody(-8, 128, 0);

		time += dt;
		if (time > timeout) {
			time = 0;
			sm.setState(StateEnum.FALLING);
		}
	}

	public void draw(Batch batch) {
		crow.setPos(sm.phyState.getPos());
		crow.draw(batch);
	}

	public void drawCape(Matrix4 cam) {
	}

	@Override
	public void setInputSpeed() {
		Vector3 vel = sm.phyState.body.getVel();
		if (sm.input.right() && !sm.test("hitrightwall")) {
			sm.setTestVal("facingleft", false);
			sm.setTestVal("hitleftwall", false);
			if (sm.input.up() && !sm.test("hitroof")) {
				sm.phyState.body.setVel(Util.PLAYERRUNSPEED,
						Util.PLAYERWALKSPEED, 0);
				sm.setTestVal("grounded", false);
			} else if (sm.input.down() && !sm.test("grounded")) {
				sm.setTestVal("hitrooft", false);
				sm.phyState.body.setVel(Util.PLAYERRUNSPEED,
						-Util.PLAYERWALKSPEED, 0);
			} else {
				sm.phyState.body.setVel(Util.PLAYERRUNSPEED, vel.y, 0);
			}
		} else if (sm.input.left() && !sm.test("hitleftwall")) {
			sm.setTestVal("facingleft", true);
			sm.setTestVal("hitrightwall", false);
			if (sm.input.up() && !sm.test("hitroof")) {
				sm.setTestVal("grounded", false);
				sm.phyState.body.setVel(-Util.PLAYERRUNSPEED,
						Util.PLAYERWALKSPEED, 0);
			} else if (sm.input.down() && !sm.test("grounded")) {
				sm.setTestVal("hitrooft", false);
				sm.phyState.body.setVel(-Util.PLAYERRUNSPEED,
						-Util.PLAYERWALKSPEED, 0);
			} else {
				sm.phyState.body.setVel(-Util.PLAYERRUNSPEED, vel.y, 0);
			}
		} else {
			sm.phyState.stop();
		}
	}

	@Override
	public void land() {
		sm.phyState.stop();
		crow.setStandTime(6);
	}

	@Override
	public void usePower() {
	}

	@Override
	public void checkGround() {
		if ( crow.getStandTime() > 0 ) {
			land();
			sm.setTestVal("grounded", true);
		}
	}

}
