package com.anythingmachine.aiengine;

import com.anythingmachine.witchcraft.Util.Util;

public enum State {
	IDLE {
	},
	WALKING {

		@Override
		public void setWalk(StateMachine sm) {

		}
	},
	RUNNING {
		@Override
		public void setRun(StateMachine sm) {
		}

		@Override
		public float getInputSpeed(StateMachine sm) {
			if ( sm.input.right()) {
				if (!sm.test("hitrightwall") ) {
					sm.setTestVal("hitleftwall", false);
					return Util.PLAYERRUNSPEED;
				}
			}
			else if (sm.input.left() ) {
				if (!sm.test("hitleftwall")) {
					sm.setTestVal("hitrightwall", false);
					return Util.PLAYERRUNSPEED;
				}
			}
			return 0;
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
		public void setWalk(StateMachine sm) {
		}
		@Override
		public void setRun(StateMachine sm) {
		}

	},
	FLYING {
		@Override
		public void land(StateMachine sm) {
			sm.setState(State.LANDING);
		}

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
		public float getInputSpeed(StateMachine sm) {
			if ( sm.input.right()) {
				if (!sm.test("hitrightwall") ) {
					sm.setTestVal("hitleftwall", false);
					return Util.PLAYERFLYSPEED;
				}
			}
			else if (sm.input.left() ) {
				if (!sm.test("hitleftwall")) {
					sm.setTestVal("hitrightwall", false);
					return Util.PLAYERFLYSPEED;
				}
			}
			return 0;
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}

		@Override
		public void setRun(StateMachine sm) {
		}


		@Override
		public void setWalk(StateMachine sm) {
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
		public void setWalk(StateMachine sm) {
		}

		@Override
		public boolean canCastSpell(StateMachine sm) {
			return false;
		}

		@Override
		public void setRun(StateMachine sm) {
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
		public void setRun(StateMachine sm) {
		}

		@Override
		public void setWalk(StateMachine sm) {
		}
	};
	public boolean canAttack(StateMachine sm) {
		return true;
	}

	public boolean startingToFly(StateMachine sm) {
		return false;
	}

	public void land(StateMachine sm) {
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

	public float getInputSpeed(StateMachine sm) {
		if (!sm.testORtest("usingpower", "dupeskin")) {
			if ( sm.input.right()) {
				if (!sm.test("hitrightwall") ) {
					sm.setTestVal("hitleftwall", false);
					return Util.PLAYERWALKSPEED;
				}
			}
			else if (sm.input.left() ) {
				if (!sm.test("hitleftwall")){
					sm.setTestVal("hitrightwall", false);
					return Util.PLAYERWALKSPEED;
				}
			}
		}
		return 0;
	}

	public boolean canCastSpell(StateMachine sm) {
		return !sm.testORtest("usingpower", "dupeskin");
	}

	public void setWalk(StateMachine sm) {
		if (!sm.testORtest("usingpower", "dupeskin")) {
			sm.setState(State.WALKING);
			sm.animate.bindPose();
			sm.animate.setCurrent("walk", true);
		}
	}

	public void setRun(StateMachine sm) {
		if (!sm.testORtest("usingpower", "dupeskin")) {
			sm.setState(State.RUNNING);
			sm.animate.bindPose();
			sm.animate.setCurrent("run", true);
		}
	}

}
