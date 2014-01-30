package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;

public class Running extends State{

	public Running(StateMachine sm, StateEnum name) {
		super(sm, name);
	}

	
	@Override
	public void setCastSpell() {
		if ( parent != StateEnum.DUPESKIN ) {
			super.setCastSpell();
		}
	}

	
	@Override
	public void setRun() {
	}

}
