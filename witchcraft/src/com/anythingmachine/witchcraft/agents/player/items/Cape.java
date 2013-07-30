package com.anythingmachine.witchcraft.agents.player.items;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Cape {
	private Link pin;
	private ArrayList< Link > linkParts;
	
	public Cape(Vector2 pinPos, World world, int length, float dY) {
		linkParts = new ArrayList< Link >( );
		pin = new Link( world, pinPos, new Texture(Gdx.files.internal("data/capePinTex.png")));
		pin.setBodyType(BodyType.KinematicBody);

		createCape(length, world, dY);
	}
	
	public void draw( SpriteBatch batch ) {
		pin.draw(batch);
	   	for ( Link l: linkParts ) {
	   		l.draw(batch);
	   	}
	}
	
	public void updatePos( Vector2 pos ) {
		pin.updatePos(pos);
	}
	
	public void movePinTowardsPos( float x, float y ) {
		Vector2 pos = new Vector2( x, y );
		Vector2 diff = pos.sub(pin.getPositionPixels());
		pin.setVelocity(diff);
	}
	
	public void flipPinSprite() {
		pin.flipSprite();
		for ( Link l: linkParts ) {
			l.flipSprite();
		}
	}
	
	private void createCape( int length, World world, float dY ) {
		Vector2 pos = pin.getPositionPixels().sub(0f, dY);
		Texture texture = new Texture(Gdx.files.internal("data/cape0Tex.png"));
		Link pastLink = pin;
		for( int i=0; i<length; i++ ) {
			if( i==1 || i == length -1 ) 
				texture = new Texture(Gdx.files.internal("data/cape" + i + "Tex.png"));
			Link l = new Link(world, pos, texture);
			l.createLinkJoint(pastLink, world);
			linkParts.add(l);
			pastLink = l;
			pos.y -= dY;
		}
	}
	
}
