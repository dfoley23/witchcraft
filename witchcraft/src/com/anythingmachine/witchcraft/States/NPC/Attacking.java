package com.anythingmachine.witchcraft.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;
import com.anythingmachine.witchcraft.Util.Pointer;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.badlogic.gdx.Gdx;
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
	private boolean isAttacking;

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
		fixCBody();

		if (childState.name == this.name
				&& sm.animate.isTimeOverAHalf(dt)) {
			swordBody.setTransform((sword.getWorldX()) * Util.PIXEL_TO_BOX,
					(sword.getWorldY()) * Util.PIXEL_TO_BOX,
					sm.facingleft ? -sword.getWorldRotation() * Util.DEG_TO_RAD
							: sword.getWorldRotation() * Util.DEG_TO_RAD);
			if ( !swordBody.isActive() )
				swordBody.setActive(true);
				
		}
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) > 175) {
			sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body
					.getX() && GamePlayManager.currentlevel <= sm.me.level;
			childState.setRun();
		}
		
		float delta = Gdx.graphics.getDeltaTime();

		sm.animate.applyTotalTime(true, delta);

		sm.animate.setPos(sm.phyState.body.getPos(), -8f, 0f);
		sm.animate.updateSkel(dt);

	}

	@Override
	public void setGoingTo(float dt) {
		if ((sm.me.level + 1 == GamePlayManager.currentlevel && (sm.phyState.body
				.getX() + Util.PLAYERRUNSPEED * dt) > GamePlayManager.levels
				.get(sm.me.level))
				|| (sm.me.level - 1 == GamePlayManager.currentlevel && sm.phyState.body
						.getX() + (-Util.PLAYERRUNSPEED * dt) < 0)) {
			sm.state.switchLevel(GamePlayManager.currentlevel + 1);
		}
	}

	@Override
	public void takeAction(float dt) {
	}

	@Override
	public void setRun() {
		super.setRun();
		isAttacking = false;
		swordBody.setActive(false);
		childState = sm.getState(NPCStateEnum.RUNNING);
	}
	
	@Override
	public ActionEnum[] getPossibleActions() {
		if (!sm.inlevel) {
			return new ActionEnum[] { ActionEnum.WANDER, ActionEnum.THINK,
					ActionEnum.TURN };
		} else if (!sm.onscreen) {
			return new ActionEnum[] { ActionEnum.WANDER };
		}
		return ActionEnum.values();
	}

	@Override
	public void checkInBounds() {
		if (!WitchCraft.cam.inBigBounds(sm.phyState.body.getPos())
				&& sm.inlevel) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}

	@Override
	public void setAttack() {
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) < 75 && !isAttacking) {
			isAttacking = true;
			childState = this;
			sm.phyState.body.stop();
			sm.animate.bindPose();
			sm.animate.setCurrent("overheadattack", true);
		}
	}

	@Override
	public void transistionIn() {
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) < 64) {
			sm.phyState.body.stop();
			sm.animate.bindPose();
			sm.animate.setCurrent("overheadattack", true);
			isAttacking = true;
			childState = this;
		} else {
			sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body
					.getX() && GamePlayManager.currentlevel <= sm.me.level;
			setRun();
		}
	}

	@Override
	public boolean transistionOut() {
		swordBody.setActive(false);
		return true;
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
		swordBody.setUserData(new Pointer(sm.me).setType(EntityType.SWORD));
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
		shape.dispose();

		swordBody.setTransform(new Vector2(-10, 0), 0);
		swordBody.setActive(true);
	}

}
