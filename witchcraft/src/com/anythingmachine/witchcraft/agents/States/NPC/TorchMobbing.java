package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.badlogic.gdx.Gdx;

public class TorchMobbing extends Mobbing {
	private String mixAnim;
	private float mixAnimTime;
	private float holdTime;
	private float time;
	
	public TorchMobbing(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		mixAnim = "torch";
		mixAnimTime = 0f;//sm.animate.getAnim(mixAnim).getDuration();
		time = 0;
	}
	
	@Override
	public void update(float dt) {
		aiChoiceTime += dt;

		checkGround();

		if ( sm.me.npctype.canAttack() )
			checkAttack();
		else
			checkInBounds();

		takeAction(dt);
		
		fixCBody();

		updateSkel(dt);
	}
	
	@Override
	public void updateSkel(float dt) {
		float delta = Gdx.graphics.getDeltaTime();

		sm.animate.applyTotalTime(true, delta);
//		time += delta;
//		if ( time > holdTime ) {
//			sm.animate.mix(mixAnim, mixAnimTime, time/mixAnimTime);
//		} else 
		{
			sm.animate.mix(mixAnim, 1f, 1f);
		}
		sm.animate.setPos(sm.phyState.body.getPos(), -8f, 0f);

		sm.animate.updateSkel(delta);
	}
	
	@Override
	public void setIdle() {
		sm.getState(NPCStateEnum.IDLE).transistionIn();
	}
	
	@Override
	public void transistionIn() {
		time = 0;
		sm.animate.setCurrent("torch", true);
		if ( childState == null || childState == this) {
			childState = sm.getState(NPCStateEnum.IDLE);
		}
	}
}
