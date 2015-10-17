package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.userinterface.GamePlayUI.UILayout;
import com.anythingmachine.witchcraft.WitchCraft;

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
				.getX() && sm.currentNode.getSet() == GamePlayManager.player.getAINode().getSet();
		if (sm.canseeplayer) {
			if (GamePlayManager.player.inAlert()) {
				if (sm.onscreen) {
					setAttack();
				}
			}
		}
	}

}
