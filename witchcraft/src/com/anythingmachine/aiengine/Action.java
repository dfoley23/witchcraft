package com.anythingmachine.aiengine;

import java.util.HashMap;
import java.util.Map;

import com.anythingmachine.aiengine.UtilityAI.AIState;

public class Action {
	private Map<String, Float> goalActionValues;
	private AIState actionState;
	public String name;
	
	public Action( String name, AIState actionState) {
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
	
	public AIState getAIState() {
		return actionState;
	}
}
