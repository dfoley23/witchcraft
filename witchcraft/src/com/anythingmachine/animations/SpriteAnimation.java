package com.anythingmachine.animations;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SpriteAnimation {
	private ArrayList<Sprite> sprites;
	private int frame;
	private float frametime;
	private float t;
	private boolean loop;
	private Vector2 pos;
	
	public SpriteAnimation(float time, boolean loop) {
		this.frametime = time;
		this.loop = loop;
		pos = Vector2.Zero;
		t = 0;
		frame = 0;
		sprites = new ArrayList<Sprite>();
	}
	
	public void update(float dt) {
		t += dt;
		if ( t > frametime ) {
			frame = frame+1 >= sprites.size() ? (loop ? 0 : frame) : frame+1;
			t = 0;
		}
			
	}
	public void draw(Batch batch) {
		if ( frame < sprites.size() ) {
			sprites.get(frame).draw(batch);
		}
	}
	
	public int getSize() {
		return sprites.size();
	}
	
	public boolean atEnd() {
		return frame == sprites.size()-1;
	}
	public void setPos(Vector3 pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
		for( Sprite s: sprites) {
			s.setPosition(pos.x-(s.getWidth()*0.5f), pos.y-(s.getHeight()*0.5f));
		}
	}

	public void setFramePos(Vector3 pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
		Sprite spr = sprites.get(frame);
		spr.setPosition(pos.x-(spr.getWidth()*0.5f), pos.y-(spr.getHeight()*0.5f));
	}

	
	public void addFrame(String atlas, String region, float scale, boolean flip) {
		Sprite spr = WitchCraft.assetManager.get(atlas, TextureAtlas.class).createSprite(region);
		spr.scale(scale);
		spr.flip(flip, false);
		sprites.add(spr);
	}
}
