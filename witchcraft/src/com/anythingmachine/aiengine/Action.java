package com.anythingmachine.aiengine;

import java.util.HashMap;
import java.util.Map;

import com.anythingmachine.witchcraft.States.NPC.NPCStateEnum;
import com.anythingmachine.witchcraft.States.Transistions.ActionEnum;

public class Action {
	private Map<String, Float> goalActionValues;
	private NPCStateEnum actionState;
	public ActionEnum name;
	
	public Action( ActionEnum name, NPCStateEnum actionState) {
		this.name = name;
		goalActionValues = new HashMap<String, Float>();
		this.actionState = actionState;
	}
	
	public float getGoalChange(String goalName) {
		if(goalActionValues.containsKey(goalName)){
			return goalActionValues.get(goalName);
		}
		else return 0;
	}
	
	public void addAction( Float value, String goalName ) {
		if(!goalActionValues.containsKey(goalName)) {
			goalActionValues.put(goalName, value);
		}
	}
	
	public NPCStateEnum getAIState() {
		return actionState;
	}
}
