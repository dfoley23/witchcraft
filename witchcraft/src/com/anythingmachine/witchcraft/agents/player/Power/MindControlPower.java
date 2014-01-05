/**
 * 
 */
package com.anythingmachine.witchcraft.agents.player.Power;

import com.anythingmachine.aiengine.State;

/**
 * @author dennis
 *
 */
public class MindControlPower implements Power {
	private ParticleSystem system;
	private Bone root;
	private TexturedBodyParticle particle;

	public MindControlPower(Bone root) {
		system = new ParticleSystem();
		particle = new TexturedBodyParticle(new Vector3(0, 0, 0), EntityType.PARTICLE);
	}

	/* (non-Javadoc)
	 */
	@Override
	public void usePower(State state) {
		if ( state != State.FLY ) {
			system.addParticle(particle.copy(new Vector3(root.getX(), root.getY(), 0))
		}
	}

	/* (non-Javadoc)
	 */
	@Override
	public void updatePower(State state, float dt) {

	}

}
