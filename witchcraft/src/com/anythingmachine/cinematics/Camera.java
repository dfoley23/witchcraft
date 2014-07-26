package com.anythingmachine.cinematics;

import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Camera extends Entity {
	public static OrthographicCamera camera;
	private static float width;
	private static float height;
	private static Rectangle bounds;
	public static CameraState state;
	private Vector2 vel;
	
	public Camera(float width, float height) {
		bounds = new Rectangle(-128, -128, width+256, height+256);
		this.width = width;
		this.height = height;
		state = CameraState.FOLLOWPLAYER;
	}
	
	public void update() {
		camera.update();
		bounds.set(camera.position.x-(width*camera.zoom)*0.5f,
		camera.position.y-(height*camera.zoom)*0.5f,
		camera.zoom*width, camera.zoom*height);
	}
	
	public static boolean inBounds(Vector3 pos) {
		return bounds.contains(pos.x, pos.y);
	}
	
	public static boolean inscaledBounds(Vector3 pos) {
		if ( pos.x < bounds.x+(bounds.width*1.1) 
				&& pos.y < bounds.y+(bounds.height*1.1)
				&& pos.x > bounds.x-bounds.width*.1
				&& pos.y > bounds.y-bounds.height*.1) {
			return true;
		}
		return false;
	}	
	
	public static boolean inBigBounds(Vector3 pos) {
		if ( pos.x < bounds.x+(bounds.width*2) 
				&& pos.y < bounds.y+(bounds.height*2)
				&& pos.x > bounds.x-bounds.width
				&& pos.y > bounds.y-bounds.height) {
			return true;
		}
		return false;
	}
	
	public static void updateState(Vector3 pos, float maxY) {
		state.update(pos, maxY);		
	}
	
	public static Rectangle getBounds() {
		return bounds;
	}
	
	@Override
	public Vector3 getPos() {
		return camera.position;
	}
	
	public void setState(CameraState s) {
		this.state = s;
	}
	/**
	 * particle functions
	 */
	@Override
	public void stop() {
		vel.x = 0;
		vel.y = 0;
	}

	@Override
	public void setX(float x) {
		Camera.camera.position.x = x;
	}

	@Override
	public void stopOnX() {
		vel.x = 0;
	}

	@Override
	public void stopOnY() {
		vel.y = 0;
	}

	@Override
	public void setY(float y) {
		Camera.camera.position.y = y;
	}

	@Override
	public void setPos(float x, float y) {
		Camera.camera.position.x = x;
		Camera.camera.position.y = y;
	}

	@Override
	public void setPos(Vector2 target) {
		Camera.camera.position.x = target.x;
		Camera.camera.position.y = target.y;
	}

	@Override
	public void addPos(float x, float y) {
		Camera.camera.position.x += x;
		Camera.camera.position.y += y;
	}

	@Override
	public void setVel(float x, float y, float z) {
		vel.x = x;
		vel.y = y;
	}

	@Override
	public void setYVel(float y) {
		vel.y = y;
	}

	@Override
	public void setXVel(float x) {
		vel.x = x;
	}

	@Override
	public void addVel(float x, float y, float z) {
		vel.x += x;
		vel.y += y;
	}

	
}
