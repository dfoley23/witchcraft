package com.anythingmachine.cinematics;



public interface CinematicAction {
	
	public void update(float dt);
	public boolean isStarted(float dt);	
	public boolean isEnded();
}
