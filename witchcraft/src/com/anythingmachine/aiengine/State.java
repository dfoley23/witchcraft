package com.anythingmachine.aiengine;

import java.util.EnumSet;
import java.util.Set;

public enum State {
    IDLE {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(WALKING, JUMPING, ATTACK);
        }
    },
    WALKING {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(JUMPING, IDLE, ATTACK);
        }
    },
    JUMPING {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(FLYING, LANDING);
        }
    },
    FLYING {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(LANDING);
        }
        @Override
        public State land() {
        	return LANDING;
        }
    },
    LANDING {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(IDLE, WALKING);
        }
    },
    ATTACK {
        @Override
        public Set<State> possibleFollowUps() {
            return EnumSet.of(IDLE, WALKING, JUMPING);
        }
    },
    DEAD;
    public Set<State> possibleFollowUps() {
        return EnumSet.noneOf(State.class);
    }
    public State land() {
    	return this;
    }
}

