package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;

public class Talking extends SharedState {
	private float timeout = 10;
	private float time = 0;
	
	public Talking(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}
	
	@Override
	public void transistionIn() {
		super.transistionIn();
		time = 0;
	}
	
	@Override
	public void update(float dt) {
		time += dt;
		if ( sm.hitnpc ) {
			sm.npc.setTalking(sm.me);
			time = 0;
		} else if ( time > timeout ) {
			sm.setState(parent.name);
		}
		super.update(dt);
	}
}
