package com.anythingmachine.witchcraft.agents.States.NPC;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.States.Transistions.ActionEnum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GoingTo extends NPCState {
    protected Vector2 target;
    private int level;
    private float waittime = 0;

    public GoingTo(NPCStateMachine sm, NPCStateEnum name, Vector2 target,
	    int level) {
	super(sm, name);
	this.target = target;
	this.level = level - 1;
    }

    @Override
    public void update(float dt) {
	checkGround();
	if (sm.me.npctype.canAttack())
	    checkAttack();
	else
	    checkInBounds();

	checkTarget();
	sm.facingleft = sm.phyState.body.getVelX() < 0;
	sm.animate.setFlipX(sm.facingleft);

	fixCBody();

	// float delta = Gdx.graphics.getDeltaTime();

	updateSkel(dt);
    }

    @Override
    public void checkTarget() {
	if (Util.subVecs(sm.phyState.body.getPos(), target).len() < 128) {
	    switch (name) {
	    case GOINGTOEAT:
		sm.state.setChildState(NPCStateEnum.EATING);
		break;
	    case GOINGTOSLEEP:
		sm.state.setChildState(NPCStateEnum.SLEEPING);
		break;
	    case GOINGTOWORK:
		sm.state.setChildState(NPCStateEnum.WORKING);
		break;
	    case GOINGTOPATROL:
		sm.state.setChildState(NPCStateEnum.PATROLLING);
		break;
	    default:
		break;
	    }
	}
    }

    @Override
    public void setGoingTo(float dt) {
	waittime += dt;
	if (waittime > sm.behavior.getActionTime() * 2) {
	    sm.state.switchLevel(level);
	    if (level != GamePlayManager.currentlevel)
		sm.phyState.body.setPos(target);
	}
    }

    @Override
    public void checkGround() {
	Vector3 pos = sm.phyState.body.getPos();
	if (target.y < 10 && sm.elevatedSegment != null
		&& sm.elevatedSegment.isBetween(sm.facingleft, pos.x)) {
	    float groundPoint = sm.elevatedSegment.getHeight(pos.x);
	    sm.phyState.body.setY(groundPoint);
	    // sm.setTestVal("grounded", true);
	}
    }

    @Override
    public ActionEnum[] getPossibleActions() {
	return new ActionEnum[] {};
    }

    @Override
    public void transistionIn() {
	sm.facingleft = target.x < sm.phyState.body.getX();
	if (target.y < 100 && target.y > 10) {
	    sm.phyState.body.addPos(0, target.y);
	    sm.phyState.body.setGravityVal(0);
	}
	sm.getState(NPCStateEnum.WALKING).transistionIn();
    }

    @Override
    public boolean transistionOut() {
	sm.phyState.body.setGravityVal(Util.GRAVITY);
	return true;
    }

    @Override
    public void setIdle() {
	sm.setState(NPCStateEnum.IDLE);
	sm.state.setParent(this);
    }

    @Override
    public void takeAction(float dt) {
    }

    @Override
    public void takeAction(Action action) {

    }

}
