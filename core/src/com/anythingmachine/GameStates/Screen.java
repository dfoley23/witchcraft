package com.anythingmachine.GameStates;

import com.anythingmachine.cinematics.CinematicTrigger;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Screen {

	public Screen() {
		
	}
	
	public void update(float dt) {
		
	}
	
	public void draw(Batch batch) {
		
	}
		
	public void switchScreen(String name) {
		Screen current = WitchCraft.currentScreen;
		current.transistionOut();
		WitchCraft.currentScreen = WitchCraft.screens.get(name);
		WitchCraft.screens.get(name).transistionIn();
	}
	
	public void setParent(String name) {
		
	}
	
	public void loadLevel(String name) {
		
	}
		
	public void loadMenuAssets() {
	}

	public void transistionIn() {
		
	}
	
	public void resize(int width, int height) {
		
	}
	public void transistionOut() {
		
	}
	
	public void drawBackground(Batch batch) {
		
	}
	public void drawPlayerLayer(Batch batch) {
		
	}
}
