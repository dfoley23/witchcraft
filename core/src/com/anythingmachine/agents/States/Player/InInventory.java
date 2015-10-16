package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.Util.Util;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.badlogic.gdx.math.Matrix4;

public class InInventory extends PlayerState {

    public InInventory (PlayerStateMachine sm, PlayerStateEnum name) {
	super(sm, name);
    }

    @Override
    public void drawCape(Matrix4 cam) {
	GamePlayManager.player.cape.draw(cam, 1f);
    }

    @Override
    public void switchPower() {
    }

    @Override
    public void usePower() {
    }

    @Override
    public void transistionIn() {
	sm.phyState.body.stop();
	sm.animate.bindPose();
	sm.animate.setCurrent("idle", true);
    }

    public void transistionOut() {

    }

    @Override
    public void setInputSpeed() {
	int axisVal = sm.input.axisRange2();
	if (axisVal > 0) {
	    sm.facingleft = false;
	    if (!sm.hitrightwall) {
		sm.hitleftwall = false;
		if (axisVal > 1) {
		    setRun();
		    sm.phyState.body.setXVel(Util.DEV_MODE ? Util.DEBUGSPED
			    : Util.PLAYERRUNSPEED);
		} else {
		    setWalk();
		    sm.phyState.body.setXVel(Util.PLAYERWALKSPEED);
		}
	    }
	} else if (axisVal < 0) {
	    sm.facingleft = true;
	    if (!sm.hitleftwall) {
		sm.hitrightwall = false;
		if (axisVal < -1) {
		    setRun();
		    sm.phyState.body.setXVel(Util.DEV_MODE ? -Util.DEBUGSPED
			    : -Util.PLAYERRUNSPEED);
		} else {
		    setWalk();
		    sm.phyState.body.setXVel(-Util.PLAYERWALKSPEED);
		}
	    }
	} else {
	    setIdle();
	}
    }

    @Override
    protected void checkGround() {
    }

}
