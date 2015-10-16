package com.anythingmachine.agents.States.NPC;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Pointer;
import com.anythingmachine.Util.Util;
import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.agents.States.Transistions.ActionEnum;
import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.witchcraft.WitchCraft;
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
	//TODO make sword obey box2d lock rules
	private boolean inactivateSwordNextStep = false;

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

		if (childState.name == this.name && sm.animate.isTimeOverAHalf(dt)) {
			swordBody.setTransform((sword.getWorldX()) * Util.PIXEL_TO_BOX, (sword.getWorldY()) * Util.PIXEL_TO_BOX,
					sm.facingleft ? -sword.getWorldRotation() * Util.DEG_TO_RAD
							: sword.getWorldRotation() * Util.DEG_TO_RAD);
			if (!swordBody.isActive())
				swordBody.setActive(true);

		}
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) > 175) {
			if (Math.abs(GamePlayManager.player.getY() - sm.phyState.body.getY()) < 128) {
				if ( sm.phyState.body.getX() > GamePlayManager.player.getX() && sm.phyState.body.getVelX() > 0) {
					sm.phyState.body.setXVel(sm.phyState.body.getVelX()*-1);
				} else if ( sm.phyState.body.getX() < GamePlayManager.player.getX() && sm.phyState.body.getVelX() < 0) {
					sm.phyState.body.setXVel(sm.phyState.body.getVelX()*-1);
					
				}
			}
			sm.facingleft = sm.phyState.body.getVelX() < 0;
			childState.setRun();
		}

		// float delta = Gdx.graphics.getDeltaTime();

		updateSkel(dt);
	}

	@Override
	public void setGoingTo(float dt) {
		System.out.println(sm.me.level);
		if (sm.me.level < GamePlayManager.currentlevel
				&& (sm.phyState.body.getX() + Util.PLAYERRUNSPEED * dt) > GamePlayManager.levels.get(sm.me.level)
				&& sm.me.level < GamePlayManager.levels.size() - 1) {
			sm.state.switchLevel(sm.me.level + 1);
		} else if (sm.me.level > GamePlayManager.currentlevel
				&& (sm.phyState.body.getX() + (-Util.PLAYERRUNSPEED * dt) < 64) && sm.me.level > 0) {
			sm.state.switchLevel(sm.me.level - 1);
		}
	}

	@Override
	public void takeAction(float dt) {
	}

	@Override
	public void hitStairs(Stairs stairs) {
//		float ypos = sm.phyState.body.getY();
//		float xpos = sm.phyState.body.getX();
//		float lowestPoint = stairs.getDownPosY();
//		float highestPoint = stairs.getUpPosY();
//		boolean slantRight = stairs.slantRight();
//		// up the stairs
//		if (ypos + 32 < GamePlayManager.player.getY() && ypos + 8 > lowestPoint && ypos - 8 < lowestPoint
//				&& ((slantRight && !sm.facingleft) || (!slantRight && sm.facingleft))
//				&& (xpos + 8 > stairs.getDownPosX() && xpos - 8 < stairs.getDownPosX())) {
//			Random rand = new Random();
//			int choice = rand.nextInt(4) + 3;
//			if (choice > 2) {
//				sm.currentPlatform = stairs;
//				sm.currentStairs = stairs;
//			}
//		}
//		// down the stairs
//		else if ((ypos - 32 > GamePlayManager.player.getY() || stairs.forceWalkDown()) 
//				&& ypos + 8 > highestPoint && ypos - 8 < highestPoint
//				&& ((slantRight && sm.facingleft) || (!slantRight && !sm.facingleft))
//				&& (xpos + 8 > stairs.getUpPosX() && xpos - 8 < stairs.getUpPosX())) {
//				sm.currentPlatform = stairs;
//				sm.currentStairs = stairs;
//		}
	}

	@Override
	protected void upStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// walk up stairs
		if (sm.phyState.body.getY() + 32 < GamePlayManager.player.getY() 
				&& ((!sm.facingleft && slantRight) || (sm.facingleft && !slantRight))) {
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
			sm.goingUpStairs = true;
			// sm.state.setState(PlayerStateEnum.ONSTAIRS);
		} else {
			sm.goingUpStairs = false;
			sm.goingDownStairs = false;
		}
	}

	@Override
	protected void downStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// down stairs
		if ((slantRight && sm.facingleft) || (!slantRight && !sm.facingleft)) {
			if (stairs.forceWalkDown() || sm.phyState.body.getY() - 32 > GamePlayManager.player.getY()) {
				sm.currentPlatform = stairs;
				sm.currentStairs = stairs;
				sm.goingDownStairs = true;
				// sm.state.setState(PlayerStateEnum.ONSTAIRS);
			} else {
				sm.goingUpStairs = false;
				sm.goingDownStairs = false;
			}
		} else {
			sm.goingUpStairs = false;
			sm.goingDownStairs = false;
		}
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
			return new ActionEnum[] { ActionEnum.WANDER, ActionEnum.THINK, ActionEnum.TURN };
		} else if (!sm.onscreen) {
			return new ActionEnum[] { ActionEnum.WANDER };
		}
		return ActionEnum.values();
	}

	@Override
	public void checkInBounds() {
		if (!WitchCraft.cam.inBigBounds(sm.phyState.body.getPos()) && sm.inlevel) {
			NPCState temp = sm.state;
			sm.setState(NPCStateEnum.INACTIVE);
			sm.state.setParent(temp);
		}
	}

	@Override
	public void setAttack() {
		if (Math.abs(GamePlayManager.player.getX() - sm.phyState.body.getX()) < 75 && 
				Math.abs(GamePlayManager.player.getY() - sm.phyState.body.getY()) < 75 && !isAttacking) {
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
			sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body.getX()
					&& GamePlayManager.currentlevel <= sm.me.level;
			setRun();
		}
	}

	@Override
	public boolean transistionOut() {
		if ( GamePlayManager.world.isLocked() ) {
			inactivateSwordNextStep = true;
			return false;
		} else {
			swordBody.setActive(false);
		}
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
		def.position.set(new Vector2(this.sm.phyState.body.getX(), this.sm.phyState.body.getY()));
		swordBody = GamePlayManager.world.createBody(def);
		swordBody.setUserData(new Pointer(sm.me).setType(EntityType.SWORD));
		PolygonShape shape = new PolygonShape();
		// shape.setAsBox(16 * Util.PIXEL_TO_BOX, 150 * Util.PIXEL_TO_BOX);
		shape.setAsBox(16 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX, new Vector2(0, 93).scl(Util.PIXEL_TO_BOX), 0f);
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
