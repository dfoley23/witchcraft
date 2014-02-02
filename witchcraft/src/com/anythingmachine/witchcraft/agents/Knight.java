package com.anythingmachine.witchcraft.agents;

import java.util.Random;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.Goal;
import com.anythingmachine.aiengine.UtilityAI;
import com.anythingmachine.aiengine.UtilityAI.AIState;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Knight extends NonPlayer {
	private Bone sword;
	private Body swordBody;

	public Knight ( String skinname, String atlasname, Vector2 pos, Vector2 bodyScale) {
		super(skinname, atlasname, pos, bodyScale);
		sword = animate.findBone("right hand");
		buildSwordBody();

	}
	
	@Override 
	public void update(float dt) {
		super.update(dt);
		swordBody.setTransform((sword.getWorldX())*Util.PIXEL_TO_BOX, 
				(sword.getWorldY())*Util.PIXEL_TO_BOX, 
				facingLeft ? -sword.getWorldRotation()*Util.DEG_TO_RAD 
						: sword.getWorldRotation()*Util.DEG_TO_RAD);

	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	@Override
	protected void handleState(AIState state) {
		switch (state) {
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
		action = new Action("swordAttack", AIState.SWORDATTACK);
		action.addAction(1f, "Eat");
		action.addAction(1f, "Sleep");
		action.addAction(-6f, "Hunt");
		behavior.addAction(action);
	}

	
	private void buildSwordBody() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position
				.set(new Vector2(this.body.getPos().x, this.body.getPos().y));
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
		swordBody.setUserData(this);
		shape.dispose();
	}

}
