package com.anythingmachine.witchcraft.GameStates;

import com.anythingmachine.witchcraft.WitchCraft;

public class Screen {

	public Screen() {
		
	}
	
	public void update(float dt) {
		
	}
	
	public void drawUi() {
		
	}
	
	public void switchScreen(String name) {
		String current = WitchCraft.currentScreen;
		WitchCraft.screens.get(current).transistionOut();
		WitchCraft.currentScreen = name;
		WitchCraft.screens.get(current).transistionIn();
	}

	public void transistionIn() {
		
	}
	
	public void transistionOut() {
		
	}
}
