/**
 * 
 */
package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.MindBeamParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
	public void usePower(StateMachine state, float dt) {
		if (state.state.isPlayer()) {
			timeout--;
			if (timeout < 0) {
				Vector2 pos = state.phyState.getPos();
				WitchCraft.rk4System.addParticle(particle.copy(new Vector3(
						state.animate.isFlipped() ? pos.x - 48f : pos.x + 8,
						pos.y + 32, 0), new Vector3(
						state.animate.isFlipped() ? -70 : 70, 0, 0)));
				timeout = 4;
			}
			state.state.setCastSpell();
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void updatePower(StateMachine state, float dt) {
	}

}
