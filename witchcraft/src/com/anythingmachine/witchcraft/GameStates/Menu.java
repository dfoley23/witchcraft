package com.anythingmachine.witchcraft.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class Menu extends Screen {
	
	public Menu() {
		
	}
	@Override
	public void update(float dt) {
		if (Gdx.input.isKeyPressed(Keys.A) ) {
			switchScreen("play");
		}
	}
}
