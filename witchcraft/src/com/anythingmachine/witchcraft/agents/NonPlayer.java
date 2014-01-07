package com.anythingmachine.witchcraft.agents;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.aiengine.UtilityAI.AIState;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class NonPlayer extends Agent {
	protected KinematicParticle body;
	protected boolean facingLeft;
	protected boolean inAir;
	protected UtilityAI behavior;
	protected float aiChoiceTime = 0.f;
	protected AIState state = AIState.IDLE;
	protected boolean onGround;
	protected AnimationManager animate;
	
	public NonPlayer(String skinname, String atlasname, Vector2 pos) {
		this.curGroundSegment = 0;
		ArrayList<Vector2> points = WitchCraft.ground.getCurveBeginPoints();
		for (int i = 0; i < points.size(); i++) {
			if (pos.x > points.get(i).x) {
				this.curGroundSegment = i;
			}
		}

		curCurve = WitchCraft.ground.getCurve(curGroundSegment);

		this.body = new KinematicParticle(
				new Vector3(pos.x, WitchCraft.ground.findPointOnCurve(
						curGroundSegment, 32f).y, 0f), -50f);
		WitchCraft.rk4System.addParticle(body);

		setupAnimations(skinname, atlasname);

		setupAI();
	}

	public void update(float dT) {
		float delta = Gdx.graphics.getDeltaTime();

		aiChoiceTime += dT;
		if (aiChoiceTime > 7) {
			AIState old = state;
			state = behavior.ChooseAction();
			aiChoiceTime = 0;
			switch (state) {
			case WALKINGLEFT:
				body.setVel(-50f, body.getVel().y, 0f);
				animate.setCurrent("walk", true);
				facingLeft = true;
				if (old != AIState.WALKINGLEFT) {
					animate.bindPose();
				}
				break;
			case WALKINGRIGHT:
				body.setVel(50f, body.getVel().y, 0f);
				animate.setCurrent("walk", true);
				facingLeft = false;
				if (old != AIState.WALKINGRIGHT) {
					animate.bindPose();
				}
				break;
			case IDLE:
				body.setVel(0, 0, 0);
				animate.setCurrent("idle", true);
				if (old != AIState.IDLE) {
					animate.bindPose();
				}
				break;
			case SWORDATTACK:
				body.setVel(0, body.getVel().y, 0f);
				animate.bindPose();
				animate.setCurrent("sword-attack", true);
				break;
			default:	
				handleState(state);
//				body.setVel(50f, body.getVel().y, 0f);
//				animate.setCurrent("walk", true);
//				facingLeft = false;
//				if (old != AIState.WALKINGRIGHT) {
//					animate.bindPose();
//				}
				break;
			}
		}
		Vector3 pos = body.getPos();
		if (pos.x > curCurve.lastPointOnCurve().x) {
			curGroundSegment++;
			if (curGroundSegment >= WitchCraft.ground.getNumCurves()) {
				body.setVel(-50, 0, 0);
				facingLeft = !facingLeft;
			}
			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		} else if (pos.x < curCurve.firstPointOnCurve().x) {
			curGroundSegment--;
			if (curGroundSegment == 0) {
				body.setVel(50, 0, 0);
				facingLeft = !facingLeft;
			}
			curCurve = WitchCraft.ground.getCurve(curGroundSegment);
		}
		animate.setFlipX(facingLeft);
		Vector2 groundPoint = WitchCraft.ground.findPointOnCurve(
				curGroundSegment, pos.x);
		if (pos.y < groundPoint.y) {
			correctHeight(groundPoint.y);
			onGround = true;
		}
		
		animate.setPos(body.getPos(), 0, -16f);
		if ( state == AIState.SWORDATTACK && animate.atEnd()) {
			animate.applyTotalTime(false, animate.getCurrentAnimTime());
		} else {
			animate.update(delta);
		}

	}

	public void draw(SpriteBatch batch) {
		animate.draw(batch);
	}

	public Vector3 getPosPixels() {
		return body.getPos();
	}

	public int getCurSegment() {
		return curGroundSegment;
	}

	public void correctHeight(float y) {
		body.setPos(body.getPos().x, y, 0f);
	}

	public void incrementSegment() {
		this.curGroundSegment++;
	}

	public void decrementSegment() {
		this.curGroundSegment++;
	}

	protected void handleState(AIState state) {
	}
	
	protected void setupAnimations(String skinname, String atlasname) {
		SkeletonBinary sb = new SkeletonBinary(WitchCraft.assetManager.getAtlas(atlasname));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/knight.skel"));

		animate = new AnimationManager(skinname, body.getPos(), new Vector2(0.6f,
				0.7f), true, sd);
		animate.addAnimation(
				"walk",
				sb.readAnimation(
						Gdx.files.internal("data/spine/knight-walk.anim"), sd));
		animate.addAnimation(
				"idle",
				sb.readAnimation(
						Gdx.files.internal("data/spine/knight-idle.anim"), sd));
		animate.addAnimation(
				"sword-attack",
				sb.readAnimation(
						Gdx.files.internal("data/spine/knight-overheadattack.anim"), sd));
		animate.setCurrent("idle", true);

	}

	protected void setupAI() {
		Random rand = new Random();
		// ai goals and actions
		behavior = new UtilityAI();
		behavior.addGoal(new Goal("Eat", rand.nextInt(5) + 1));
		behavior.addGoal(new Goal("Sleep", rand.nextInt(5) + 1));
		behavior.addGoal(new Goal("Hunt", rand.nextInt(20) + 7));
		Action action = new Action("eatBread", AIState.IDLE);
		action.addAction(-5f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(3f, "Hunt");
		behavior.addAction(action);
		action = new Action("eatMeat", AIState.IDLE);
		action.addAction(-7f, "Eat");
		action.addAction(2f, "Sleep");
		action.addAction(3f, "Hunt");
		behavior.addAction(action);
		action = new Action("sleepInHouse", AIState.IDLE);
		action.addAction(3f, "Eat");
		action.addAction(-7f, "Sleep");
		action.addAction(5f, "Hunt");
		behavior.addAction(action);
		action = new Action("sleepInBed", AIState.IDLE);
		action.addAction(3f, "Eat");
		action.addAction(-6f, "Sleep");
		action.addAction(5f, "Hunt");
		behavior.addAction(action);
		action = new Action("standIdle", AIState.IDLE);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-5f, "Hunt");
		behavior.addAction(action);
		action = new Action("walkLeft", AIState.WALKINGLEFT);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-8f, "Hunt");
		behavior.addAction(action);
		action = new Action("walkRight", AIState.WALKINGRIGHT);
		action.addAction(2f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-4f, "Hunt");
		behavior.addAction(action);
		action = new Action("shootArrow", AIState.SHOOTARROW);
		action.addAction(1f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-4f, "Hunt");
		behavior.addAction(action);
		action = new Action("swordAttack", AIState.SWORDATTACK);
		action.addAction(1f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-6f, "Hunt");
		behavior.addAction(action);

	}
	
}
