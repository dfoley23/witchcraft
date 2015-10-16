package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.Util.Util;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.physicsEngine.particleEngine.FireEmitter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class TorchMobbing extends Mobbing {
//	private String mixAnim;
//	private float mixAnimTime;
//	private float holdTime;
//	private float time;
	private FireEmitter fire;
	
	public TorchMobbing(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
//		mixAnim = "torch";
//		mixAnimTime = 0f;//sm.animate.getAnim(mixAnim).getDuration();
//		time = 0;
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		float rootx = sm.animate.findBone("right hand").getWorldX();
		float rooty = sm.animate.findBone("right hand").getWorldY();
		rooty += 6;
		rootx = sm.facingleft ? rootx-36 : rootx+36;
		fire.changeOrigin(new Vector3(rootx, rooty, 0));
		fire.update(dt);
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		fire.draw(batch);
	}
	
//	@Override
//	public void updateSkel(float dt) {
//		float delta = Gdx.graphics.getDeltaTime();
//
//		sm.animate.applyTotalTime(true, delta);
////		time += delta;
////		if ( time > holdTime ) {
////			sm.animate.mix(mixAnim, mixAnimTime, time/mixAnimTime);
////		} else 
//		{
//			sm.animate.mix(mixAnim, 1f, 1f);
//		}
//		sm.animate.setPos(sm.phyState.body.getPos(), -8f, 0f);
//
//		sm.animate.updateSkel(delta);
//	}
	
	@Override
	public void setIdle() {
		sm.getState(NPCStateEnum.IDLE).transistionIn();
	}
	
	@Override
	public void transistionIn() {
//		time = 0;
		sm.animate.bindPose();
		sm.animate.setCurrent("walk_torch", true);
		if ( childState == null || childState == this) {
			childState = sm.getState(NPCStateEnum.IDLE);
		}
		if ( !sm.phyState.body.isStable() )
			sm.phyState.body.setVel(sm.facingleft ? -Util.PLAYERWALKSPEED: Util.PLAYERWALKSPEED, 0, 0);
		float rootx = sm.animate.findBone("right hand").getWorldX();
		float rooty = sm.animate.findBone("right hand").getWorldY();
		rooty += 6;
		rootx = sm.facingleft ? rootx-36 : rootx+36;
		fire = new FireEmitter(	new Vector3(rootx, rooty, 0), 3, 0.27f, 60.0f, true,1f, 1f, 1, Util.FIRESPEED*0.15f);
	}
	
	@Override
	public boolean transistionOut() {
		return true;
	}
}
