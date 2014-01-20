package com.anythingmachine.witchcraft.agents;

import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.aiengine.UtilityAI.AIState;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.Arrow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Archer extends NonPlayer {
	private Arrow arrow;
	private boolean shotArrow = false;
	private Bone arrowBone;
	
	public Archer (String skinname, String atlasname, Vector2 pos, Vector2 bodyScale) {
		super(skinname, atlasname,  pos, bodyScale);
		arrow = new Arrow(new Vector3(0, 0, 0),	new Vector3(0, 0, 0));
		arrowBone = animate.findBone("right hand");
	}

	@Override 
	public void update(float dt) {
		if ( state != AIState.SHOOTARROW ) {
			shotArrow = false;
		} else if( !shotArrow && animate.getTime() > animate.getCurrentAnimTime() *0.75f ) {
			arrow.setPos(arrowBone.getWorldX() + (facingLeft ? -128 : 128), arrowBone.getWorldY(), 0);
			arrow.pointAtTarget(WitchCraft.player.getPosPixels(), 650);
			shotArrow = true;
		}
		super.update(dt);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
	@Override
	protected void handleState(AIState state) {
		switch (state) {
		case SHOOTARROW:			
			if ( WitchCraft.player.getPosPixels().x < body.getPos2D().x) {
				facingLeft = true;
			} else {
				facingLeft = false;
			}
			animate.setFlipX(facingLeft);
			body.setVel(0, body.getVel().y, 0f);
			animate.bindPose();
			animate.setCurrent("drawbow", false);
			break;
		default:
			break;
		}
	}

	@Override
	protected void setupAnimations(String skinname,  String atlasname) {
		SkeletonBinary sb = new SkeletonBinary((TextureAtlas)WitchCraft.assetManager.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));
		animate = new AnimationManager(skinname, body.getPos(), bodyScale, true, sd);

		animate.addAnimation("idle", sd.findAnimation("idle"));
		animate.addAnimation("walk", sd.findAnimation("walk"));
		animate.addAnimation("run", sd.findAnimation("run"));
		animate.addAnimation("swordattack", sd.findAnimation("overheadattack"));
		animate.addAnimation("drawbow", sd.findAnimation("drawbow"));

		animate.setCurrent("idle", true);
	}

	@Override
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
