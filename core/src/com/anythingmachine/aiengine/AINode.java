package com.anythingmachine.aiengine;

import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.Util.Util.EntityType;

public class AINode extends Entity {
    private int set;
    
    public AINode(int set) {
	this.set = set;
	this.type = EntityType.AINODE;
    }
    
    public int getSet() {
	return set;
    }
    
    
}
