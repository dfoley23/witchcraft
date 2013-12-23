package com.anythingmachine.aiengine;

import java.util.EnumSet;
import java.util.Set;

public enum State {
    IDLE {
    },
    WALKING {
    	@Override
    	public boolean canWalk() {
    		return false;
    	}
    },
    JUMPING {
        @Override 
        public boolean canBeIdle() {
        	return false;
        }
    	@Override
    	public boolean canFly() {
    		return false;
    	}
    	@Override
    	public boolean startingToFly() {
    		return true;
    	}
    },
    FLYING {
        @Override
        public State land() {
        	return LANDING;
        }
        @Override 
        public boolean canBeIdle() {
        	return false;
        }
        @Override
        public boolean canLand() {
        	return true;
        }
        @Override
        public boolean canFly( ) {
        	return false;
        }
    	@Override
    	public boolean startingToFly() {
    		return true;
    	}
    },
    LANDING {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(IDLE, WALKING);
        }
        @Override 
        public boolean canBeIdle() {
        	return false;
        }
    },
    FALLING {
    	@Override
    	public boolean canWalk() {
    		return false;
    	}
    },
    ATTACK {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(IDLE, WALKING, JUMPING);
        }
    },
    DEAD {
    	@Override
    	public boolean canFly() {
    		return false;
    	}
    };
	public boolean startingToFly() {
		return false;
	}
    public Set<State> possibleFollowUps() {
        return EnumSet.noneOf(State.class);
    }
    public State land() {
    	return this;
    }
    public boolean canBeIdle() {
    	return true;
    }
    public boolean canLand() {
    	return false;
    }
    public boolean canFly() {
    	return true;
    }
    public boolean canWalk() {
    	return true;
    }
}

