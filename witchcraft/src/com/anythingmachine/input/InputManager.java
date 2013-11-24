package com.anythingmachine.input;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;


public class InputManager {
	private HashMap<String, Integer> keymap;

	public InputManager() {
		keymap = new HashMap<String, Integer>();
	}
	
	public void update() {
	}
	
	public boolean is(String state) {
		if ( keymap.containsKey(state)) {
			return Gdx.input.isKeyPressed(keymap.get(state));
		} 
		return false;
	}

	public void addInputState(String state, Integer keyValue) {
		keymap.put(state, keyValue);
	}
}
