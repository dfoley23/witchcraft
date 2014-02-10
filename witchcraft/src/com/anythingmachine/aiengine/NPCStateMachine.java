	package com.anythingmachine.aiengine;
	
	import com.anythingmachine.physicsEngine.PhysicsState;
	import com.anythingmachine.witchcraft.States.NPC.NPCState;
	import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;
	import com.anythingmachine.witchcraft.ground.Platform;
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.math.Vector2;
	import com.badlogic.gdx.math.Vector3;
	import com.esotericsoftware.spine.SkeletonData;
	
	public class NPCStateMachine extends StateMachine {
		public NPCState state;
		protected NPCState[] states;
		public PhysicsState phyState;
		public UtilityAI behavior;
		public Platform elevatedSegment;

		public NPCStateMachine(String name, Vector3 pos, Vector2 scl, boolean flip,
				SkeletonData sd) {
			super(name, pos, scl, flip, sd);

			states = new NPCState[NPCStateEnum.IDLE.getSize()];
			
		}
	
		public void update(float dt) {
			state.update(dt);
	
			if (!phyState.body.isStable()) {
				float delta = Gdx.graphics.getDeltaTime();
	
				animate.applyTotalTime(true, delta);
	
				animate.setPos(phyState.getPos(), -8f, 0f);
				animate.updateSkel(dt);
			}	
		}
	
		public void addState(NPCStateEnum type, NPCState s) {
			states[type.getID()] = s;
		}
	
		public void setInitialState(NPCStateEnum name) {
			this.state = states[name.getID()];
			this.state.setParent(getState(NPCStateEnum.IDLE));
		}
	
		public void setState(NPCStateEnum name) {
			state.transistionOut();
			this.state = states[name.getID()];
			state.transistionIn();
		}
	
		public NPCState getState(NPCStateEnum name) {
			return states[name.getID()];
		}
	
		public boolean inState(NPCStateEnum state) {
			return this.state.name.getID() == state.getID();
		}	
	}
