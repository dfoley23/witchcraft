package com.anythingmachine.witchcraft.agents.player.items;

import com.anythingmachine.physicsEngine.particleEngine.particles.TexturedBodyParticle;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class InventoryObject extends TexturedBodyParticle {
    public InventoryType type;
    
    public InventoryObject(Vector3 pos, Sprite sprite, Vector3 ext,InventoryType name) {
	super(pos,EntityType.INVENTORYOBJECT, sprite, ext);
	type = name;
    }
	
}
