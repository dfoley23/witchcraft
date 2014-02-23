package com.anythingmachine.tiledMaps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Camera {

	public static OrthographicCamera camera;
	
	private float width;
	private float height;
	private Rectangle bounds;
	
	public Camera(float width, float height) {
		bounds = new Rectangle(-128, -128, width+256, height+256);
		this.width = width;
		this.height = height;
	}
	
	
	public void update() {
		camera.update();
		bounds.set(camera.position.x-(width*camera.zoom)*0.5f,
		camera.position.y-(height*camera.zoom)*0.5f,
		camera.zoom*width, camera.zoom*height);
	}
	
	public boolean inBounds(Vector3 pos) {
		return bounds.contains(pos.x, pos.y);
	}
	
	public boolean inscaledBounds(Vector3 pos) {
		if ( pos.x < bounds.x+(bounds.width*1.1) 
				&& pos.y < bounds.y+(bounds.height*1.1)
				&& pos.x > bounds.x-bounds.width*.1
				&& pos.y > bounds.y-bounds.height*.1) {
			return true;
		}
		return false;
	}	
	
	public boolean inBigBounds(Vector3 pos) {
		if ( pos.x < bounds.x+(bounds.width*2) 
				&& pos.y < bounds.y+(bounds.height*2)
				&& pos.x > bounds.x-bounds.width
				&& pos.y > bounds.y-bounds.height) {
			return true;
		}
		return false;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
}
