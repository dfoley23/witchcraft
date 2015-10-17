package com.anythingmachine.animations;

import java.util.HashMap;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class AnimationManager {
	private HashMap<String, Animation> animations;
	private SkeletonRenderer renderer;
	private Skeleton skel;
	private String currentAnim;
	private Bone root;
	public Bone hip;
	private float totalTime = 0f;
	private Vector2 scale;
	private boolean loop;
	private boolean isFlipped = false;
	private Array<Event> events;

	public AnimationManager(String name, Vector3 pos, Vector2 scl,
			boolean flip, SkeletonData sd) {
		this.scale = scl;
		animations = new HashMap<String, Animation>();
		skel = new com.esotericsoftware.spine.Skeleton(sd);
		skel.setSkin(name);
		skel.setToSetupPose();
		root = skel.getRootBone();
		hip = skel.findBone("hip");
		root.setX(pos.x);
		root.setY(pos.y);
		root.setScaleX(scl.x);
		root.setScaleY(scl.y);
		skel.updateWorldTransform();
		skel.setFlipX(flip);
		loop = true;
		events = new Array<Event>();
		renderer = new SkeletonRenderer();
	}

	public void update(float delta) {
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
		// {
		// System.out.println(currentAnim);

		if (delta > WitchCraft.dt * 30) {
			totalTime += delta;
			animations.get(currentAnim).apply(skel,
					totalTime - WitchCraft.dt * 30, totalTime, loop, events);
			 }
			skel.updateWorldTransform();
			skel.update(delta);
	}

	public void updateSkel(float delta) {
		totalTime += delta;
		skel.updateWorldTransform();
		skel.update(delta);
	}

	public boolean isSkin(String skinname) {
		return getSkin().getName().equals(skinname);
	}

	public void applyTotalTime(boolean val, float delta) {
		animations.get(currentAnim).apply(skel, totalTime, totalTime + delta,
				val, events);
	}

	public void mix(String anim, float time, float alpha) {
		animations.get(anim).mix(skel, time, time, false, events, alpha);
	}

	public Animation getAnim(String anim) {
		return animations.get(anim);
	}

	public void setRegion(String slot, String attach, String set,
			boolean updatesize, float width, float height) {
		RegionAttachment ra = ((RegionAttachment) skel
				.getAttachment(slot, slot));
		if (updatesize) {
			ra.setWidth(width);
			ra.setHeight(height);
		}
		ra.setRegion(WitchCraft.assetManager.get("data/spine/characters.atlas",
				TextureAtlas.class).findRegion(set));
	}

	public void draw(Batch batch) {
		renderer.draw(batch, skel);
	}

	public void addAnimation(String id, Animation anim) {
		animations.put(id, anim);
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void setTint(Color c) {
		this.skel.setColor(c);
	}
	public void rotate(float r) {
		float x = hip.getX();
		float y = hip.getY();
		hip.setX(0);
		hip.setY(0);
		this.hip.setRotation(r);
		hip.setX(x);
		hip.setY(y);
	}

	public void setCurrent(String id, boolean val) {
		currentAnim = id;
		loop = val;
	}
	
	public String getCurrentAnim() {
		return currentAnim;
	}

	public void bindPose() {
		totalTime = 0;
		skel.setToSetupPose();
		root.setScaleX(scale.x);
		root.setScaleY(scale.y);
	}

	public boolean isTimeOverAQuarter(float delta) {
		return totalTime + delta > animations.get(currentAnim).getDuration() * 0.25f;
	}

	public boolean isTimeOverAThird(float delta) {
		return totalTime + delta > animations.get(currentAnim).getDuration() * 0.33f;
	}

	public boolean isTimeOverAHalf(float delta) {
		return totalTime + delta > animations.get(currentAnim).getDuration() * 0.5f;
	}

	public boolean isTImeOverThreeQuarters(float delta) {
		return totalTime + delta > animations.get(currentAnim).getDuration() * 0.75f;
	}
	
	public boolean isAnimationEnded(float delta) {
		return totalTime+delta >= animations.get(currentAnim).getDuration()-delta*2;
	}

	public void setFlipX(boolean val) {
		float x = skel.getX();
		skel.setX(0);
		skel.setFlipX(val);
		skel.setX(x);
		isFlipped = val;
	}

	public void setFlipY(boolean val) {
		float x = skel.getY();
		skel.setY(0);
		skel.setFlipY(val);
		skel.setY(x);
	}

	public boolean atEnd() {
		return totalTime > animations.get(currentAnim).getDuration();
	}

	public Skin getSkin() {
		return skel.getSkin();
	}

	public void switchSkin(String skin) {
		skel.setSkin(skin);
	}

	public void setSkin(Skin skin) {
		skel.setSkin(skin);
	}

	public Bone getRoot() {
		return root;
	}
	
	public Bone getBone(String name) {
		return skel.findBone(name);
	}

	public Vector2 getScale() {
		return new Vector2(root.getScaleX(), root.getScaleY());
	}

	public void setScale(float x, float y) {
		scale.x = x;
		scale.y = y;
	}
		
	/**
	 * gets the duration of the current animation
	 * 
	 * @return
	 */
	public float getCurrentAnimTime() {
		return animations.get(currentAnim).getDuration();
	}

	public void setPos(Vector3 pos, float dx, float dy) {
		root.setX(pos.x + dx);
		root.setY(pos.y + dy);
	}

	/**
	 * return the total time so far for this animations
	 * 
	 * @return
	 */
	public float getTime() {
		return totalTime;
	}

	public boolean testUnderTime(float delta, float test) {
		return totalTime + delta < test;
	}

	public boolean testOverTime(float delta, float test) {
		return totalTime + delta > test;
	}

	public Bone findBone(String name) {
		return skel.findBone(name);
	}

	public Slot findSlot(String name) {
		return skel.findSlot("right hand");
	}
}
