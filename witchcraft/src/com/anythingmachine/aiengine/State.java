package com.anythingmachine.aiengine;

import com.anythingmachine.witchcraft.Util.Util;

public enum State {
	IDLE {
	},
	WALKING {
		@Override
		public boolean canWalk(StateMachine sm) {
			return false;
		}
	},
	RUNNING {
		@Override
		public boolean canRun(StateMachine sm) {
			return false;
		}
	},
	JUMPING {
		@Override
		public boolean canBeIdle(StateMachine sm) {
			return false;
		}

		@Override
		public boolean canFly(StateMachine sm) {
			return false;
		}

		@Override
		public boolean startingToFly(StateMachine sm) {
			return true;
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}

		@Override
		public boolean canWalk(StateMachine sm) {
			return false;
		}
		@Override
		public boolean canRun(StateMachine sm) {
			return false;
		}
	},
	FLYING {
		@Override
		public State land(StateMachine sm) {
			return LANDING;
		}

		@Override
		public boolean canBeIdle(StateMachine sm) {
			return false;
		}

		@Override
		public boolean canLand(StateMachine sm) {
			return true;
		}

		@Override
		public boolean canFly(StateMachine sm) {
			return false;
		}

		@Override
		public boolean startingToFly(StateMachine sm) {
			return true;
		}

		@Override
		public float getInputSpeed(StateMachine sm) {
			return Util.PLAYERFLYSPEED;
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}
		@Override
		public boolean canRun(StateMachine sm) {
			return false;
		}
	},
	LANDING {

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}
	},
	FALLING {
		@Override
		public boolean canWalk(StateMachine sm) {
			return false;
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}
		@Override
		public boolean canRun(StateMachine sm) {
			return false;
		}
	},
	ATTACK {
		@Override
		public boolean canBeIdle(StateMachine sm) {
			return sm.animate.isTImeOverThreeQuarters(0);
		}
		@Override
		public boolean canAttack(StateMachine sm) {
			return false;
		}
	},
	DEAD {
		@Override
		public boolean canFly(StateMachine sm) {
			return false;
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}
		@Override
		public boolean canRun(StateMachine sm) {
			return false;
		}
		@Override
		public boolean canWalk(StateMachine sm) {
			return false;
		}
	};
	public boolean canAttack(StateMachine sm) {
		return true;
	}
	public boolean startingToFly(StateMachine sm) {
		return false;
	}

	public State land(StateMachine sm) {
		return this;
	}

	public boolean canBeIdle(StateMachine sm) {
		return !sm.testORtest("usingpower", "dupeskin");
	}

	public boolean canLand(StateMachine sm) {
		return false;
	}

	public boolean canFly(StateMachine sm) {
		return !sm.testtesttestOR("usingpower", "dupeskin", "usingdupeskin");
	}

	public boolean canWalk(StateMachine sm) {
		return !sm.testORtest("usingpower", "dupeskin");
	}

	public boolean canRun(StateMachine sm) {
		return !sm.testORtest("usingpower", "dupeskin");
	}
	public float getInputSpeed(StateMachine sm) {
		if (!sm.testORtest("usingpower", "dupeskin"))
			return Util.PLAYERWALKSPEED;
		else
			return 0;
	}

	public boolean canCastSpell(StateMachine sm) {
		return !sm.testORtest("usingpower", "dupeskin");
	}
}
