package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;

public enum NPCType {
	CIV{
		@Override
		public NPCStateEnum getAttackState() {
			return NPCStateEnum.IDLE;
		}
	},
	KNIGHT {
		@Override
		public NPCStateEnum getWorkingState() {
			return NPCStateEnum.WORKING;
		}
	},
	ARCHER {
		@Override
		public NPCStateEnum getAttackState() {
			return NPCStateEnum.ARCHERATTACK;
		}
		@Override
		public NPCStateEnum getWorkingState() {
			return NPCStateEnum.WORKING;
		}
	};
	public NPCStateEnum getAttackState() {
		return NPCStateEnum.ATTACKING;
	}
	public NPCStateEnum getWorkingState() {
		return NPCStateEnum.WORKING;
	}
}
