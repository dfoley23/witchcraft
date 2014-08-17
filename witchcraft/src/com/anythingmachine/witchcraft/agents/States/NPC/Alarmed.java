package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.GamePlayUI.UILayout;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;

public class Alarmed extends NPCState {

	public Alarmed(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
	}

	@Override
	public void transistionIn() {
		WitchCraft.playSound("data/sounds/alert.ogg");
		UILayout screenPos = sm.facingleft ? UILayout.RIGHT : UILayout.LEFT;
		GamePlayManager.player.addAlarmToUI(screenPos);
	}

	@Override
	public void checkAttack() {
		sm.canseeplayer = sm.facingleft == GamePlayManager.player.getX() < sm.phyState.body
				.getX();
		if (sm.canseeplayer) {
			if (GamePlayManager.player.inAlert()) {
				if (sm.onscreen) {
					setAttack();
				}
			}
		}
	}

}
