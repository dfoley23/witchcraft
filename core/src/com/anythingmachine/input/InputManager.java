package com.anythingmachine.input;

import java.util.HashMap;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector3;

public class InputManager implements ApplicationListener, InputProcessor {
	private HashMap<String, Integer> keymap;
	private HashMap<String, Float> deltamap;
	private HashMap<String, Boolean> onoffmap;
	private Controller controller;
	private boolean debugcontrols = true;
	private MouseInput mouseInput;

	public InputManager() {
		keymap = new HashMap<String, Integer>();
		deltamap = new HashMap<String, Float>();
		onoffmap = new HashMap<String, Boolean>();
		if (Controllers.getControllers().size > 0) {
			this.controller = Controllers.getControllers().get(0);
			debugcontrols = false;
		} 			
		mouseInput = new MouseInput();
		Gdx.input.setInputProcessor(this);
	}

	public void update(float dT) {
		for (String s : onoffmap.keySet()) {
			if (debugcontrols) {
				if (onoffmap.get(s) && !Gdx.input.isKeyPressed(keymap.get(s))) {
					onoffmap.put(s, false);
				}
			} else {
				if (onoffmap.get(s) && !controller.getButton(keymap.get(s))) {
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
		} else {
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
		if (debugcontrols) {
			if (!onoffmap.get(state)
					&& Gdx.input.isKeyPressed(keymap.get(state))) {
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

	/**
	 * sets two regions of movement with the axis stick first range return 1 or
	 * -1 second range return 2 or -2
	 * 
	 * @return
	 */
	public int axisRange2() {
		if (WitchCraft.ON_ANDROID) {
			float val = controller.getAxis(0);
			float absval = Math.abs(val);
			if (absval > 0.25) {
				if (absval > 0.99) {
					return (int) Math.signum(val) * 2;
				} else {
					return (int) Math.signum(val);
				}
			}
		} else {
			if (left()) {
				if ( is("shift")) {
					return -2;
				}
				return -1;
			} else if (right()) {
				if ( is("shift")) {
					return 2;
				}
				return 1;
			}
		}
		return 0;
	}

	/**
	 * sets two regions of movement with the axis stick first range return 1 or
	 * -1 second range return 2 or -2
	 * 
	 * @return
	 */
	public int axisRange2Y() {
		if (WitchCraft.ON_ANDROID) {
			float val = controller.getAxis(1);
			float absval = Math.abs(val);
			if (absval > 0.25) {
				if (absval > 0.95) {
					return (int) Math.signum(val) * 2;
				} else {
					return (int) Math.signum(val);
				}
			}
		} 
		return 0;
	}

	public float axisDegree() {
		if ( WitchCraft.ON_ANDROID ) {
			float xval = controller.getAxis(0);
			float yval = controller.getAxis(1);
			if ( Math.abs(xval) > 0.25f || Math.abs(yval) > 0.25f )
				return (float)Math.atan2(yval, xval);
		}
		return 0;
	}
	public boolean left() {
		if (debugcontrols) {
			return Gdx.input.isKeyPressed(keymap.get("Left"));
		}
		return controller.getButton(keymap.get("Left")) || axisRange2() < 0;
	}

	public boolean right() {
		if (debugcontrols) {
			return Gdx.input.isKeyPressed(keymap.get("Right"));
		}
		return controller.getButton(keymap.get("Right")) || axisRange2() > 0;
	}

	public boolean up() {
		if (debugcontrols)
			return Gdx.input.isKeyPressed(keymap.get("UP"));
		return controller.getButton(keymap.get("UP")) || axisRange2Y() < 0;
	}

	public boolean down() {
		if (debugcontrols)
			return Gdx.input.isKeyPressed(keymap.get("down"));
		return controller.getButton(keymap.get("down")) || axisRange2Y() > 0;
	}
	
	public boolean rightClick() {
		return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}
	
	public boolean leftClick() {
//        sprite.setPosition(Gdx.input.getX() - sprite.getWidth()/2,
//                Gdx.graphics.getHeight() - Gdx.input.getY() - sprite.getHeight()/2);
//    }
//    if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
//        sprite.setPosition(Gdx.graphics.getWidth()/2 -sprite.getWidth()/2, 
//                Gdx.graphics.getHeight()/2 - sprite.getHeight()/2);		
		return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}

	public void addInputState(String state, Integer keyValue) {
		keymap.put(state, keyValue);
		deltamap.put(state, 10f);
		onoffmap.put(state, false);
	}
	

    @Override
    public void create() {        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {        
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
    	Vector3 tmp = WitchCraft.cam.camera.unproject(new Vector3(screenX, screenY, 0));
    	mouseInput.updateTarget(tmp.x, tmp.y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
