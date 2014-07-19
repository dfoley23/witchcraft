package com.anythingmachine.cinematics;

import java.util.ArrayList;

import com.anythingmachine.collisionEngine.Entity;

public class CinematicObject extends Entity {
	private ArrayList<CinematicAction> actions;
	private boolean hasactions = false;
	
	public CinematicObject() {
		actions = new ArrayList<CinematicAction>();
	}
	
	public CinematicObject addAction(CinematicAction action) {
		actions.add(action);
		hasactions = true;
		return this;
	}
	
	public void update(float dt) {
		hasactions = false;
		for(CinematicAction a: actions) {
			if( !a.isEnded() ) {
				if( a.isStarted(dt) ) {
					a.update(dt);
					System.out.println(a.toString()+" has begun");
				} else {
				System.out.println(a.toString()+" has not begun");
				}
				hasactions = true;
			} else {
				System.out.println(a.toString()+" has ended");				
			}
		}
	}
	
	public boolean hasActions() {
		return hasactions;
	}
	
	

}
