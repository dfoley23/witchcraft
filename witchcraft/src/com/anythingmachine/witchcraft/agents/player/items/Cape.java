package com.anythingmachine.witchcraft.agents.player.items;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.Cloth;
import com.anythingmachine.physicsEngine.Particle;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.SpringParticle;
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
//		int size = pins.size();
//		if (flip ) {
//			int j = 0;
//			if (rotate) {
//				for( int i=size-1; i>=0; i-- ) {
//					Vector3 pos = pinpos.get(j);
//					pins.get(i).setPos(pinpos.get(i).y+x, pos.y+y, pos.z);
//					j++;
//				}			
//			} else {
//				for( int i=size-1; i>=0; i-- ) {
//					Vector3 pos = pinpos.get(j);
//					pins.get(i).setPos(pos.x+x, pinpos.get(i).y+y, pos.z);
//					j++;
//				}			
//			}
//		} else {
//			if ( rotate ) {
//				for( int i=0; i<size; i++ ) {
//					Vector3 pos = pinpos.get(i);
//					pins.get(i).setPos(pos.x+x, pos.x+y, pos.z);
//				}
//			} else {
//				for( int i=0; i<size; i++ ) {
//					Vector3 pos = pinpos.get(i);
//					pins.get(i).setPos(pos.x+x, pos.y+y, pos.z);
//				}
//			}
//		}
	}
	
	public void addWindForce(float x, float y) {
		cloth.addForce(new Vector3(x, y, 0));
	}
	
	private void buildCloth(Cloth cloth, Vector3 originpos) { 
		float springK = 350f;
		float damper = 0.75f;
		Particle pin1 = new Particle(new Vector3(10f,  0f,  -30f));
		Particle pin2 = new Particle(new Vector3(-15f, 6f,   -22f));
		Particle pin3 = new Particle(new Vector3(-30f, 0f,  -14f));
		pins.add(pin1);
		pins.add(pin2);
		pins.add(pin3);
		pinpos.add(new Vector3(pin1.getPos().x,pin1.getPos().y, pin1.getPos().x));
		pinpos.add(new Vector3(pin2.getPos().x,pin2.getPos().y, pin2.getPos().x));
		pinpos.add(new Vector3(pin3.getPos().x,pin3.getPos().y, pin3.getPos().x));
		SpringParticle unPin1_1 = new SpringParticle(new Vector3(originpos.x+-8f,   originpos.y+-16f,   -8f));
		SpringParticle unPin1_2 = new SpringParticle(new Vector3(originpos.x+-16f, originpos.y+ -16f,   -8f));
		SpringParticle unPin1_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-16f,   -8f));
		SpringParticle unPin2_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -32f,   -4f));
		SpringParticle unPin2_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-32f,   -4f));
		SpringParticle unPin2_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-32f,   -4f));
		SpringParticle unPin3_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -48f,   -2f));
		SpringParticle unPin3_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-48f,   -2f));
		SpringParticle unPin3_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-48f,   -2f));
		SpringParticle unPin4_1 = new SpringParticle(new Vector3(originpos.x+ -8f, originpos.y+ -64,   0f));
		SpringParticle unPin4_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-64,   0f));
		SpringParticle unPin4_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-64,   0f));
//		SpringParticle unPin5_1 = new SpringParticle(new Vector3(originpos.x+-8f,   originpos.y+-80f,   2f));
		SpringParticle unPin5_2 = new SpringParticle(new Vector3(originpos.x+-16f,  originpos.y+-80f,   2f));
//		SpringParticle unPin5_3 = new SpringParticle(new Vector3(originpos.x+-24f,  originpos.y+-80f,   2f));
		unPin1_1.addSpring(pin1,     24f, springK, damper );
		unPin1_1.addSpring(unPin1_2, 24f, springK, damper );
		unPin1_1.addSpring(unPin2_1, 24f, springK, damper );

		unPin1_2.addSpring(pin2,     24f, springK, damper );
		unPin1_2.addSpring(unPin1_1, 24f, springK, damper );
		unPin1_2.addSpring(unPin1_3, 24f, springK, damper );
		unPin1_2.addSpring(unPin2_2, 24f, springK, damper );
		
		unPin1_3.addSpring(pin3,     24f, springK, damper);
		unPin1_3.addSpring(unPin1_2, 24f, springK, damper);
		unPin1_3.addSpring(unPin2_3, 24f, springK, damper);

		unPin2_1.addSpring(unPin1_1, 24f, springK, damper);
		unPin2_1.addSpring(unPin2_2, 24f, springK, damper);
		unPin2_1.addSpring(unPin3_1, 24f, springK, damper);

		unPin2_2.addSpring(unPin1_2, 24f, springK, damper);
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

		cloth.addLink(unPin1_1);
		cloth.addLink(unPin1_2);
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

//		cloth.addLink(unPin5_1);
		cloth.addLink(unPin5_2);
//		cloth.addLink(unPin5_3);
	}

}
