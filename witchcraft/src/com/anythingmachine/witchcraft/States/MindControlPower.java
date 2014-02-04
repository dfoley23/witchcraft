package com.anythingmachine.witchcraft.States;

import com.anythingmachine.aiengine.StateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.ParticleEngine.MindBeamParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class MindControlPower extends CastSpell {
	private MindBeamParticle particle;
	private int timeout = 0;

	public MindControlPower(StateMachine sm, StateEnum name) {
		super(sm, name);
		particle = new MindBeamParticle(new Vector3(0, 0, 0), new Sprite(
				((TextureAtlas) WitchCraft.assetManager
						.get("data/world/otherart.atlas"))
						.findRegion("mindbeam")), new Vector3(0, 0, 0));

	}

	@Override
	public void usePower() {
		if (sm.input.is("UsePower")) {
			timeout--;
			if (timeout < 0) {
				Vector3 pos = sm.phyState.getPos();
				WitchCraft.rk4System.addParticle(particle.copy(new Vector3(
						sm.animate.isFlipped() ? pos.x - 48f : pos.x + 8,
						pos.y + 32, 0), new Vector3(
						sm.animate.isFlipped() ? -70 : 70, 0, 0)));
				timeout = 4;
			}
		}
	}

}
