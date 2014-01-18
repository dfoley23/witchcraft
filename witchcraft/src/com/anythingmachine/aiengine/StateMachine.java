package com.anythingmachine.aiengine;

import java.util.HashMap;

import com.anythingmachine.animations.AnimationManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonData;

public class StateMachine {
	public State state;
	public AnimationManager animate;
	private HashMap<String, Boolean> tests;

	public StateMachine(String name, Vector3 pos, Vector2 scl, boolean flip,
			SkeletonData sd) {
		tests = new HashMap<String, Boolean>();
		animate = new AnimationManager(name, pos, scl, flip, sd);
	}

	public void addTest(String name, boolean val) {
		tests.put(name, val);
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean inState(State state) {
		return this.state == state;
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

	public boolean testANDState(String test, State state) {
		return tests.get(test) && this.state == state;
	}

	public boolean testORState(String test, State state) {
		return tests.get(test) || this.state == state;
	}

	public boolean testORNotState(String test, State state) {
		return tests.get(test) || this.state != state;
	}
	
}
