package com.anythingmachine.witchcraft.AIEngine;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.LuaEngine.LoadScript;

public class UtilityAI {
	private ArrayList<Goal> goals;
	private ArrayList<String> actions;
	private LoadScript script;
	
	public UtilityAI() {
		goals = new ArrayList<Goal>();
		actions = new ArrayList<String>();
		script = new LoadScript("aiScript.lua");
	}
	
	public String ChooseAction()
    {
        String bestAction = actions.get(0);
        float thisValue = 0;
        float bestValue = calculateDiscontentment(actions.get(0));

        for (int i = 1; i < actions.size(); i++)
        {
            thisValue = calculateDiscontentment(actions.get(i));

            if (thisValue < bestValue)
            {
                bestValue = thisValue;
                bestAction = actions.get(i);
            }
        }

        return bestAction;
    }
	
    public float calculateDiscontentment(String action)
    {
        float discontentment = 0;
        float newValue = 0;

        // Iterate through all goals
        for(Goal g: goals)
        {
        	float value = Float.parseFloat(
            		script.getGlobalString(action+"_"+g.name));
            //find new insistence if this action was performed
            newValue = g.insistence + value;
            //optimization to break when positive infinity
            if (newValue < 0)
                newValue = 0;
            discontentment += newValue*newValue;
        }

        return discontentment;
    }


    public String ChooseActionSimple()
    {
        // find the top goal
        Goal topGoal = goals.get(0);
        for (int i = 1; i < goals.size(); i++)
        {
            if (goals.get(0).insistence > topGoal.insistence)
                topGoal = goals.get(0);
        }

        // Find the best action to take
        String bestAction = actions.get(0);
        float bestUtility = Float.parseFloat(
        		script.getGlobalString(actions.get(0)+"_"+topGoal.name));
        for (int i = 1; i < actions.size(); i++)
        {
            float thisUtility = Float.parseFloat(
            		script.getGlobalString(actions.get(i)+"_"+topGoal.name));
            if (thisUtility < bestUtility)
            {
                bestUtility = thisUtility;
                bestAction = actions.get(i);
            }
        }

        return bestAction;
    }

}
