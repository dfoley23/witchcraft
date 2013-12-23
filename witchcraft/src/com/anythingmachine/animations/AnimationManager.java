package com.anythingmachine.animations;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.Slot;

public class AnimationManager {
	private HashMap<String, Animation> animations;
	private Skeleton skel;
	private String currentAnim;
	private Bone root;
	private float totalTime = 0f;
	private Vector2 scale;
	private boolean loop;
	
	public AnimationManager(String name, Vector3 pos, Vector2 scl,
			boolean flip, SkeletonData sd) {
		this.scale = scl;
		animations = new HashMap<String, Animation>();
		skel = new com.esotericsoftware.spine.Skeleton(sd);
		skel.setSkin(name);
		skel.setToBindPose();
		root = skel.getRootBone();
		root.setX(pos.x);
		root.setY(pos.y);
		root.setScaleX(scl.x);
		root.setScaleY(scl.y);
		skel.updateWorldTransform();
		skel.setFlipX(flip);
		loop = true;
	}

	public void update(float delta) {
		totalTime += delta;
		// if ( animation != null ) {
		// if ( totalTime > animation.getDuration() ) {//&& !inAir && !moveLeft
		// && !moveRight) {
		// totalTime = 0;
		// skel.setToBindPose();
		// root.setX(getPosPixels().x);
		// root.setY(getPosPixels().y - 64f);
		// root.setScaleX(0.6f);
		// root.setScaleY(0.7f);
		// } else
		{
			animations.get(currentAnim).apply(skel, totalTime, loop);
		}
		// }
		skel.updateWorldTransform();
		skel.update(delta);
	}

	public void updateSkel(float delta) {
		totalTime += delta;
		skel.updateWorldTransform();
		skel.update(delta);		
	}
	
	public void applyTotalTime(boolean val, float delta) {
		animations.get(currentAnim).apply(skel, totalTime+delta, val);
	}
	
	public void draw(SpriteBatch batch) {
		skel.draw(batch);
	}
	
	public void addAnimation(String id, Animation anim) {
		animations.put(id, anim);
	}

	public void setCurrent(String id, boolean val) {
		currentAnim = id;
		loop = val;
	}

	public void bindPose() {
		totalTime = 0;
		skel.setToBindPose();
		root.setScaleX(scale.x);
		root.setScaleY(scale.y);
	}

	public boolean isTimeOverAQuarter(float delta) {
		return totalTime+delta > animations.get(currentAnim).getQuarterDuration();
	}
	
	public void setFlipX(boolean val){
		skel.setFlipX(val);
	}
	
	public boolean atEnd() {
		return totalTime > animations.get(currentAnim).getDuration();
	}

	public Bone getRoot() {
		return root;
	}
	
	public float getCurrentAnimTime() {
		return animations.get(currentAnim).getDuration();
	}
	public void setPos(Vector3 pos, float dx, float dy) {
		root.setX(pos.x + dx);
		root.setY(pos.y + dy);
	}
	
	public float getTime() {
		return totalTime;
	}
	
	public Bone findBone(String name) {
		return skel.findBone(name);
	}
	
	public Slot findSlot(String name) {
		return skel.findSlot("right hand");
	}
}
