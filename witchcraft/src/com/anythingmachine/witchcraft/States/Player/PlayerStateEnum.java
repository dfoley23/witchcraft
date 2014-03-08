package com.anythingmachine.witchcraft.States.Player;

public enum PlayerStateEnum {
	DEFAULT {
	},
	IDLE { 
	},
	WALKING {
	}, 
	RUNNING{
	}, 
	JUMPING{
		@Override 
		public int getNextPower() {
			return PlayerStateEnum.MINDCONTROLPOWER.getID();
		}
	},
	FLYING{
	},
	LANDING{
	},FALLING{
	},
	ATTACKING{
	},
	DEAD{
	},
	ARROWDEAD{
	},
	DUPESKIN{
	},
	CASTSPELL {
	},	/*POWERS*/
	DUPESKINPOWER{
		@Override
		public int getNextPower() {
			return PlayerStateEnum.SHAPECROWPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 3;
		}
	},
	MINDCONTROLPOWER{
		@Override
		public int getNextPower() {
			return PlayerStateEnum.INVISIBLEPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 1;
		}
	},
	INVISIBLEPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.DUPESKINPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 2;
		}
	},
	SHAPECROWPOWER {
		@Override
		public int getNextPower() {
			//TODO link more powers later
			return PlayerStateEnum.JUMPING.getID();
		}
		@Override
		public int getPowerIndex() {
			return 4;
		}
	},
	SHAPECATPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.INTANGIBLEPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 4;
		}
	},
	INTANGIBLEPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.JUMPING.getID();
		}
		@Override
		public int getPowerIndex() {
			return 6;
		}
	};

	
	public int getID() {
		return this.ordinal();
	}
	
	public int getNextPower() {
		return PlayerStateEnum.JUMPING.getID();
	}
	public int getPowerIndex() {
		return 0;
	}
	public int getSize() {
		return PlayerStateEnum.values().length;
	}
}
