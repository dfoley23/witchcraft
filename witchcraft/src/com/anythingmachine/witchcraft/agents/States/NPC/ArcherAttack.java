package com.anythingmachine.witchcraft.agents.States.NPC;

import java.util.ArrayList;
import java.util.Iterator;

import com.anythingmachine.aiengine.Action;
import com.anythingmachine.aiengine.NPCStateMachine;
import com.anythingmachine.physicsEngine.particleEngine.particles.Arrow;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;

public class ArcherAttack extends NPCState {
    private ArrayList<Arrow> arrows;
    private Bone arrowBone;
    private boolean shotArrow;
    private boolean idle = false;
    private boolean higher = false;
    private boolean lower = false;
    private float lastHeight = 128f;

    public ArcherAttack(NPCStateMachine sm, NPCStateEnum name) {
	super(sm, name);
	arrows = new ArrayList<Arrow>();
	arrowBone = sm.animate.findBone("right hand");
    }

    @Override
    public void update(float dt) {
	checkGround();
	sm.facingleft = GamePlayManager.player.getX() < sm.phyState.body.getX();
	sm.animate.setFlipX(sm.facingleft);
	if (sm.animate.getTime() > sm.animate.getCurrentAnimTime() * 0.75
		&& !shotArrow) {
	    shotArrow = true;
	    Arrow arrow = new Arrow(new Vector3(arrowBone.getWorldX()
		    + (sm.facingleft ? -128 : 128), arrowBone.getWorldY(), 0),
		    new Vector3(0, 0, 0));
	    arrow.setStable(false);
	    if ( higher ) {
		lastHeight += 128f;
	    } else if ( lower ) {
		lastHeight -= 128;
	    }
	    arrow.pointAtTarget(GamePlayManager.player.getPosPixels(), 650, lastHeight);
	    arrows.add(arrow);
	} else if (!shotArrow) {
	    Vector3 target = GamePlayManager.player.getPosPixels();
	    Vector3 pos = sm.phyState.body.getPos();
	    Vector3 dir = new Vector3(target.x - pos.x, target.y - pos.y, 0);
	    float costheta = Util.dot(dir, new Vector3(1, 0, 0)) / dir.len();
	    sm.animate.hip.setRotation((float) Math.acos(costheta));
	} else {
	    Iterator<Arrow> it = arrows.iterator();
	    while (it.hasNext()) {
		Arrow a = it.next();
		if (!a.isDestroyed()) {
		    if ((a.isStable() || a.isStuck())) {
			a.destroyBody();
			float ypos = a.getY();
			float playerYpos = GamePlayManager.player.getY();
			if ( ypos < playerYpos-16 ) {
			    higher = true;
			    lower = false;
			} else if ( ypos > playerYpos+16){
			    higher = false;
			    lower = true;
			}
		    } 
		} else if (arrows.size() > 5) {
			it.remove();
		}
	    }
	    if (sm.animate.getTime() > sm.animate.getCurrentAnimTime() * 0.97) {
		sm.animate.bindPose();
		sm.animate.setCurrent("idle", true);
		idle = true;
	    }
	    if (idle
		    && sm.animate.getTime() > sm.animate.getCurrentAnimTime() * 0.5) {
		transistionIn();
	    }
	}
	if (GamePlayManager.player.dead()) {
	    super.setIdle();
	}

	// float delta = Gdx.graphics.getDeltaTime();

	updateSkel(dt);

    }

    @Override
    public void checkInBounds() {

    }

    @Override
    public void draw(Batch batch) {
	super.draw(batch);
	for (Arrow a : arrows) {
	    a.draw(batch);
	}
    }

    @Override
    public boolean transistionOut() {
	for (Arrow a : arrows) {
	    a.destroyBody();
	}
	arrows.clear();
	return true;
    }

    @Override
    public void transistionIn() {
	sm.animate.bindPose();
	sm.animate.setCurrent("drawbow", true);
	sm.phyState.body.stop();
	shotArrow = false;
	idle = false;
    }

    @Override
    public void takeAction(Action action) {

    }

    @Override
    public void setIdle() {
	if (sm.animate.atEnd()) {
	    super.setIdle();
	}
    }

}
