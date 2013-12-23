package com.anythingmachine.physicsEngine;

import java.util.ArrayList;

public class CollisionWorld {
	private ArrayList<RectCollisionComponent> anonItems;
	private ArrayList<RectCollisionComponent> checkEverything;
	
	public CollisionWorld() {
		anonItems = new ArrayList<RectCollisionComponent>();
		checkEverything = new ArrayList<RectCollisionComponent>();
	}
	
	public void update() {
		for(RectCollisionComponent main: checkEverything) {
			if ( main.isActive() ) {
				for( RectCollisionComponent anon: anonItems)  {
					if ( anon.isActive() ) {
						main.isColliding(anon);
					}
				}
			}
		}
	}
	
}
