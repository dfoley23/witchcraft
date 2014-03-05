package com.anythingmachine.witchcraft.agents.npcs;

import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;

public enum NPCType {
	CIV{
		@Override
		public NPCStateEnum getAttackState() {
			return NPCStateEnum.IDLE;
		}
		@Override
		public boolean canAttack(){
			return false;
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
	public boolean canAttack(){
		return true;
	}
}
