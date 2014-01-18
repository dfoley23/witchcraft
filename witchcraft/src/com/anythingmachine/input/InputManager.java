package com.anythingmachine.input;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;


public class InputManager {
	private HashMap<String, Integer> keymap;
	private HashMap<String, Float> deltamap;
	private HashMap<String, Boolean> onoffmap;
	
	public InputManager() {
		keymap = new HashMap<String, Integer>();
		deltamap = new HashMap<String, Float>();
		onoffmap = new HashMap<String, Boolean>();
	}
	
	public void update(float dT) {
		for( String s: onoffmap.keySet()) {
			if ( onoffmap.get(s) && !Gdx.input.isKeyPressed(keymap.get(s))) {
				onoffmap.put(s, false);
			}
		}
	}
	
	public boolean is(String state) {
		return Gdx.input.isKeyPressed(keymap.get(state));
	}

	public boolean isAfterDelta(String state, Float delta) {
		if ( Gdx.input.isKeyPressed(keymap.get(state)) )  {
			Float last = deltamap.get(state);
			if ( last > delta) {
				deltamap.put(state, 0f);
				return true;
			} else {
				deltamap.put(state, WitchCraft.dt+last);
			}
		} 
		return false;
	}
	
	public boolean isNowNotThen(String state) {
		if ( !onoffmap.get(state) && Gdx.input.isKeyPressed(keymap.get(state))) {
			onoffmap.put(state, true);
			return true;
		}
		return false;
	}
	
	public void addInputState(String state, Integer keyValue) {
		keymap.put(state, keyValue);
		deltamap.put(state, 10f);
		onoffmap.put(state, false);
	}
}
