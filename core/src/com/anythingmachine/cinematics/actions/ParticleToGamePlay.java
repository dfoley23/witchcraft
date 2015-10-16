package com.anythingmachine.cinematics.actions;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.cinematics.CinematicAction;
import com.anythingmachine.collisionEngine.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ParticleToGamePlay extends Entity implements CinematicAction {
	protected Entity component;
	protected float cineTime = 0.0f;
	protected float startDT;
	private boolean hasStarted = false;
	
	
	public ParticleToGamePlay(float startDT, Entity e, int index) {
		this.component = e;		
		this.startDT = startDT;

		GamePlayManager.addEntity(this,index);
	}

	@Override
	public void update(float dt) {
		if ( hasStarted && !component.isEnded() ) 
			component.update(dt);
	}
	
	@Override
	public void draw(Batch batch) {
		if ( hasStarted && !component.isEnded() ) 
			component.draw(batch);
	}
		
	public boolean isStarted(float dt) {
		cineTime += dt;
		if( cineTime >= startDT && !hasStarted) {
			hasStarted = true;			
		}
		return cineTime >= startDT;
	}
	
	public boolean isEnded() {
		return hasStarted;
	}
}
