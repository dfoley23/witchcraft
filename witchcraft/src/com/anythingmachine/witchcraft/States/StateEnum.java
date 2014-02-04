package com.anythingmachine.witchcraft.States;

public enum StateEnum {
	DEFAULT {
	},
	IDLE { 
		@Override
		public int getID() {
			return 8;
		}
	},
	WALKING {
		@Override
		public int getID() {
			return 0;
		}
	}, 
	RUNNING{
		@Override
		public int getID() {
			return 1;
		}
	}, 
	JUMPING{
		@Override
		public int getID() {
			return 2;
		}
		@Override 
		public int getNextPower() {
			return StateEnum.MINDCONTROLPOWER.getID();
		}
	},
	FLYING{
		@Override
		public int getID() {
			return 3;
		}
	},
	LANDING{
		@Override
		public int getID() {
			return 4;
		}
	},FALLING{
		@Override
		public int getID() {
			return 5;
		}
	},
	ATTACKING{
		@Override
		public int getID() {
			return 6;
		}
	},
	DEAD{
		@Override
		public int getID() {
			return 7;
		}
	},
	DUPESKIN{
		@Override
		public int getID() {
			return 9;
		}
	},
	CASTSPELL {
		@Override
		public int getID() {
			return 10;
		}
	},	/*POWERS*/
	DUPESKINPOWER{
		@Override
		public int getID() {
			return 11;
		}
		@Override
		public int getNextPower() {
			return StateEnum.SHAPECROWPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 3;
		}
	},
	MINDCONTROLPOWER{
		@Override
		public int getID() {
			return 12;
		}
		@Override
		public int getNextPower() {
			return StateEnum.INVISIBLEPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 1;
		}
	},
	INVISIBLEPOWER {
		@Override
		public int getID() {
			return 13;
		}
		@Override
		public int getNextPower() {
			return StateEnum.DUPESKINPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 2;
		}
	},
	SHAPECROWPOWER {
		@Override
		public int getID() {
			return 14;
		}
		@Override
		public int getNextPower() {
			//TODO link more powers later
			return StateEnum.JUMPING.getID();
		}
		@Override
		public int getPowerIndex() {
			return 4;
		}
	},
	SHAPECATPOWER {
		@Override
		public int getID() {
			return 15;
		}
		@Override
		public int getNextPower() {
			return StateEnum.INTANGIBLEPOWER.getID();
		}
		@Override
		public int getPowerIndex() {
			return 4;
		}
	},
	INTANGIBLEPOWER {
		@Override
		public int getID() {
			return 16;
		}
		@Override
		public int getNextPower() {
			return StateEnum.JUMPING.getID();
		}
		@Override
		public int getPowerIndex() {
			return 6;
		}
	};

	
	public int getID() {
		return -1;
	}
	public int getNextPower() {
		return StateEnum.JUMPING.getID();
	}
	public int getPowerIndex() {
		return 0;
	}
	public int getSize() {
		return StateEnum.values().length;
	}
}
