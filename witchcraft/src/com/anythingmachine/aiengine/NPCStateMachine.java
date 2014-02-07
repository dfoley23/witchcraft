package com.anythingmachine.aiengine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonData;

public class NPCStateMachine extends StateMachine {
	
	public NPCStateMachine(String name, Vector3 pos, Vector2 scl, boolean flip,
			SkeletonData sd) {
		super(name, pos, scl, flip, sd);
	}

}
