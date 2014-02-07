package com.anythingmachine.aiengine;

import java.util.HashMap;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.witchcraft.States.PlayerState;
import com.anythingmachine.witchcraft.States.PlayerStateEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonData;

public class StateMachine {
	public PlayerState state;
	public AnimationManager animate;
	protected HashMap<String, Boolean> tests;
	public InputManager input;
	protected PlayerState[] states;
	public PhysicsState phyState;
	/* WHERE TO PUT THESE */

	public StateMachine(String name, Vector3 pos, Vector2 scl, boolean flip,
			SkeletonData sd) {
		tests = new HashMap<String, Boolean>();
		animate = new AnimationManager(name, pos, scl, flip, sd);
		input = new InputManager();
		states = new PlayerState[PlayerStateEnum.DEAD.getSize()];
	}

	public void update(float dt) {

		state.update(dt);

		float delta = Gdx.graphics.getDeltaTime();

		animate.applyTotalTime(true, delta);

		animate.setPos(phyState.getPos(), -8f, 0f);
		animate.updateSkel(dt);

	}

	public void addState(PlayerStateEnum type, PlayerState s) {
		states[type.getID()] = s;
	}

	public void addTest(String name, boolean val) {
		tests.put(name, val);
	}

	public void setInitialState(PlayerStateEnum name) {
		this.state = states[name.getID()];
	}

	public void setState(PlayerStateEnum name) {
		state.transistionOut();
		this.state = states[name.getID()];
		state.transistionIn();
	}

	public PlayerState getState(PlayerStateEnum name) {
		return states[name.getID()];
	}

	public boolean inState(PlayerStateEnum state) {
		return this.state.name.getID() == state.getID();
	}

	public void setTestVal(String name, boolean val) {
		tests.put(name, val);
	}

	public boolean test(String name) {
		return tests.get(name);
	}

	public boolean testANDtest(String test1, String test2) {
		return tests.get(test1) && tests.get(test2);
	}

	public boolean testORtest(String test1, String test2) {
		return tests.get(test1) || tests.get(test2);
	}

	public boolean testtesttestOR(String test1, String test2, String test3) {
		return tests.get(test1) || tests.get(test2) || tests.get(test3);
	}

	public boolean testANDState(String test, PlayerStateEnum state) {
		return tests.get(test) && this.state.name == state;
	}

	public boolean testORState(String test, PlayerStateEnum state) {
		return tests.get(test) || this.state.name == state;
	}

	public boolean testORNotState(String test, PlayerStateEnum state) {
		return tests.get(test) || this.state.name != state;
	}

}
