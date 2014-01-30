package com.anythingmachine.tiledMaps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Camera {

	public static OrthographicCamera camera;
	
	private float width;
	private float height;
	private Rectangle bounds;
	
	public Camera(float width, float height) {
		bounds = new Rectangle(0, 0, width, height);
		this.width = width;
		this.height = height;
	}
	
	
	public void update() {
		camera.update();
		bounds.set(camera.position.x-(width*camera.zoom)*0.5f,
		camera.position.y-(height*camera.zoom)*0.5f,
		camera.zoom*width, camera.zoom*height);
	}
	
	public boolean inBounds(Vector2 pos) {
		return bounds.contains(pos.x, pos.y);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
}
