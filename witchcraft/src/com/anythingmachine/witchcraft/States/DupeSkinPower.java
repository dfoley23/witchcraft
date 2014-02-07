package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.agents.NonPlayer;

public class DupeSkinPower extends CastSpell {
	private float lasttime = -150000;

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
		sm.dupeSkin = npc.getSkin().getName();
		setDupeSkin();
	}
	
	@Override
	public void setDupeSkin() {
		float now = System.currentTimeMillis();		
		if ( !sm.dupeSkin.isEmpty() && now - lasttime > 150000) {
			lasttime = now;
			sm.animate.switchSkin(sm.dupeSkin);
			sm.setState(PlayerStateEnum.DUPESKIN);
			sm.animate.bindPose();
		}
	}
	
	@Override
	public void transistionOut() {
		sm.dupeSkin = "";
	}
	
	
}
