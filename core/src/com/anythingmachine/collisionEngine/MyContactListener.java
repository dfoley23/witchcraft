package com.anythingmachine.collisionEngine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	/**
	 * When two new objects start to touch
	 */
	@Override
	public void beginContact( Contact contact ) {
		final Fixture x1 = contact.getFixtureA( );
		final Fixture x2 = contact.getFixtureB( );
		Entity entityA = (Entity)x1.getBody( ).getUserData( );
		Entity entityB = (Entity)x2.getBody( ).getUserData( );

		entityA.handleContact(contact, true);
		entityB.handleContact(contact, false);
		
		//System.out.println("hello world: collision between" + contact);
	}

	/**
	 * When two objects stop touching
	 */
	@Override
	public void endContact( Contact contact ) {
		final Fixture x1 = contact.getFixtureA( );
		final Fixture x2 = contact.getFixtureB( );
		if ( x1 != null && x2 != null ) {
			Entity entityA = (Entity) x1.getBody( ).getUserData( );
			Entity entityB = (Entity)x2.getBody( ).getUserData( );

			entityA.endContact(contact, true);
			entityB.endContact(contact, false);
		}

	}

	/**
	 * Before physics is calculated each step
	 */
	@Override
	public void preSolve( Contact contact, Manifold oldManifold ) {

	}


	/*
	 * After physics is calculated each step
	 */
	@Override
	public void postSolve( Contact contact, ContactImpulse impulse ) {

	}
}