package com.anythingmachine.agents.States.Player;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.aiengine.PlayerStateMachine;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.physicsEngine.particleEngine.MindBeamParticle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

public class MindControlPower extends CastSpell {
	private MindBeamParticle particle;
	private int timeout = 0;

	public MindControlPower(PlayerStateMachine sm, PlayerStateEnum name) {
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
				Vector3 pos = sm.phyState.body.getPos();
				GamePlayManager.rk4System.addParticle(particle.copy(new Vector3(
						sm.animate.isFlipped() ? pos.x - 48f : pos.x + 8,
						pos.y + 32, 0), new Vector3(
						sm.animate.isFlipped() ? -70 : 70, 0, 0)));
				timeout = 4;
			}
		}
	}

}
