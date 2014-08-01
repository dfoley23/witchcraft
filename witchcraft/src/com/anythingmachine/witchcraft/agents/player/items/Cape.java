package com.anythingmachine.witchcraft.agents.player.items;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.Cloth;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.particles.Particle;
import com.anythingmachine.physicsEngine.particleEngine.particles.SpringParticle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Cape {
	private ArrayList< Particle > pins;
	private ArrayList<Vector3> pinpos;
	private Cloth cloth;
	
	public Cape(int width, int height, RK4Integrator rk4, Vector3 originpos) {
		pins = new ArrayList<Particle>();
		pinpos = new ArrayList<Vector3>();
		cloth = new Cloth(width, height, rk4);
		buildCloth(cloth, originpos);
	}
	
	public void draw( Matrix4 cam, float alpha ) {
		cloth.draw(cam, alpha);
	}
	
	public void updatePos( float x, float y ) {
		cloth.trans(x, y);
		//cloth.flip(flip);
	}
	
	public void flip(boolean val) {
		cloth.flip(val);
	}
	
	public void addWindForce(float x, float y) {
		cloth.addForce(new Vector3(x, y, 0));
	}
	
	private void buildCloth(Cloth cloth, Vector3 originpos) { 
		float springK = 350f;
		float damper = 0.75f;
		Particle pin1 = new Particle(new Vector3(2f,  3f,  -30f));
		Particle pin2 = new Particle(new Vector3(-15f, 6f,   -22f));
		Particle pin3 = new Particle(new Vector3(-26f, 0f,  -14f));
		Particle pin5 = new Particle(new Vector3(-19f,  -30f,  -22f));
		Particle pin6 = new Particle(new Vector3(-33f,  -30f,  -14f));
		pins.add(pin1);
		pins.add(pin2);
		pins.add(pin3);
		pins.add(pin5);
		pins.add(pin6);
		SpringParticle pin4 = new SpringParticle(new Vector3(6f,  -30f,  -30f));
		SpringParticle unPin2_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -32f,   -4f));
		SpringParticle unPin2_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-32f,   -4f));
		SpringParticle unPin2_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-32f,   -4f));
		SpringParticle unPin3_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -48f,   -2f));
		SpringParticle unPin3_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-48f,   -2f));
		SpringParticle unPin3_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-48f,   -2f));
		SpringParticle unPin4_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -60,   0f));
		SpringParticle unPin4_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-60,   0f));
		SpringParticle unPin4_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-60,   0f));
//		SpringParticle unPin5_1 = new SpringParticle(new Vector3(originpos.x+-8f,   originpos.y+-80f,   2f));
		SpringParticle unPin5_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-70f,   2f));
//		SpringParticle unPin5_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-80f,   2f));
		pin4.addSpring(pin1, 24f, springK, damper);
		pin4.addSpring(unPin2_1, 24f, springK, damper);

		unPin2_1.addSpring(pin4,     24f, springK, damper );
		unPin2_1.addSpring(unPin2_2, 24f, springK, damper);
		unPin2_1.addSpring(unPin3_1, 24f, springK, damper);

		unPin2_2.addSpring(pin5,     24f, springK, damper );
		unPin2_2.addSpring(unPin2_3, 24f, springK, damper);
		unPin2_2.addSpring(unPin2_1, 24f, springK, damper);
		unPin2_2.addSpring(unPin3_2, 24f, springK, damper);

		unPin2_3.addSpring(pin6,     24f, springK, damper);
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
//		unPin4_1.addSpring(unPin5_1, 24f, springK, damper);

		unPin4_2.addSpring(unPin3_2, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_1, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_3, 24f, springK, damper);
		unPin4_2.addSpring(unPin5_2, 24f, springK, damper);

		unPin4_3.addSpring(unPin3_3, 24f, springK, damper);
		unPin4_3.addSpring(unPin4_2, 24f, springK, damper);
//		unPin4_3.addSpring(unPin5_3, 24f, springK, damper);

//		unPin5_1.addSpring(unPin4_1, 24f, springK, damper);
//		unPin5_1.addSpring(unPin5_2, 24f, springK, damper);

		unPin5_2.addSpring(unPin4_2, 24f, springK, damper);
//		unPin5_2.addSpring(unPin5_1, 24f, springK, damper);
//		unPin5_2.addSpring(unPin5_3, 24f, springK, damper);

//		unPin5_3.addSpring(unPin4_3, 24f, springK, damper);
//		unPin5_3.addSpring(unPin5_2, 24f, springK, damper);

		cloth.addLink(pin1);
		cloth.addLink(pin2);
		cloth.addLink(pin3);

		cloth.addLink(pin4);
		cloth.addLink(pin5);
		cloth.addLink(pin6);

		cloth.addLink(unPin2_1);
		cloth.addLink(unPin2_2);
		cloth.addLink(unPin2_3);

		cloth.addLink(unPin3_1);
		cloth.addLink(unPin3_2);
		cloth.addLink(unPin3_3);

		cloth.addLink(unPin4_1);
		cloth.addLink(unPin4_2);
		cloth.addLink(unPin4_3);

//		cloth.addLink(unPin5_1);
		cloth.addLink(unPin5_2);
//		cloth.addLink(unPin5_3);
	}

}
