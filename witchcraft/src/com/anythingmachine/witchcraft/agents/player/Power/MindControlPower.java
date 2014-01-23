/**
 * 
 */
package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.MindBeamParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("mindbeam")), new Vector3(0, 0, 0));
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void usePower(StateMachine state, AnimationManager animate,
			KinematicParticle body, float dt) {
		timeout--;
		if (timeout < 0) {
			WitchCraft.rk4System.addParticle(particle.copy(
					new Vector3(animate.isFlipped() ? body.getPos().x - 48f
							: body.getPos().x + 8, body.getPos().y + 32, 0),
					new Vector3(animate.isFlipped() ? -70 : 70, 0, 0)));
			timeout = 4;
		}
		if (state.state.canCastSpell(state) && !state.test("usingpower")) {
			body.setVel(0, body.getVel().y, 0);
			animate.bindPose();
			animate.setCurrent("castspell", true);
			state.setTestVal("usingpower", true);
			body.setVel(0, 0, 0);
		} else if (state.test("usingpower")
				&& animate.isTImeOverThreeQuarters(0.0f)) {
			animate.applyTotalTime(true, -animate.getCurrentAnimTime() * 0.75f);
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void updatePower(StateMachine state, AnimationManager animate,
			float dt) {
		if (state.test("usingpower") && animate.atEnd()) {
			state.setTestVal("usingpower", false);
			state.state.setWalk(state);
		}
	}

}
