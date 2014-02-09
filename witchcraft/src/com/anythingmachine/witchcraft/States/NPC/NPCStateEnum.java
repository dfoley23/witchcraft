package com.anythingmachine.witchcraft.States.NPC;


public enum NPCStateEnum {
	WALKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 0;
		}
	},
	RUNNING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 1;
		}
	},
	IDLE {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED,
					GOINGTOSLEEP, GOINGTOEAT, GOINGTOWORK, HUNTING, MOBBING,
					ALARMED };
		}

		@Override
		public int getID() {
			return 2;
		}
	},
	DEAD {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DEAD };
		}

		@Override
		public int getID() {
			return 23;
		}
	},
	ATTACKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DEAD, IDLE };
		}

		@Override
		public int getID() {
			return 3;
		}
	},
	EATING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED,
					GOINGTOSLEEP, HUNTING, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 4;
		}
	},
	SLEEPING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOWORK, GOINGTOEAT, CLEANING,
					HUNTING, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 5;
		}
	},
	TALKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TIRED, GOINGTOEAT, MOBBING,
					ALARMED };
		}

		@Override
		public int getID() {
			return 6;
		}
	},
	CLEANING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					GOINGTOSLEEP, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 7;
		}
	},
	HUNTING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					GOINGTOSLEEP, SLEEPING, EATING, MOBBING, ALARMED };
		}

		@Override
		public int getID() {
			return 8;
		}
	},
	GUARDING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					ALARMED, MOBBING };
		}

		@Override
		public int getID() {
			return 9;
		}

	},
	PATROLLING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, TALKING, TIRED, GOINGTOEAT,
					ALARMED, GUARDING, MOBBING };
		}

		@Override
		public int getID() {
			return 10;
		}

	},
	FOLLOWING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOEAT, TALKING, MOBBING };
		}

		@Override
		public int getID() {
			return 11;
		}

	},
	LEADING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, MOBBING };
		}

		@Override
		public int getID() {
			return 12;
		}

	},
	GOINGTOEAT {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { EATING, TALKING, ALARMED };
		}

		@Override
		public int getID() {
			return 20;
		}

	},
	MOBBING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, GOINGTOEAT };
		}

		@Override
		public int getID() {
			return 13;
		}

	},
	DRINKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { DRUNK };
		}

		@Override
		public int getID() {
			return 19;
		}

	},
	DRUNK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { TIRED, SLEEPING, WALKING, GOINGTOSLEEP,
					GOINGTOEAT, TALKING, ALARMED, MOBBING };
		}

		@Override
		public int getID() {
			return 14;
		}

	},
	ALARMED {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { ATTACKING, WALKING, GOINGTOEAT,
					GOINGTOSLEEP, TALKING };
		}

		@Override
		public int getID() {
			return 15;
		}

	},
	TIRED {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WALKING, GOINGTOEAT, GOINGTOSLEEP,
					TALKING, ALARMED };
		}

		@Override
		public int getID() {
			return 16;
		}

	},
	THEIVING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WALKING, ALARMED, FOLLOWING, RUNNING };
		}

		@Override
		public int getID() {
			return 17;
		}
	},
	GOINGTOWORK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { WORKING, TALKING, ALARMED, MOBBING };
		}

		@Override
		public int getID() {
			return 21;
		}

	},
	GOINGTOSLEEP {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { SLEEPING, ALARMED, TALKING, TIRED };
		}

		@Override
		public int getID() {
			return 22;
		}

	},
	RETURNFROMWORK {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { GOINGTOEAT, GOINGTOSLEEP, ALARMED,
					FOLLOWING, MOBBING, CLEANING, TALKING, IDLE };
		}

		@Override
		public int getID() {
			return 24;
		}

	},
	WORKING {
		@Override
		public NPCStateEnum[] getFollowUpStates() {
			return new NPCStateEnum[] { TIRED, ALARMED, FOLLOWING, MOBBING,
					CLEANING, TALKING, IDLE };
		}

		@Override
		public int getID() {
			return 18;
		}
	};

	public NPCStateEnum[] getFollowUpStates() {
		return NPCStateEnum.values();
	}

	public int getID() {
		return -1;
	}

	public int getSize() {
		return NPCStateEnum.values().length;
	}
}
