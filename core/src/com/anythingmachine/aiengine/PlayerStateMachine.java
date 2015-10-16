package com.anythingmachine.aiengine;

import java.util.Random;

import com.anythingmachine.GamePlayUI;
import com.anythingmachine.agents.States.Player.PlayerState;
import com.anythingmachine.agents.States.Player.PlayerStateEnum;
import com.anythingmachine.collisionEngine.ground.Door;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.StairTrigger;
import com.anythingmachine.input.InputManager;
import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonData;

public class PlayerStateMachine extends StateMachine {
	public PlayerState state;
	public InputManager input;
	protected PlayerState[] states;
	public PhysicsState phyState;
	public PlayerStateEnum power;
	public GamePlayUI playerInterface;
	public float uiFadein;
	public AINode currentNode;

	public Bone neck;
	public int curGroundSegment;
	public Platform currentPlatform;
	public Platform currentStairs;
	public StairTrigger stairTrigger;
	public Door currentDoor;
	public String dupeSkin;
	public Random rand;

	public boolean hitplatform;
	public boolean hitstairs = false;
	public boolean hitroof;
	public boolean invi;
	public boolean killedbehind = false;
	public boolean switchStateThisStep = false;

	public PlayerStateMachine(String name, Vector3 pos, Vector2 scl, boolean flip, SkeletonData sd) {
		super(name, pos, scl, flip, sd);
		playerInterface = new GamePlayUI();
		neck = animate.findBone("neck");
		dupeSkin = "";
		power = PlayerStateEnum.MINDCONTROLPOWER;
		currentNode = new AINode(1);

		rand = new Random();

		input = new InputManager();
		states = new PlayerState[PlayerStateEnum.DEAD.getSize()];
	}

	@Override
	public void update(float dt) {

		switchStateThisStep = false;

		input.update(dt);

		state.update(dt);

		if (state.canAnimate())
			animate.applyTotalTime(true, dt);

		animate.setPos(phyState.body.getPos(), 0, 0f);
		animate.updateSkel(dt);

		if (playerInterface.isStacked()) {
			playerInterface.update(dt);
		}
	}

	public void drawUI(Batch batch) {
		playerInterface.draw(batch);
		if (uiFadein >= 0f) {
			playerInterface.uiByIndex.get(power.getPowerIndex()).draw(batch, uiFadein);
			uiFadein += WitchCraft.dt * 0.5f;
			if (uiFadein >= 1f)
				uiFadein = -2f;
		} else if (uiFadein < 0 && uiFadein > -4) {
			playerInterface.uiByIndex.get(power.getPowerIndex()).draw(batch, -(uiFadein + 1));
			uiFadein += WitchCraft.dt * 0.5f;
			if (uiFadein >= -1)
				uiFadein = -5f;
		}
	}

	public void nextPower() {
		power = states[power.getNextPower()].name;
	}

	public void usePower() {
		state.transistionOut();
		this.state = states[power.getID()];
		state.transistionIn();
	}

	public void addState(PlayerStateEnum type, PlayerState s) {
		states[type.getID()] = s;
	}

	public void setInitialState(PlayerStateEnum name) {
		this.state = states[name.getID()];
	}

	public void setState(PlayerStateEnum name) {
		state.transistionOut();
		this.state = states[name.getID()];
		switchStateThisStep = true;
//		Gdx.app.log("Player State Change", "" + name);
		state.transistionIn();
	}

	public PlayerState getState(PlayerStateEnum name) {
		return states[name.getID()];
	}

	public boolean inState(PlayerStateEnum state) {
		return this.state.name.getID() == state.getID();
	}

}
