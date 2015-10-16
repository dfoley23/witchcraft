package com.anythingmachine.agents.player.items;

import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.physicsEngine.particleEngine.particles.TexturedBodyParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class InventoryObject extends TexturedBodyParticle {
    public InventoryType type;
    
    public InventoryObject(Vector3 pos, Sprite sprite, Vector3 ext,InventoryType name) {
	super(pos,EntityType.INVENTORYOBJECT, sprite, ext);
	type = name;
    }
	
}
