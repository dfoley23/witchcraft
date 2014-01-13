package com.anythingmachine.aiengine;

import java.util.HashMap;

public class StateMachine {
	public State state;
	private HashMap<String, Boolean> tests;
	
	public StateMachine() {
		tests = new HashMap<String, Boolean>();
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
	
	public boolean testtest(String test1, String test2) {
		return  tests.get(test1) && tests.get(test2);
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
