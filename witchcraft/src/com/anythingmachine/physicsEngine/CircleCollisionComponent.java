package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.math.Vector2;

public class CircleCollisionComponent extends CollisionComponent {
	private float radius;
	private Vector2 center;
	
	public CircleCollisionComponent(float rad) {
		radius = rad;
	}
	
//	@Override
//	public boolean isColliding(CircleCollisionComponent c) {
//		return false;
//	}
	
	@Override
	public boolean isColliding(RectCollisionComponent c) {
		return false;
	}
	
}
