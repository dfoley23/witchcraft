/**
 * 
 */
package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.MindBeamParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

/**
 * @author dennis
 * 
 */
public class MindControlPower implements Power {
	private MindBeamParticle particle;
	private int timeout = 0;

	public MindControlPower() {
		particle = new MindBeamParticle(new Vector3(0, 0, 0), new Sprite(
				WitchCraft.assetManager.getAtlas("npcsheet1").findRegion(
						"archer_xcf-string1")), new Vector3(0, -0, 0));
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void usePower(State state, AnimationManager animate,
			KinematicParticle body) {
		timeout--;
		if ( timeout < 0 ) {
			WitchCraft.rk4System.addParticle(particle.copy(
					new Vector3(animate.isFlipped() ? body.getPos().x - 48f
							: body.getPos().x + 8, body.getPos().y + 16, 0),
						new Vector3(animate.isFlipped() ? -70 : 70, 0, 0)));
			timeout = 2;
		}
		if (state.canCastSpell()) {
			body.setVel(0, body.getVel().y, 0);
			animate.bindPose();
			animate.setCurrent("castspell", true);
		} else if (state == State.USINGPOWER
				&& animate.isTImeOverThreeQuarters()) {
			animate.applyTotalTime(true, -animate.getCurrentAnimTime() * 0.75f);
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void updatePower(State state, AnimationManager animate, float dt) {

	}

}
