package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
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
	private NPCState childState;
	private float swingTime;

	public Attacking(NPCStateMachine sm, NPCStateEnum name) {
		super(sm, name);
		sword = sm.animate.findBone("right hand");
		buildSwordBody();
	}

	@Override
	public void update(float dt) {
		checkGround();
		checkInBounds();
		setAttack();
		setIdle();

		if ( swingTime < 0 ) {
			swordBody.setAwake(false);
		} else if ( swingTime <= 1) {
			swingTime -= dt;
		}
		swordBody.setTransform((sword.getWorldX()) * Util.PIXEL_TO_BOX,
				(sword.getWorldY()) * Util.PIXEL_TO_BOX,
				sm.facingleft ? -sword.getWorldRotation() * Util.DEG_TO_RAD
						: sword.getWorldRotation() * Util.DEG_TO_RAD);
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) > 64) {
			sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body
					.getX() && GamePlayManager.currentlevel <= sm.npc.level;
			childState.setRun();
		}
	}

	public void takeAction(float dt) {
	}
	@Override
	public ActionEnum[] getPossibleActions() {
		if ( !sm.inlevel ) {
			return new ActionEnum[] { ActionEnum.WANDER, ActionEnum.THINK, ActionEnum.TURN };
		} else if ( !sm.onscreen ) {
			return new ActionEnum[] { ActionEnum.WANDER };	
		}
		return ActionEnum.values();
	}

	@Override
	public void checkInBounds() {
		if (!WitchCraft.cam.inBigBounds(sm.phyState.body.getPos())) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}

	@Override
	public void setAttack() {
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) < 128 ) {
			if (swingTime > 1f) {
				sm.phyState.body.stop();
				sm.animate.bindPose();
				sm.animate.setCurrent("overheadattack", true);
				swingTime = 1;
			}
		} 
	}

	@Override
	public void transistionIn() {
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) < 64) {
			sm.phyState.body.stop();
			sm.animate.bindPose();
			sm.animate.setCurrent("overheadattack", true);
			swordBody.setAwake(false);
		} else {
			sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body
					.getX() && GamePlayManager.currentlevel <= sm.npc.level;
			sm.state.setRun();
			swordBody.setAwake(true);
		}
		swingTime = 2;
		childState = sm.getState(NPCStateEnum.RUNNING);
	}

	@Override
	public void takeAction(Action action) {

	}

	@Override
	public void setIdle() {
		if (GamePlayManager.player.dead()) {
			super.setIdle();
		}
	}

	private void buildSwordBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(this.sm.phyState.body.getX(),
				this.sm.phyState.body.getY()));
		swordBody = GamePlayManager.world.createBody(def);
		swordBody.setUserData(new Entity().setType(EntityType.SWORD));
		PolygonShape shape = new PolygonShape();
		// shape.setAsBox(16 * Util.PIXEL_TO_BOX, 150 * Util.PIXEL_TO_BOX);
		shape.setAsBox(16 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX,
				new Vector2(0, 93).scl(Util.PIXEL_TO_BOX), 0f);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_ITEMS;
		fixture.filter.maskBits = Util.CATEGORY_PLAYER | Util.CATEGORY_NPC;
		swordBody.createFixture(fixture);
		swordBody.setUserData(sm.me);
		shape.dispose();
	}

}
