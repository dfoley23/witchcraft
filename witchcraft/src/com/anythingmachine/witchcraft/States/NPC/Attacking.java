package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.Bone;

public class Attacking extends NPCState {
	private Bone sword;
	private Body swordBody;
	
	public Attacking(NPCStateMachine sm, NPCStateEnum name, NonPlayer npc ) {
		super(sm,name);
		sword = sm.animate.findBone("right hand");
		buildSwordBody(npc);

	}
	
	@Override
	public void update(float dt) {
		swordBody.setTransform((sword.getWorldX())*Util.PIXEL_TO_BOX, 
				(sword.getWorldY())*Util.PIXEL_TO_BOX, 
				sm.facingleft ? -sword.getWorldRotation()*Util.DEG_TO_RAD 
						: sword.getWorldRotation()*Util.DEG_TO_RAD);
		setIdle();
	}
	
	@Override
	public void takeAction(Action action) {
		
	}
	
	@Override
	public void setIdle() {
		if ( sm.animate.atEnd() ) {
			super.setIdle();
		}
	}
	
	private void buildSwordBody(NonPlayer npc) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.sm.phyState.body.getX(), this.sm.phyState.body.getY()));
		swordBody = WitchCraft.world.createBody(def);
		PolygonShape shape = new PolygonShape();
//		shape.setAsBox(16 * Util.PIXEL_TO_BOX, 150 * Util.PIXEL_TO_BOX);
		shape.setAsBox(16 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX,
				new Vector2(0, 93).scl(Util.PIXEL_TO_BOX), 
				0f);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = Util.CATEGORY_EVERYTHING;
		swordBody.createFixture(fixture);
		swordBody.setUserData(npc);
		shape.dispose();
	}

}
