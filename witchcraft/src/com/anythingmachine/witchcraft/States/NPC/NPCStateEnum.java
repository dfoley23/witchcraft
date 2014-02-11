package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.NPCStateMachine;
import com.badlogic.gdx.math.Vector2;


public enum NPCStateEnum {
	WALKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Walking(sm, NPCStateEnum.WALKING);
		}
	},
	RUNNING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Running(sm, NPCStateEnum.RUNNING);
		}
	},
	IDLE {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED,
					GOINGTOSLEEP, GOINGTOEAT, GOINGTOWORK, HUNTING, MOBBING,
					ALARMED };
		}
	},
	DEAD {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DEAD };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Dead(sm, NPCStateEnum.DEAD);
		}
	},
	ATTACKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DEAD, IDLE };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Attacking(sm, NPCStateEnum.ATTACKING);
		}
	},
	EATING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED,
					GOINGTOSLEEP, HUNTING, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Eating(sm, NPCStateEnum.EATING);
		}
	},
	SLEEPING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOWORK, GOINGTOEAT, CLEANING,
					HUNTING, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Sleeping(sm, NPCStateEnum.SLEEPING);
		}
	},
	GOINGTOTALK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TIRED, GOINGTOEAT, MOBBING,
					ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm, Vector2 target) {
			return new GoingTo(sm, NPCStateEnum.GOINGTOTALK, target);
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.TALKING;
		}
	},
	TALKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TIRED, GOINGTOEAT, MOBBING,
					ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Talking(sm, NPCStateEnum.TALKING);
		}
	},
	CLEANING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					GOINGTOSLEEP, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Cleaning(sm, NPCStateEnum.CLEANING);
		}
	},
	HUNTING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					GOINGTOSLEEP, SLEEPING, EATING, MOBBING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Hunting(sm, NPCStateEnum.HUNTING);
		}
	},
	GUARDING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					ALARMED, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Guarding(sm, NPCStateEnum.GUARDING);
		}
	},
	PATROLLING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					ALARMED, GUARDING, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Patrolling(sm, NPCStateEnum.PATROLLING);
		}
	},
	FOLLOWING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOEAT, TALKING, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Following(sm, NPCStateEnum.FOLLOWING);
		}
	},
	LEADING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Leading(sm, NPCStateEnum.LEADING);
		}
	},
	GOINGTOEAT {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { EATING, TALKING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm, Vector2 target) {
			return new GoingTo(sm, NPCStateEnum.EATING, target);
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.EATING;
		}
	},
	MOBBING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, GOINGTOEAT };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Mobbing(sm, NPCStateEnum.MOBBING);
		}
	},
	DRINKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DRUNK };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Drinking(sm, NPCStateEnum.DRINKING);
		}
	},
	DRUNK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { TIRED, SLEEPING, WALKING, GOINGTOSLEEP,
					GOINGTOEAT, TALKING, ALARMED, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Drunk(sm, NPCStateEnum.DRUNK);
		}
	},
	ALARMED {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, WALKING, GOINGTOEAT,
					GOINGTOSLEEP, TALKING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Alarmed(sm, NPCStateEnum.ALARMED);
		}
	},
	TIRED {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WALKING, GOINGTOEAT, GOINGTOSLEEP,
					TALKING, ALARMED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Tired(sm, NPCStateEnum.TIRED);
		}
	},
	THEIVING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WALKING, ALARMED, FOLLOWING, RUNNING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Theiving(sm, NPCStateEnum.THEIVING);
		}
	},
	GOINGTOWORK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WORKING, TALKING, ALARMED, MOBBING };
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.WORKING;
		}
	},
	GOINGTOSLEEP {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { SLEEPING, ALARMED, TALKING, TIRED };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm, Vector2 target) {
			return new GoingTo(sm, NPCStateEnum.GOINGTOSLEEP, target);
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.SLEEPING;
		}
	},
	RETURNFROMWORK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOEAT, GOINGTOSLEEP, ALARMED,
					FOLLOWING, MOBBING, CLEANING, TALKING, IDLE };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm, Vector2 target) {
			return new GoingTo(sm, NPCStateEnum.RETURNFROMWORK, target);
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.IDLE;
		}
	},
	WORKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { TIRED, ALARMED, FOLLOWING, MOBBING,
					CLEANING, TALKING, IDLE };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new Working(sm, NPCStateEnum.WORKING);
		}
	},
	GOINGTOPATROL {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WORKING, TALKING, ALARMED, MOBBING };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm, Vector2 target) {
			return new GoingTo(sm, NPCStateEnum.GOINGTOPATROL, target);
		}
		@Override
		public NPCStateEnum goingTo() {
			return NPCStateEnum.PATROLLING;
		}
	},
	ARCHERATTACK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DEAD, IDLE };
		}
		@Override
		public NPCState constructState(NPCStateMachine sm) {
			return new ArcherAttack(sm, NPCStateEnum.ARCHERATTACK);
		}
	};


	public NPCState constructState(NPCStateMachine sm) {
		return new Idle(sm, NPCStateEnum.IDLE);
	}
	
	public NPCState constructState(NPCStateMachine sm, Vector2 target) {
		return new GoingTo(sm, NPCStateEnum.GOINGTOWORK, target);
	}

	public NPCStateEnum goingTo() {
		return NPCStateEnum.IDLE;
	}
	
	public NPCStateEnum[] getFollowUpStates() {
		return NPCStateEnum.values();
	}

	public int getSize() {
		return NPCStateEnum.values().length;
	}
	
	public String getName() {
		return this.toString();
	}

}
