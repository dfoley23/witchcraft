package com.anythingmachine.collisionEngine.ground;

import com.anythingmachine.collisionEngine.ground.Ground.GroundElemType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GroundElem {
	private Sprite sprite;
	public GroundElem(Vector2 pos, float size, float rotation, Color tint, GroundElemType type) {
		Texture tex = new Texture(Gdx.files.internal("data/rockAB.png"));
		//tex.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		switch( type ) {
		case ROCK:
			//tex = new Texture(Gdx.files.internal("data/rockAB.png")); 
			break;
		case GRASS:
			tex = new Texture(Gdx.files.internal("data/grassA.png")); 
			break;
		default:
			break;
		}
		sprite = new Sprite(tex);
		sprite.scale(size);
		sprite.setRotation(rotation);
		sprite.setOrigin(pos.x, pos.y);
		sprite.setPosition(pos.x, pos.y);
		sprite.setColor(tint);
	}
	
	public void draw(SpriteBatch batch){
		sprite.draw(batch);
	}
}
