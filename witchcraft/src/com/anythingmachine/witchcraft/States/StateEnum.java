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
	};
	
	public int getID() {
		return -1;
	}
	public int getSize() {
		return StateEnum.values().length;
	}
}
