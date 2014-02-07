package com.anythingmachine.aiengine;

import java.util.ArrayList;
import java.util.HashMap;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.witchcraft.States.NPCState;
import com.anythingmachine.witchcraft.States.NPCStateEnum;
import com.badlogic.gdx.Gdx;

public class UtilityAI {
	private ArrayList<Goal> goals;
	private HashMap<String, Action> actions;
	private LoadScript script;
	
	public UtilityAI() {
		goals = new ArrayList<Goal>();
		actions = new HashMap<String, Action>();
	}
	
	public NPCState ChooseAction(NPCStateEnum state)
    {
		NPCStateEnum[] followUps = state.getFollowUpStates();
		Action bestAction = actions.get(0);
        float thisValue = 0;
        float bestValue = calculateDiscontentment(actions.get(followUps[0].getName()));

        for (int i = 1; i < actions.size(); i++)
        {
//        	System.out.println(actions.get(i));
            thisValue = calculateDiscontentment(actions.get(i));

            if (thisValue < bestValue)
            {
                bestValue = thisValue;
                bestAction = actions.get(i);
            }
        }

        return bestAction.getAIState();
        
    }
	
	public void takeAction(String name) {
        Gdx.app.log("ai has chosen to " + bestAction.name, "");
        for(Goal g: goals ) {
        	g.insistence += bestAction.getGoalChange(g.name);
        }	
	}
	
    public float calculateDiscontentment(Action action)
    {
        float discontentment = 0;
        float newValue = 0;

        // Iterate through all goals
        for(Goal g: goals)
        {
        	float value = action.getGoalChange(g.name);
            //find new insistence if this action was performed
            newValue = g.insistence + value;
            //optimization to break when positive infinity
            if (newValue < 0)
                newValue = 0;
            discontentment += newValue*newValue;
        }

        return discontentment;
    }


    public Action ChooseActionSimple()
    {
        // find the top goal
        Goal topGoal = goals.get(0);
        for (int i = 1; i < goals.size(); i++)
        {
            if (goals.get(0).insistence > topGoal.insistence)
                topGoal = goals.get(0);
        }

        // Find the best action to take
        Action bestAction = actions.get(0);
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
    
    public void addGoal(Goal g){
    	goals.add(g);
    }
    
    public void addAction(Action action){
    	actions.add(action);
    }
}
