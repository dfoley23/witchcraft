package com.anythingmachine.witchcraft.agents.player.items;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Cape {
	private Link pin;
	private ArrayList< Link > linkParts;
	private boolean isRotated = false;
	private Vector2 dim = new Vector2(8f, 8f);
	
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
	
	public void drawShape( ShapeRenderer renderer ) {
		Vector2 p0 = pin.getPositionPixels().add(-dim.x, dim.y);
		Vector2 p1 = pin.getPositionPixels().add(-dim.x, -dim.y);
		Vector2 p2 = pin.getPositionPixels().add(dim.x, -dim.y);
		Vector2 p3 = pin.getPositionPixels().add(dim.x, dim.y);
		Vector2 oldTrig = new Vector2((float)Math.cos(pin.body.getAngle()+Util.PI),
				(float)Math.sin(pin.body.getAngle()+Util.PI));
		Vector2 newTrig = Vector2.Zero;
		renderer.begin(ShapeType.FilledTriangle);
		renderer.setColor(1, 0, 0, 1);
		renderer.filledTriangle(p0.x+oldTrig.x, p0.y+oldTrig.y, 
								p1.x+oldTrig.x, p1.y+oldTrig.y,
								p2.x+oldTrig.x, p2.y+oldTrig.y);
		renderer.filledTriangle(p0.x+oldTrig.x, p0.y+oldTrig.y, 
								p2.x+oldTrig.x, p2.y+oldTrig.y,
								p3.x+oldTrig.x, p3.y+oldTrig.y);
		for( Link l: linkParts ) {
			newTrig = new Vector2((float)Math.cos(l.body.getAngle()+Util.PI),
					(float)Math.sin(l.body.getAngle()+Util.PI));
			p1 = l.getPositionPixels().add(-dim.x, -dim.y);
			p2 = l.getPositionPixels().add(dim.x, -dim.y);			
			renderer.filledTriangle(p0.x+oldTrig.x, p0.y+oldTrig.y, 
									p1.x+newTrig.x, p1.y+newTrig.y,
									p2.x+newTrig.x, p2.y+newTrig.y);
			renderer.filledTriangle(p0.x+oldTrig.x, p0.y+oldTrig.y, 
									p2.x+newTrig.x, p2.y+newTrig.y,
									p3.x+oldTrig.x, p3.y+oldTrig.y);
			p0 = l.getPositionPixels().add(-dim.x, dim.y);
			p3 = l.getPositionPixels().add(dim.x, dim.y);
			oldTrig = newTrig.cpy();
		}
		renderer.end();
	}
	
	public void updatePos( Vector2 pos ) {
		pin.updatePos(pos);
	}
	
	public void movePinTowardsPos( float x, float y ) {
		Vector2 pos = new Vector2( x, y );
		Vector2 diff = pos.sub(pin.getPositionPixels());
		pin.setVelocity(diff);
	}
	
	public boolean isCapeRotated() {
		return isRotated;
	}
	
	public void flipPinSprite() {
		pin.flipSprite();
		for ( Link l: linkParts ) {
			l.flipSprite();
		}
	}
	
	public void setGravityScale(float scale ) {
		pin.setGravityScale(scale);
		for ( Link l: linkParts ) {
			l.setGravityScale(scale);
		}
	}
	
	public void rotateCape(float degree) {
		pin.rotateSprite(degree);
	}
	
	private void createCape( int length, World world, float dY ) {
		Texture texture = new Texture(Gdx.files.internal("data/cape0Tex.png"));
		Vector2 pos = pin.getPositionPixels().sub(0f, texture.getHeight()/2.f);
		Link pastLink = pin;
		for( int i=0; i<length; i++ ) {
			if( i==1 || i == length -1 ) 
				texture = new Texture(Gdx.files.internal("data/cape" + i + "Tex.png"));
			Link l = new Link(world, pos, texture);
			l.createLinkJoint(pastLink, world);
			linkParts.add(l);
			pastLink = l;
			pos.y -= texture.getHeight()/2.f;
		}
	}
	
}
