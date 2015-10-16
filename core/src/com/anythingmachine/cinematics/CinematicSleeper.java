package com.anythingmachine.cinematics;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Contact;

public class CinematicSleeper extends CinematicTrigger {
	private boolean isAsleep;
	
	public CinematicSleeper() {
		super();
		isAsleep = true;
	}
	
	@Override
	public CinematicTrigger addAction(CinematicAction a) {
		return this;
	}

	@Override
	public void update(float dt) {
		if( !isAsleep ) {
			GamePlayManager.sleepCinematic();
			isAsleep = true;
		}
	}

	@Override
	public void draw(Batch batch) {
	}

	@Override
	public boolean isEnded() {
		return false;
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
	}

	@Override
	public CinematicTrigger buildBody(float x, float y, float width, float height) {
		return this;
	}
	
	@Override
	public void transistionOut() {
		isAsleep = false;
	}


}
