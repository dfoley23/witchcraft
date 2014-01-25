package com.anythingmachine.aiengine;

import java.util.HashMap;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.States.State;
import com.anythingmachine.witchcraft.States.StateEnum;
import com.anythingmachine.witchcraft.ground.Curve;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonData;

public class StateMachine {
	public State state;
	public AnimationManager animate;
	private HashMap<String, Boolean> tests;
	public InputManager input;
	private State[] states;
	public PhysicsState phyState;
	public Bone neck;	
	public int curGroundSegment;
	public Curve curCurve;
	public Platform elevatedSegment;

	public StateMachine(String name, Vector2 pos, Vector2 scl, boolean flip,
			SkeletonData sd) {
		tests = new HashMap<String, Boolean>();
		animate = new AnimationManager(name, pos, scl, flip, sd);
		input = new InputManager();
		states = new State[StateEnum.DEAD.getSize()];
		this.curGroundSegment = 0;
		this.curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		neck = animate.findBone("neck");

	}

	public void update(float dt) {
		input.update(dt);

		state.update(dt);

		float delta = Gdx.graphics.getDeltaTime();

		if ( state.canAnimate() ) 
			animate.applyTotalTime(true, delta);

		animate.setPos(phyState.getPos(), -8f, 0f);
		animate.updateSkel(dt);

	}
	public void addState(StateEnum type, State s) {
		states[type.getID()] = s;
	}
	public void addTest(String name, boolean val) {
		tests.put(name, val);
	}

	public void setState(StateEnum name) {
		this.state = states[name.getID()];
	}

	public boolean inState(StateEnum state) {
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

	public boolean testANDState(String test, StateEnum state) {
		return tests.get(test) && this.state.name == state;
	}

	public boolean testORState(String test, StateEnum state) {
		return tests.get(test) || this.state.name == state;
	}

	public boolean testORNotState(String test, StateEnum state) {
		return tests.get(test) || this.state.name != state;
	}
	
}
