package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;

public class DupeSkinPower extends CastSpell {
	private float lasttime = -150000;
	private float npcScaleX;
	private float npcScaleY;

	public DupeSkinPower(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		if( !sm.dupeSkin.isEmpty() )
			setDupeSkin();
	}
	
	@Override
	public void hitNPC(NonPlayer npc) {
		sm.npc = npc;
		sm.dupeSkin = sm.npc.getSkin().getName();
		npcScaleX = sm.npc.getBodyScale().x;
		npcScaleY = sm.npc.getBodyScale().y;
		setDupeSkin();			
	}
	
	@Override
	public void setDupeSkin() {
		float now = System.currentTimeMillis();		
		System.out.println(now);
		if ( !sm.dupeSkin.isEmpty() ) {
			lasttime = now;
			sm.animate.switchSkin(sm.dupeSkin);
			sm.animate.setScale(npcScaleX,npcScaleY);
			sm.setState(PlayerStateEnum.DUPESKIN);
			sm.animate.bindPose();
		}
	}
	
	@Override
	public void transistionOut() {
		sm.dupeSkin = "";
	}
	
	@Override
	public void transistionIn() {
		super.transistionIn();
		if ( sm.npc != null ) {
			sm.dupeSkin = sm.npc.getSkin().getName();
			npcScaleX = sm.npc.getBodyScale().x;
			npcScaleY = sm.npc.getBodyScale().y;
			setDupeSkin();			
		}
	}
	
	
}
