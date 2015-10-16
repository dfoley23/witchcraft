package com.anythingmachine.GameStates;

import java.util.ArrayList;

import com.anythingmachine.input.InputManager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Menu extends Screen {
	protected InputManager input;
	protected ArrayList<Sprite> uiimgs;
	protected int currentSprite;
	
	public Menu() {
		input = new InputManager();
		setupInput();
		currentSprite = 0;
	}
	
	@Override
	public void update(float dt) {
		input.update(dt);
		float yaxis = input.axisRange2Y();
		if( input.isNowNotThen("down") || (yaxis > 0 && input.isNowNotThen("YAxis"))) {
			currentSprite = currentSprite + 1 >= uiimgs.size() ? 0 : currentSprite+1;
		} else if ( input.isNowNotThen("up") || (yaxis < 0 && input.isNowNotThen("YAxis"))) {
			currentSprite = currentSprite <= 0 ? uiimgs.size()-1 : currentSprite-1;			
		}
		if (input.is("select")) {
			switchScreen("play");
		}
	}
	
	@Override
	public void draw(Batch batch ) {
		batch.begin();
		int index = 0;
		for( Sprite s: uiimgs) {
			if ( index == currentSprite ) {
				s.setColor(Color.WHITE);
				s.draw(batch);
			} else {
				s.setColor(Color.DARK_GRAY);
				s.draw(batch);
			}
			index++;
		}
		batch.end();
	}
	
	protected void setupInput() {
		if (Controllers.getControllers().size > 0) {
			input.addInputState("YAxis", 1);
			input.addInputState("up", 19);
			input.addInputState("down", 20);
			input.addInputState("select", 96);
		} else {
			input.addInputState("up", Keys.UP);
			input.addInputState("down", Keys.DOWN);
			input.addInputState("select", Keys.ENTER);
		}
	}
}
