package com.anythingmachine.cinematics;

import com.badlogic.gdx.graphics.g2d.Batch;


public interface CinematicAction {
	
	public void update(float dt);
	public boolean isStarted(float dt);	
	public boolean isEnded();
}
