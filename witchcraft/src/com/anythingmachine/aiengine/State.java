package com.anythingmachine.aiengine;

import java.util.EnumSet;
import java.util.Set;

import com.anythingmachine.witchcraft.Util.Util;

public enum State {
    IDLE {
    },
    USINGPOWER {
    	@Override
    	public boolean canBeIdle() {
    		return false;
    	}
    	@Override
        public boolean canCastSpell() {
        	return false;
        }
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
    	@Override
        public boolean canCastSpell() {
        	return false;
        }
    	@Override
    	public boolean canWalk() {
    		return false;
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
        @Override
        public float getInputSpeed(){
            return Util.PLAYERFLYSPEED;
        }
    	@Override
        public boolean canCastSpell() {
        	return false;
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
    	@Override
        public boolean canCastSpell() {
        	return false;
        }
    },
    FALLING {
    	@Override
    	public boolean canWalk() {
    		return false;
    	}
    	@Override
        public boolean canCastSpell() {
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
    	@Override
        public boolean canCastSpell() {
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
    public float getInputSpeed(){
        return Util.PLAYERWALKSPEED;
    }
    public boolean canCastSpell() {
    	return true;
    }
}

