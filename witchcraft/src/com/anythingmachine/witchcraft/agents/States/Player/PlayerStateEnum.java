package com.anythingmachine.witchcraft.agents.States.Player;

import com.anythingmachine.aiengine.PlayerStateMachine;

public enum PlayerStateEnum {
	IDLE {
	},
	WALKING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Walking(sm, this);
		}
	},
	RUNNING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Running(sm, this);
		}
	},
	JUMPING {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.MINDCONTROLPOWER.getID();
		}
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Jumping(sm, this);
		}
	},
	FLYING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Flying(sm, this);
		}
	},
	LANDING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Landing(sm, this);
		}
	},
	FALLING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Falling(sm, this);
		}
	},
	ATTACKING {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Attacking(sm, this);
		}
	},
	DEAD {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Dead(sm, this);
		}
	},
	CINEMATIC {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Cinematic(sm, this);
		}
	},
	ARROWDEAD {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new ArrowDead(sm, this);
		}
	},
	SHAPESHIFTINTERCROW {
		@Override
		public int getNextPower() {
			// TODO link more powers later
			return PlayerStateEnum.MINDCONTROLPOWER.getID();
		}

		@Override
		public int getPowerIndex() {
			return 4;
		}
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new ShapeShiftIntermediate(sm, this, PlayerStateEnum.SHAPECROWPOWER);
		}
	},
	SHAPESHIFTINTERCAT {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.INTANGIBLEPOWER.getID();
		}

		@Override
		public int getPowerIndex() {
			return 4;
		}
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new LoadingState(sm, this);
		}
	},
	DUPESKIN {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new LoadingState(sm, this);
		}
	},
	CASTSPELL {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new CastSpell(sm, this);
		}
	}, /* POWERS */
	DUPESKINPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.SHAPESHIFTINTERCROW.getID();
		}

		@Override
		public int getPowerIndex() {
			return 3;
		}
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new DupeSkinPower(sm, this);
		}
	},
	MINDCONTROLPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.INVISIBLEPOWER.getID();
		}

		@Override
		public int getPowerIndex() {
			return 1;
		}
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new MindControlPower(sm, this);
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
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new Invisible(sm, this);
		}
	},
	SHAPECROWPOWER {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new ShapeCrow(sm, this);
		}
	},
	SHAPECATPOWER {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new LoadingState(sm, this);
		}
	},
	INTANGIBLEPOWER {
		@Override
		public int getNextPower() {
			return PlayerStateEnum.MINDCONTROLPOWER.getID();
		}

		@Override
		public int getPowerIndex() {
			return 6;
		}

		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new LoadingState(sm, this);
		}
	},
	LOADINGSTATE {
		@Override
		public PlayerState constructState(PlayerStateMachine sm) {
			return new LoadingState(sm, this);
		}
	};

	public int getID() {
		return this.ordinal();
	}

	public int getNextPower() {
		return PlayerStateEnum.MINDCONTROLPOWER.getID();
	}

	public int getPowerIndex() {
		return 0;
	}

	public PlayerState constructState(PlayerStateMachine sm) {
		return new Idle(sm, PlayerStateEnum.IDLE);
	}

	public int getSize() {
		return PlayerStateEnum.values().length;
	}
}
