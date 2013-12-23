package com.anythingmachine.physicsEngine;

import com.badlogic.gdx.math.Rectangle;

public class RectCollisionComponent extends CollisionComponent {
	private Rectangle rect;
	
	public RectCollisionComponent(int w, int h) {
		rect = new Rectangle(0, 0, w, h);
	}
		
//	@Override
//	public boolean isColliding(CircleCollisionComponent c) {
//		return false;
		//		return Util.pointInRectangle(c.getPos(), this ) ||
//				Util.intersectCircle(S, (A,B)) ||
//				Util.intersectCircle(S, (B,C)) ||
//				Util.intersectCircle(S, (C,D)) ||
//				Util.intersectCircle(S, (D,A))
//	}
	
	@Override
	public boolean isColliding(RectCollisionComponent c) {
		return this.rect.overlaps(c.getRect());
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
}
