package com.anythingmachine.aiengine;

import java.util.HashMap;

import com.anythingmachine.physicsEngine.PhysicsState;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.agents.States.NPC.NPCState;
import com.anythingmachine.witchcraft.agents.States.NPC.NPCStateEnum;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.ground.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonData;

public class NPCStateMachine extends StateMachine {
	public NPCState state;
	protected HashMap<NPCStateEnum, NPCState> states;
	public PhysicsState phyState;
	public UtilityAI behavior;
	public Platform elevatedSegment;
	public NonPlayer me;
	public boolean canseeplayer;
	public boolean onscreen;
	public boolean inlevel;

	public NPCStateMachine(String name, Vector3 pos, Vector2 scl, boolean flip,
			SkeletonData sd, NonPlayer me) {
		super(name, pos, scl, flip, sd);
		this.me = me;
		this.npc = me;
		inlevel = me.level == GamePlayManager.currentlevel;
		canseeplayer = false;
		states = new HashMap<NPCStateEnum, NPCState>();
	}

	public void update(float dt) {
		state.update(dt);
	}

	public void addState(NPCStateEnum type, NPCState s) {
		states.put(type, s);
	}

	public boolean stateExists(NPCStateEnum type) {
		return states.containsKey(type);
	}

	public void setInitialState(NPCStateEnum name) {
		System.out.println(me.datafile+" initial state:"+name);		
		this.state = states.get(name);
		this.state.setParent(getState(NPCStateEnum.IDLE));
	}

	public void setState(NPCStateEnum name) {
		if (state.transistionOut()) {
			System.out.println(me.datafile+" set state:"+name);
			this.state = states.get(name);
			state.transistionIn();
		} else {
			System.out.println(me.datafile+"transistion state:"+name);
			states.get(name).transistionIn();
		}
	}

	public NPCState getState(NPCStateEnum name) {
		return states.get(name);
	}

	public boolean inState(NPCStateEnum state) {
		return this.state.name == state;
	}
}
