package com.anythingmachine.agents.player;

import java.util.ArrayList;
import java.util.Iterator;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.Util.Util.EntityType;
import com.anythingmachine.agents.States.Player.ArrowDead;
import com.anythingmachine.agents.States.Player.Attacking;
import com.anythingmachine.agents.States.Player.CastSpell;
import com.anythingmachine.agents.States.Player.Cinematic;
import com.anythingmachine.agents.States.Player.Dead;
import com.anythingmachine.agents.States.Player.DupeSkin;
import com.anythingmachine.agents.States.Player.DupeSkinPower;
import com.anythingmachine.agents.States.Player.Falling;
import com.anythingmachine.agents.States.Player.Flying;
import com.anythingmachine.agents.States.Player.Idle;
import com.anythingmachine.agents.States.Player.Invisible;
import com.anythingmachine.agents.States.Player.Jumping;
import com.anythingmachine.agents.States.Player.Landing;
import com.anythingmachine.agents.States.Player.LoadingState;
import com.anythingmachine.agents.States.Player.MindControlPower;
import com.anythingmachine.agents.States.Player.PlayerStateEnum;
import com.anythingmachine.agents.States.Player.Running;
import com.anythingmachine.agents.States.Player.ShapeCrow;
import com.anythingmachine.agents.States.Player.ShapeShiftIntermediate;
import com.anythingmachine.agents.States.Player.Walking;
import com.anythingmachine.agents.player.items.Cape;
import com.anythingmachine.agents.player.items.InventoryObject;
import com.anythingmachine.aiengine.AINode;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.particles.KinematicParticle;
import com.anythingmachine.userinterface.GamePlayUI.UILayout;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class Player extends Entity {
	public Cape cape;
	private PlayerStateMachine state;
	public ArrayList<InventoryObject> inventory;

	public Player(RK4Integrator rk4, Vector2 pos) {
		setupState("player", pos);
		setupInput();
		setupPowers();
		inventory = new ArrayList<InventoryObject>();

		cape = new Cape(3, 5, rk4, state.phyState.body.getPos());
		type = EntityType.PLAYER;

	}

	/** public functions **/

	public void update(float dT) {
		state.update(dT);
		// System.out.println(state.state);
	}

	public boolean dead() {
		return state.inState(PlayerStateEnum.DEAD)
				|| state.inState(PlayerStateEnum.ARROWDEAD);
	}

	public void draw(Batch batch) {
		state.state.draw(batch);
	}

	public void addAlarmToUI(UILayout screenpos) {
		float x = WitchCraft.screenWidth-100;
		if ( screenpos == UILayout.LEFT ) {
			x = 7;
		}
	    state.playerInterface.addToStack(new Vector3(x, WitchCraft.screenHeight*0.5f, 0), screenpos, "ALARMSIGN", "data/world/otherart.atlas");
	}
	
	public void addToInventory(InventoryObject io) {
	    inventory.add(io);
	}
	
	public void refreshInventory() {
	    Iterator<InventoryObject> it = inventory.iterator();
	    while ( it.hasNext() ) {
		InventoryObject io = (InventoryObject)it.next();
		if ( io.isDestroyed() ) {
		    it.remove();
		}
	    }
	}
	
	@Override
	public void setStateByValue(String strvalue) {
		state.state.setState(PlayerStateEnum.valueOf(strvalue));
	}
	
	public void setState(PlayerStateEnum name) {
		//state.setState(name);
		state.state.setState(name);
	}
	
	@Override
	public void setParentState() {
		state.state.transistionToParent();
	}
	
	@Override
	public void setAnimation(String anim, boolean val) {
		state.animate.setCurrent(anim, val);
	}
	
	public PlayerStateEnum getState() {
		return state.state.name;
	}
	public boolean inHighAlert() {
		return state.state.isHighAlertState();
	}

	public boolean inAlert() {
		return state.state.isAlertState();
	}

	public void drawCape(Matrix4 cam) {
		state.state.drawCape(cam);
	}

	public Vector3 getPosPixels() {
		return state.phyState.body.getPos();
	}

	public float getX() {
		return state.phyState.body.getX();
	}
	
	public float getY() {
		return state.phyState.body.getY();
	}

	public AINode getAINode() {
	    return state.currentNode;
	}
	
	@Override
	public void stop() {
		state.state.setIdle();
	}

	@Override
	public void stopOnX() {		
		state.state.setIdle();
	}

	@Override
	public void stopOnY() {
		state.state.setIdle();
	}

	@Override
	public void addPos(float x, float y) {
		state.phyState.body.addPos(x, y);
	}
	
	public void switchLevel(Vector2 newPos) {
		state.state.switchLevel(newPos);
	}

	@Override
	public void handleContact(Contact contact, boolean isFixture1) {
		state.state.handleContact(contact, isFixture1);
	}

	@Override
	public void endContact(Contact contact, boolean isFixture1) {
		state.state.endContact(contact, isFixture1);
	}

	@Override
	public void setX(float x) {
		state.phyState.body.setX(x);
	}

	public void drawUI(Batch batch) {
		state.drawUI(batch);
	}
	
	public void drawUIShapes() {
		state.playerInterface.drawUIShapes();
	}
	
	public void addTextoUi(CharSequence str){
		state.playerInterface.addText(str, UILayout.RIGHT);
	}
	
	public void clearTextUI(){
		state.playerInterface.clearText();
	}

	/************ private functions ************/
	/******************************************/

	/******************* setup functions ****************/
	/***************************************************/
	private void setupInput() {
		if (Controllers.getControllers().size > 0) {
			state.input.addInputState("Left", 21);
			state.input.addInputState("Right", 22);
			state.input.addInputState("UPAxis", 1);
			state.input.addInputState("UP", 19);
			state.input.addInputState("down", 20);
			state.input.addInputState("SwitchPower", 97);
			state.input.addInputState("SwitchPower1", 105);
			state.input.addInputState("SwitchPower2", 103);
			state.input.addInputState("UsePower", 96);
			state.input.addInputState("Interact", 100);
			state.input.addInputState("attack", 99);
		} else {
			state.input.addInputState("shift", Keys.SHIFT_LEFT);
			state.input.addInputState("Left", Keys.LEFT);
			state.input.addInputState("Right", Keys.RIGHT);
			state.input.addInputState("UP", Keys.UP);
			state.input.addInputState("down", Keys.DOWN);
			state.input.addInputState("SwitchPower", Keys.SHIFT_LEFT);
			state.input.addInputState("UsePower", Keys.SPACE);
			state.input.addInputState("Interact", Keys.D);
			state.input.addInputState("attack", Keys.A);
		}
	}

	private void setupStates() {
//		for( PlayerStateEnum s: PlayerStateEnum.DEAD.values() ) {
//			state.addState(s, s.constructState(state));
//		}
		state.addState(PlayerStateEnum.IDLE, new Idle(state,
				PlayerStateEnum.IDLE));
		state.addState(PlayerStateEnum.WALKING, new Walking(state,
				PlayerStateEnum.WALKING));
		state.addState(PlayerStateEnum.RUNNING, new Running(state,
				PlayerStateEnum.RUNNING));
		state.addState(PlayerStateEnum.ONSTAIRS, new Running(state,
				PlayerStateEnum.ONSTAIRS));
		state.addState(PlayerStateEnum.JUMPING, new Jumping(state,
				PlayerStateEnum.JUMPING));
		state.addState(PlayerStateEnum.FLYING, new Flying(state,
				PlayerStateEnum.FLYING));
		state.addState(PlayerStateEnum.FALLING, new Falling(state,
				PlayerStateEnum.FALLING));
		state.addState(PlayerStateEnum.LANDING, new Landing(state,
				PlayerStateEnum.LANDING));
		state.addState(PlayerStateEnum.ATTACKING, new Attacking(state,
				PlayerStateEnum.ATTACKING));
		state.addState(PlayerStateEnum.DEAD, new Dead(state,
				PlayerStateEnum.DEAD));
		state.addState(PlayerStateEnum.CINEMATIC, new Cinematic(state,
				PlayerStateEnum.CINEMATIC));
		state.addState(PlayerStateEnum.ARROWDEAD, new ArrowDead(state,
				PlayerStateEnum.ARROWDEAD));
		state.addState(PlayerStateEnum.CASTSPELL, new CastSpell(state,
				PlayerStateEnum.CASTSPELL));
		state.addState(PlayerStateEnum.DUPESKIN, new DupeSkin(state,
				PlayerStateEnum.DUPESKIN));
		state.addState(PlayerStateEnum.LOADINGSTATE, new LoadingState(state, PlayerStateEnum.LOADINGSTATE));
		/* power states */
		state.addState(PlayerStateEnum.DUPESKINPOWER, new DupeSkinPower(state,
				PlayerStateEnum.DUPESKINPOWER));
		state.addState(PlayerStateEnum.MINDCONTROLPOWER, new MindControlPower(
				state, PlayerStateEnum.MINDCONTROLPOWER));
		state.addState(PlayerStateEnum.INVISIBLEPOWER, new Invisible(state,
				PlayerStateEnum.INVISIBLEPOWER));
		state.addState(PlayerStateEnum.SHAPECROWPOWER, new ShapeCrow(state,
				PlayerStateEnum.SHAPECROWPOWER));
		state.addState(PlayerStateEnum.SHAPESHIFTINTERCROW,
				new ShapeShiftIntermediate(state,
						PlayerStateEnum.SHAPESHIFTINTERCROW,
						PlayerStateEnum.SHAPECROWPOWER));
		// state.addState(StateEnum.SHAPECATPOWER, new ShapeCatPower(state,
		// StateEnum.SHAPECATPOWER));
		// state.addState(StateEnum.INTANGIBLEPOWER, new Intangible(state,
		// StateEnum.INTANGIBLEPOWER));
		state.setInitialState(PlayerStateEnum.LOADINGSTATE);
		state.animate.bindPose();
		state.animate.setCurrent("idle", true);
	}

	private void setupPowers() {
		float width = WitchCraft.viewport.width / 2f;
		float height = WitchCraft.viewport.height;
		Sprite sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas")).findRegion("FUGE"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f,
				height - sprite.getHeight());
		state.playerInterface.addSprite(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("ANIMIMPERI"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f,
				height - sprite.getHeight());
		state.playerInterface.addSprite(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("INVISIBIL"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f,
				height - sprite.getHeight());
		state.playerInterface.addSprite(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("EFFINGO"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f,
				height - sprite.getHeight());
		state.playerInterface.addSprite(sprite);
		sprite = new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("MUTATIO"));
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setPosition(width - sprite.getWidth() * 0.5f,
				height - sprite.getHeight());
		state.playerInterface.addSprite(sprite);
		// powers.put("intangible", new IntangibilityPower());
		// powers.put("convert", new ConvertPower());
		// powers.put("freeze", new FreezePower());
		// powers.put("death", new DeathPower());
	}

	private void setupState(String name, Vector2 pos) {
		TextureAtlas atlas = WitchCraft.assetManager
				.get("data/spine/characters.atlas");
		SkeletonBinary sb = new SkeletonBinary(atlas);
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		KinematicParticle body = new KinematicParticle(new Vector3(pos.x, pos.y,
				0f), Util.GRAVITY * 7);
//		body.useEuler(false);

		state = new PlayerStateMachine(name, body.getPos(), new Vector2(0.53f,
				0.53f), false, sd);

		state.animate.addAnimation("jump", sd.findAnimation("beginfly"));
		state.animate.addAnimation("idle", sd.findAnimation("idle"));
		state.animate.addAnimation("walk", sd.findAnimation("walk"));
		state.animate.addAnimation("run", sd.findAnimation("run"));
		state.animate.addAnimation("castspell", sd.findAnimation("castspell"));
		state.animate.addAnimation("swordattack",
				sd.findAnimation("overheadattack"));
		state.animate.addAnimation("drawbow", sd.findAnimation("drawbow"));
		state.animate.addAnimation("ded", sd.findAnimation("ded"));

		setupStates();

		buildPhysics(body);
	}

	private void buildPhysics(KinematicParticle body) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(body.getPos().x, body.getPos().y));
		Body collisionBody = GamePlayManager.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 * Util.PIXEL_TO_BOX, 64 * Util.PIXEL_TO_BOX);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = ~Util.CATEGORY_STAIRS;
		Fixture feetFixture = collisionBody.createFixture(fixture);

		shape = new PolygonShape();
		shape.setAsBox(35 * Util.PIXEL_TO_BOX, 4 * Util.PIXEL_TO_BOX,
				new Vector2(0, 16).scl(Util.PIXEL_TO_BOX), 0f);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_PLAYER;
		fixture.filter.maskBits = ~Util.CATEGORY_STAIRS;
		Fixture hitRadius = collisionBody.createFixture(fixture);

		shape = new PolygonShape();
		shape.setAsBox(2 * Util.PIXEL_TO_BOX, 8 * Util.PIXEL_TO_BOX,
				new Vector2(0, -64).scl(Util.PIXEL_TO_BOX), 0f);
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		fixture.density = 1f;
		fixture.filter.categoryBits = Util.CATEGORY_FEET;
		fixture.filter.maskBits = Util.CATEGORY_STAIRS;
		Fixture bottomFeetFix = collisionBody.createFixture(fixture);

		collisionBody.setUserData(this);
		shape.dispose();
		GamePlayManager.rk4System.addParticle(body);

		state.phyState = new PhysicsState(body, collisionBody);
		state.phyState.addFixture(bottomFeetFix, "bottomfeet");
		state.phyState.addFixture(feetFixture, "feet");
		state.phyState.addFixture(hitRadius, "radius");

	}
}
