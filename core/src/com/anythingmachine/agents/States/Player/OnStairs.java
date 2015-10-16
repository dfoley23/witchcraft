package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.collisionEngine.ground.Platform;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OnStairs extends PlayerState {
	private float platformHitStepLimit = 12;
	private float hitPlatformForSteps = 0;

	public OnStairs(PlayerStateMachine sm, PlayerStateEnum name) {
		super(sm, name);
	}
	
	@Override
	protected void checkGround() {
		Vector3 pos = sm.phyState.body.getPos();
		sm.grounded = false;
		float groundPoint = sm.currentPlatform.getHeight(pos.x);
		Vector2 stairsStart = sm.currentPlatform.getStartPos();
		Vector2 stairsEnd = sm.currentPlatform.getEndPos();
		Gdx.app.log("checkGround:OnStairs:",""+((pos.x + 8 > stairsStart.x && sm.facingleft) || (pos.x - 8 < stairsEnd.x && !sm.facingleft)));
		if ((pos.x + 8 > stairsStart.x && sm.facingleft) || (pos.x - 8 < stairsEnd.x && !sm.facingleft)) {
			sm.phyState.body.setY(groundPoint);
			sm.currentStairs = sm.currentPlatform;
		}
		if (sm.phyState.body.getY() < GamePlayManager.lowestPlatInLevel.getDownPosY()) {
			sm.currentPlatform = GamePlayManager.lowestPlatInLevel;
			sm.hitplatform = true;
			hitPlatformForSteps = 0;
			land();
			sm.setState(PlayerStateEnum.IDLE);
		}
		sm.hitplatform = false;
		sm.hitstairs = false;
	}

	@Override
	protected void hitPlatform(Platform plat) {
		float platHeight = plat.getHeight();
		// platform is below player
		if (platHeight - 32 < sm.phyState.body.getY()) {
			// if not hitting stairs or hitting both platform and stairs
			// but input is walking down
			if (!sm.currentStairs.forceWalkDown() || !sm.input.is("down")
					|| sm.currentStairs.getUpPosY() - 8 > sm.phyState.body.getY()) {
				if (hitPlatformForSteps > platformHitStepLimit) {
					sm.hitstairs = false;
					sm.hitplatform = true;
					sm.currentPlatform = plat;
					sm.currentStairs = null;
					land();
					sm.setState(PlayerStateEnum.IDLE);
				}
			}
		}
	}

	
	@Override
	public void setState(PlayerStateEnum newstate) {
//		sm.getState(newstate).transistionIn();
	}

	@Override
	protected void hitStairs(Stairs stairs) {
		sm.hitplatform = false;
		sm.currentPlatform = stairs;
		sm.currentStairs = stairs;
	}
	
	@Override
	protected void upStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// walk up stairs
		if (sm.input.up() && ((!sm.facingleft && slantRight) || (sm.facingleft && !slantRight))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
		} else {
			sm.setState(this.parent.name);
		}
	}

	@Override
	protected void downStairs(Stairs stairs) {
		boolean slantRight = stairs.slantRight();
		// walk down stairs
		if ((sm.input.down() || stairs.forceWalkDown())
				&& ((slantRight && sm.facingleft) || (!slantRight && !sm.facingleft))) {
			sm.hitstairs = true;
			sm.hitplatform = false;
			sm.currentPlatform = stairs;
			sm.currentStairs = stairs;
		} else {
			sm.setState(this.parent.name);
		}
	}

	@Override
	public void setWalk(){
		
	}

	@Override
	public void setRun(){
		
	}

	@Override
	public void land(){
		
	}


}
