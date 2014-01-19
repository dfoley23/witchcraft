package com.anythingmachine.input;

import java.util.HashMap;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputManager {
	private HashMap<String, Integer> keymap;
	private HashMap<String, Float> deltamap;
	private HashMap<String, Boolean> onoffmap;
	private Controller controller;
	private boolean debugcontrols = true;

	public InputManager() {
		keymap = new HashMap<String, Integer>();
		deltamap = new HashMap<String, Float>();
		onoffmap = new HashMap<String, Boolean>();
		if (Controllers.getControllers().size > 0) {
			this.controller = Controllers.getControllers().get(0);
			debugcontrols = false;
		}
	}

	public void update(float dT) {
		for (String s : onoffmap.keySet()) {
			if (debugcontrols) {
				if (onoffmap.get(s) && !Gdx.input.isKeyPressed(keymap.get(s))) {
					onoffmap.put(s, false);
				}
			} else {
				if (onoffmap.get(s) && controller.getButton(keymap.get(s))) {
					onoffmap.put(s, false);
				}
			}
		}
	}

	public boolean is(String state) {
		if (debugcontrols) {
			return Gdx.input.isKeyPressed(keymap.get(state));
		} 
		return controller.getButton(keymap.get(state));
	}

	public boolean isAfterDelta(String state, Float delta) {
		if (debugcontrols) {
		if (Gdx.input.isKeyPressed(keymap.get(state))) {
			Float last = deltamap.get(state);
			if (last > delta) {
				deltamap.put(state, 0f);
				return true;
			} else {
				deltamap.put(state, WitchCraft.dt + last);
			}
		}
		}else {
			if (controller.getButton(keymap.get(state))) {
				Float last = deltamap.get(state);
				if (last > delta) {
					deltamap.put(state, 0f);
					return true;
				} else {
					deltamap.put(state, WitchCraft.dt + last);
				}
			}
		}
		return false;
	}

	public boolean isNowNotThen(String state) {
		if ( debugcontrols) {
		if (!onoffmap.get(state) && Gdx.input.isKeyPressed(keymap.get(state))) {
			onoffmap.put(state, true);
			return true;
		}
		} else {
			if (!onoffmap.get(state) && controller.getButton(keymap.get(state))) {
				onoffmap.put(state, true);
				return true;
			}
		}
		return false;
	}

	public void addInputState(String state, Integer keyValue) {
		keymap.put(state, keyValue);
		deltamap.put(state, 10f);
		onoffmap.put(state, false);
	}
}
