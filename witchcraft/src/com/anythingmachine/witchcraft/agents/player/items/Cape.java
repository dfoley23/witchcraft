package com.anythingmachine.witchcraft.agents.player.items;

import com.anythingmachine.physicsEngine.Cloth;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.particles.Particle;
import com.anythingmachine.physicsEngine.particleEngine.particles.SpringParticle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Cape {
	private Cloth cloth;

	public Cape(int width, int height, RK4Integrator rk4, Vector3 originpos) {
		cloth = new Cloth(width, height, rk4, false);
		buildCloth(cloth, originpos);
	}

	public void draw(Matrix4 cam, float alpha) {
		cloth.draw(cam, alpha);
	}

	public void updatePos(float x, float y) {
		cloth.trans(x, y);
		// cloth.flip(flip);
	}
	
	public void rotate(float deg) {
		cloth.rotate(deg);
	}

	public void flip(boolean val) {
		cloth.flip(val);
	}

	public void addWindForce(float x, float y) {
		cloth.addForce(new Vector3(x, y, 0));
	}

	private void buildCloth(Cloth cloth, Vector3 originpos) {
		float springK = 300f;
		float damper = 0.85f;

		// the hood
		Particle hoodpin1 = new Particle(new Vector3(-27, 44f, -8f));
		Particle hoodpin2 = new Particle(new Vector3(1f, 44f, -8f));
		// ffront hood
		SpringParticle hoodSpring3 = new SpringParticle(new Vector3(-32, 28f, -8f));
		// back hood
		Particle hoodSpring4 = new Particle(new Vector3(-28, 20f, -8f));
		// middle hood
		Particle hoodpin3 = new Particle(new Vector3(-7f, 20f, -8f));
		// the cape
		Particle capepin1 = new Particle(new Vector3(-38f, 0f, -16f));
		Particle capepin2 = new Particle(new Vector3(-22f, 6f, -8f));
		Particle capepin3 = new Particle(new Vector3(2f, 3f, 0f));
		Particle capepin1_1 = new Particle(new Vector3(-44f, -30f, -16f));
		Particle capepin1_2 = new Particle(new Vector3(-17f, -30f, 0f));
		SpringParticle unPin1_3 = new SpringParticle(new Vector3(3f, -30f, 0f));

		SpringParticle unPin2_1 = new SpringParticle(new Vector3(-38f, -48f, -16f));
		SpringParticle unPin2_2 = new SpringParticle(new Vector3(-16f, -48f, -8f));
		SpringParticle unPin2_3 = new SpringParticle(new Vector3(2f, -48f, 0f));
		SpringParticle unPin3_1 = new SpringParticle(new Vector3(-38f, -60f, -16f));
		SpringParticle unPin3_2 = new SpringParticle(new Vector3(-16f, -60f, -8f));
		SpringParticle unPin3_3 = new SpringParticle(new Vector3(2f, -60f, 0f));
		SpringParticle unPin4_1 = new SpringParticle(new Vector3(-38f, -80f, -16f));
		SpringParticle unPin4_2 = new SpringParticle(new Vector3(-16f, -80f, -8f));
		SpringParticle unPin4_3 = new SpringParticle(new Vector3(2f, -80f, 0f));
//		SpringParticle unPin5_2 = new SpringParticle(new Vector3(0, 0, 0));

		hoodSpring3.addSpring(hoodpin1,14f, 170, 2.75f);
		hoodSpring3.addSpring(capepin1, 14f, 170, 2.75f);
		hoodSpring3.addSpring(hoodSpring4, 14f, 170, 2.75f);
//		hoodSpring4.addSpring(hoodpin1, 24f, springK, damper);
//		hoodSpring4.addSpring(hoodSpring3, 24f, springK, damper);
//		hoodSpring4.addSpring(hoodpin3, 24f, springK, damper);
//		hoodSpring4.addSpring(capepin2, 24f, springK, damper);
//		hoodSpring5.addSpring(hoodSpring4, 24f, springK, damper);
//		hoodSpring5.addSpring(capepin3, 24f, springK, damper);
//		hoodSpring5.addSpring(hoodpin2, 24f, springK, damper);

		unPin1_3.addRope(capepin3, 64f, 100, 2.5f);
		unPin1_3.addSpring(capepin1_2, 24f, springK, damper);
		unPin1_3.addSpring(unPin2_3, 24f, springK, damper);
		unPin2_1.addSpring(capepin1_1, 24f, springK, damper);
		unPin2_1.addSpring(unPin2_2, 24f, springK, damper);
		unPin2_1.addSpring(unPin3_1, 24f, springK, damper);
		unPin2_2.addSpring(capepin1_2, 24f, springK, damper);
		unPin2_2.addSpring(unPin2_3, 24f, springK, damper);
		unPin2_2.addSpring(unPin2_1, 24f, springK, damper);
		unPin2_2.addSpring(unPin3_2, 24f, springK, damper);
		unPin2_3.addSpring(unPin1_3, 24f, springK, damper);
		unPin2_3.addSpring(unPin2_2, 24f, springK, damper);
		unPin2_3.addSpring(unPin3_3, 24f, springK, damper);
		unPin3_1.addSpring(unPin2_1, 24f, springK, damper);
		unPin3_1.addSpring(unPin3_2, 24f, springK, damper);
		unPin3_1.addSpring(unPin4_1, 24f, springK, damper);
		unPin3_2.addSpring(unPin2_2, 24f, springK, damper);
		unPin3_2.addSpring(unPin3_1, 24f, springK, damper);
		unPin3_2.addSpring(unPin3_3, 24f, springK, damper);
		unPin3_2.addSpring(unPin4_2, 24f, springK, damper);
		unPin3_3.addSpring(unPin2_3, 24f, springK, damper);
		unPin3_3.addSpring(unPin3_2, 24f, springK, damper);
		unPin3_3.addSpring(unPin4_3, 24f, springK, damper);
		unPin4_1.addSpring(unPin3_1, 24f, springK, damper);
		unPin4_1.addSpring(unPin4_2, 24f, springK, damper);
		unPin4_2.addSpring(unPin3_2, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_1, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_3, 24f, springK, damper);
//		unPin4_2.addSpring(unPin5_2, 24f, springK, damper);
		unPin4_3.addSpring(unPin3_3, 24f, springK, damper);
		unPin4_3.addSpring(unPin4_2, 24f, springK, damper);
//		unPin5_2.addSpring(unPin4_2, 24f, springK, damper);

		cloth.addLink(hoodpin1);
		cloth.addLink(hoodpin2);
		cloth.addLink(hoodSpring3);
		cloth.addLink(hoodSpring4);
		cloth.addLink(hoodpin3);

		cloth.addLink(capepin1);
		cloth.addLink(capepin2);
		cloth.addLink(capepin3);

		cloth.addLink(capepin1_1);
		cloth.addLink(capepin1_2);
		cloth.addLink(unPin1_3);

		cloth.addLink(unPin2_1);
		cloth.addLink(unPin2_2);
		cloth.addLink(unPin2_3);

		cloth.addLink(unPin3_1);
		cloth.addLink(unPin3_2);
		cloth.addLink(unPin3_3);

		cloth.addLink(unPin4_1);
		cloth.addLink(unPin4_2);
		cloth.addLink(unPin4_3);

		// cloth.addLink(unPin5_1);
//		cloth.addLink(unPin5_2);
		// cloth.addLink(unPin5_3);

		FileHandle handle = Gdx.files.internal("data/capeverts");
		String[] fileContent = handle.readString().split("\n");

		short[] indices = new short[Integer.parseInt(fileContent[0])];
		int index = 0;
		for (String s : fileContent) {
			String[] verts = s.split(",");
			if (verts.length == 3) {
				for (String v : verts) {
					indices[index] = (short) Integer.parseInt(v);
					index++;
				}
			}
		}
		cloth.setIndices(indices);
	}

}
