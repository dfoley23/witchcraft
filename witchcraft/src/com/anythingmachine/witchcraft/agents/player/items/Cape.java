package com.anythingmachine.witchcraft.agents.player.items;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.Cloth;
import com.anythingmachine.physicsEngine.KinematicParticle;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.SpringParticle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Cape {
	private ArrayList< KinematicParticle > pins;
	private ArrayList<Vector3> pinpos;
	private Cloth cloth;
	private boolean flip;
	
	public Cape(int width, int height, RK4Integrator rk4) {
		pins = new ArrayList<KinematicParticle>();
		pinpos = new ArrayList<Vector3>();
		cloth = new Cloth(width, height);
		buildCloth(cloth);
		rk4.addComponent(cloth);
	}
	
	public void draw( Matrix4 cam ) {
		cloth.draw(cam);
	}
	
	public void updatePos( float x, float y, boolean flip, boolean rotate ) {
		int size = pins.size();
		if (flip ) {
			int j = 0;
			if (rotate) {
				for( int i=size-1; i>=0; i-- ) {
					Vector3 pos = pinpos.get(j);
					pins.get(i).setPos(pos.y+x, pinpos.get(i).x+y, pos.z);
					j++;
				}			
			} else {
				for( int i=size-1; i>=0; i-- ) {
					Vector3 pos = pinpos.get(j);
					pins.get(i).setPos(pos.x+x, pinpos.get(i).y+y, pos.z);
					j++;
				}			
			}
		} else {
			if ( rotate ) {
				for( int i=0; i<size; i++ ) {
					Vector3 pos = pinpos.get(i);
					pins.get(i).setPos(pos.y+x, pos.x+y, pos.z);
				}
			} else {
				for( int i=0; i<size; i++ ) {
					Vector3 pos = pinpos.get(i);
					pins.get(i).setPos(pos.x+x, pos.y+y, pos.z);
				}
			}
		}
	}
	
	public void flip(boolean val) {
		flip = val;
	}
	
	public void addWindForce(float x, float y) {
		cloth.addForce(new Vector3(x, y, 0));
	}
	
	private void buildCloth(Cloth cloth) { 
		float springK = 350f;
		float damper = 0.75f;
		KinematicParticle pin1 = new KinematicParticle(new Vector3(0f,  0f,  -30f));
		KinematicParticle pin2 = new KinematicParticle(new Vector3(-15f, 8f,   -22f));
		KinematicParticle pin3 = new KinematicParticle(new Vector3(-30f,  16f,  -14f));
//		KinematicParticle pin4 = new KinematicParticle(new Vector3(-8f, 100f,   -8f));
//		KinematicParticle pin5 = new KinematicParticle(new Vector3(-10f, 100f,   -8f));
//		KinematicParticle pin6 = new KinematicParticle(new Vector3(-12f, 100f,   -8f));
		pins.add(pin1);
		pins.add(pin2);
		pins.add(pin3);
//		pins.add(pin4);
//		pins.add(pin5);
//		pins.add(pin6);
		pinpos.add(new Vector3(pin1.getPos().x,pin1.getPos().y, pin1.getPos().x));
		pinpos.add(new Vector3(pin2.getPos().x,pin2.getPos().y, pin2.getPos().x));
		pinpos.add(new Vector3(pin3.getPos().x,pin3.getPos().y, pin3.getPos().x));
//		pinpos.add(new Vector2(pin4.getPos().x,pin4.getPos().y));
//		pinpos.add(new Vector2(pin5.getPos().x,pin5.getPos().y));
//		pinpos.add(new Vector2(pin6.getPos().x,pin6.getPos().y));
		SpringParticle unPin1_1 = new SpringParticle(new Vector3(-8f,   -16f,   -8f));
		SpringParticle unPin1_2 = new SpringParticle(new Vector3(-16f,  -16f,   -8f));
		SpringParticle unPin1_3 = new SpringParticle(new Vector3(-24f,  -16f,   -8f));
//		SpringParticle unPin1_4 = new SpringParticle(new Vector3(-32f,  84f,   -8f));
//		SpringParticle unPin1_5 = new SpringParticle(new Vector3(-40f,  84f,   -8f));
//		SpringParticle unPin1_6 = new SpringParticle(new Vector3(-48f,  84f,   -8f));
		SpringParticle unPin2_1 = new SpringParticle(new Vector3( -8f,  -32f,   -4f));
		SpringParticle unPin2_2 = new SpringParticle(new Vector3(-16f,  -32f,   -4f));
		SpringParticle unPin2_3 = new SpringParticle(new Vector3(-24f,  -32f,   -4f));
//		SpringParticle unPin2_4 = new SpringParticle(new Vector3(-32f,  68f,   -4f));
//		SpringParticle unPin2_5 = new SpringParticle(new Vector3(-40f,  68f,   -4f));
//		SpringParticle unPin2_6 = new SpringParticle(new Vector3(-48f,  68f,   -4f));
		SpringParticle unPin3_1 = new SpringParticle(new Vector3( -8f,  -48f,   -2f));
		SpringParticle unPin3_2 = new SpringParticle(new Vector3(-16f,  -48f,   -2f));
		SpringParticle unPin3_3 = new SpringParticle(new Vector3(-24f,  -48f,   -2f));
//		SpringParticle unPin3_4 = new SpringParticle(new Vector3(-32f,  52f,   -2f));
//		SpringParticle unPin3_5 = new SpringParticle(new Vector3(-40f,  52f,   -2f));
//		SpringParticle unPin3_6 = new SpringParticle(new Vector3(-48f,  52f,   -2f));
		SpringParticle unPin4_1 = new SpringParticle(new Vector3( -8f,  -64,   0f));
		SpringParticle unPin4_2 = new SpringParticle(new Vector3(-16f,  -64,   0f));
		SpringParticle unPin4_3 = new SpringParticle(new Vector3(-24f,  -64,   0f));
//		SpringParticle unPin4_4 = new SpringParticle(new Vector3(-32f,  36f,   0f));
//		SpringParticle unPin4_5 = new SpringParticle(new Vector3(-40f,  36f,   0f));
//		SpringParticle unPin4_6 = new SpringParticle(new Vector3(-48f,  36f,   0f));
		SpringParticle unPin5_1 = new SpringParticle(new Vector3(-8f,   -80f,   2f));
		SpringParticle unPin5_2 = new SpringParticle(new Vector3(-16f,  -80f,   2f));
		SpringParticle unPin5_3 = new SpringParticle(new Vector3(-24f,  -80f,   2f));
//		SpringParticle unPin5_4 = new SpringParticle(new Vector3(-32f,  20f,   2f));
//		SpringParticle unPin5_5 = new SpringParticle(new Vector3(-40f,  20f,   2f));
//		SpringParticle unPin5_6 = new SpringParticle(new Vector3(-48f,  20f,   2f));

/*		SpringParticle unPin6_1 = new SpringParticle(new Vector3(0f,  2f,   -24f));
		SpringParticle unPin6_2 = new SpringParticle(new Vector3(0f,  2f,    -8f));
		SpringParticle unPin6_3 = new SpringParticle(new Vector3(0f,  2f,     8f));
		SpringParticle unPin6_4 = new SpringParticle(new Vector3(0f,  2f,    24f));
		SpringParticle unPin6_5 = new SpringParticle(new Vector3(0f,  84f,    8f));
		SpringParticle unPin6_6 = new SpringParticle(new Vector3(0f,  84f,   24f));

		SpringParticle unPin7_1 = new SpringParticle(new Vector3(0f, -14f,  -24f));
		SpringParticle unPin7_2 = new SpringParticle(new Vector3(0f, -14f,   -8f));
		SpringParticle unPin7_3 = new SpringParticle(new Vector3(0f, -14f,    8f));
		SpringParticle unPin7_4 = new SpringParticle(new Vector3(0f, -14f,   24f));
		SpringParticle unPin7_5 = new SpringParticle(new Vector3(0f,  84f,    8f));
		SpringParticle unPin7_6 = new SpringParticle(new Vector3(0f,  84f,   24f));

		SpringParticle unPin8_1 = new SpringParticle(new Vector3(0f, -30f,  -24f));
		SpringParticle unPin8_2 = new SpringParticle(new Vector3(0f, -30f,   -8f));
		SpringParticle unPin8_3 = new SpringParticle(new Vector3(0f, -30f,    8f));
		SpringParticle unPin8_4 = new SpringParticle(new Vector3(0f, -30f,   24f));
*/
		unPin1_1.addSpring(pin1,     24f, springK, damper );
		unPin1_1.addSpring(unPin1_2, 24f, springK, damper );
		unPin1_1.addSpring(unPin2_1, 24f, springK, damper );

		unPin1_2.addSpring(pin2,     24f, springK, damper );
		unPin1_2.addSpring(unPin1_1, 24f, springK, damper );
		unPin1_2.addSpring(unPin1_3, 24f, springK, damper );
		unPin1_2.addSpring(unPin2_2, 24f, springK, damper );
		
		unPin1_3.addSpring(pin3,     24f, springK, damper);
		unPin1_3.addSpring(unPin1_2, 24f, springK, damper);
//		unPin1_3.addSpring(unPin1_4, 24f, springK, damper);
		unPin1_3.addSpring(unPin2_3, 24f, springK, damper);

//		unPin1_4.addSpring(pin4,     24f, springK, damper);
//		unPin1_4.addSpring(unPin1_3, 24f, springK, damper);
//		unPin1_4.addSpring(unPin1_5, 24f, springK, damper);
//		unPin1_4.addSpring(unPin2_4, 24f, springK, damper);
//
//		unPin1_5.addSpring(pin5,     24f, springK, damper);
//		unPin1_5.addSpring(unPin1_4, 24f, springK, damper);
//		unPin1_5.addSpring(unPin1_6, 24f, springK, damper);
//		unPin1_5.addSpring(unPin2_5, 24f, springK, damper);
//
//		unPin1_6.addSpring(pin6,     24f, springK, damper);
//		unPin1_6.addSpring(unPin1_5, 24f, springK, damper);
//		unPin1_6.addSpring(unPin2_6, 24f, springK, damper);

		unPin2_1.addSpring(unPin1_1, 24f, springK, damper);
		unPin2_1.addSpring(unPin2_2, 24f, springK, damper);
		unPin2_1.addSpring(unPin3_1, 24f, springK, damper);

		unPin2_2.addSpring(unPin1_2, 24f, springK, damper);
		unPin2_2.addSpring(unPin2_3, 24f, springK, damper);
		unPin2_2.addSpring(unPin2_1, 24f, springK, damper);
		unPin2_2.addSpring(unPin3_2, 24f, springK, damper);

		unPin2_3.addSpring(unPin1_3, 24f, springK, damper);
		unPin2_3.addSpring(unPin2_2, 24f, springK, damper);
//		unPin2_3.addSpring(unPin2_4, 24f, springK, damper);
		unPin2_3.addSpring(unPin3_3, 24f, springK, damper);

//		unPin2_4.addSpring(unPin1_4, 24f, springK, damper);
//		unPin2_4.addSpring(unPin2_3, 24f, springK, damper);
//		unPin2_4.addSpring(unPin2_5, 24f, springK, damper);
//		unPin2_4.addSpring(unPin3_4, 24f, springK, damper);
//
//		unPin2_5.addSpring(unPin1_5, 24f, springK, damper);
//		unPin2_5.addSpring(unPin2_4, 24f, springK, damper);
//		unPin2_5.addSpring(unPin2_6, 24f, springK, damper);
//		unPin2_5.addSpring(unPin3_5, 24f, springK, damper);
//
//		unPin2_6.addSpring(unPin1_6, 24f, springK, damper);
//		unPin2_6.addSpring(unPin2_5, 24f, springK, damper);
//		unPin2_6.addSpring(unPin3_6, 24f, springK, damper);

		unPin3_1.addSpring(unPin2_1, 24f, springK, damper);
		unPin3_1.addSpring(unPin3_2, 24f, springK, damper);
		unPin3_1.addSpring(unPin4_1, 24f, springK, damper);

		unPin3_2.addSpring(unPin2_2, 24f, springK, damper);
		unPin3_2.addSpring(unPin3_1, 24f, springK, damper);
		unPin3_2.addSpring(unPin3_3, 24f, springK, damper);
		unPin3_2.addSpring(unPin4_2, 24f, springK, damper);

		unPin3_3.addSpring(unPin2_3, 24f, springK, damper);
		unPin3_3.addSpring(unPin3_2, 24f, springK, damper);
//		unPin3_3.addSpring(unPin3_4, 24f, springK, damper);
		unPin3_3.addSpring(unPin4_3, 24f, springK, damper);

//		unPin3_4.addSpring(unPin2_4, 24f, springK, damper);
//		unPin3_4.addSpring(unPin3_3, 24f, springK, damper);
//		unPin3_4.addSpring(unPin3_5, 24f, springK, damper);
//		unPin3_4.addSpring(unPin4_4, 24f, springK, damper);
//
//		unPin3_5.addSpring(unPin2_5, 24f, springK, damper);
//		unPin3_5.addSpring(unPin3_4, 24f, springK, damper);
//		unPin3_5.addSpring(unPin3_6, 24f, springK, damper);
//		unPin3_5.addSpring(unPin4_5, 24f, springK, damper);
//
//		unPin3_6.addSpring(unPin2_6, 24f, springK, damper);
//		unPin3_6.addSpring(unPin3_5, 24f, springK, damper);
//		unPin3_6.addSpring(unPin4_6, 24f, springK, damper);

		unPin4_1.addSpring(unPin3_1, 24f, springK, damper);
		unPin4_1.addSpring(unPin4_2, 24f, springK, damper);
		unPin4_1.addSpring(unPin5_1, 24f, springK, damper);

		unPin4_2.addSpring(unPin3_2, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_1, 24f, springK, damper);
		unPin4_2.addSpring(unPin4_3, 24f, springK, damper);
		unPin4_2.addSpring(unPin5_2, 24f, springK, damper);

		unPin4_3.addSpring(unPin3_3, 24f, springK, damper);
		unPin4_3.addSpring(unPin4_2, 24f, springK, damper);
//		unPin4_3.addSpring(unPin4_4, 24f, springK, damper);
		unPin4_3.addSpring(unPin5_3, 24f, springK, damper);

//		unPin4_4.addSpring(unPin3_4, 24f, springK, damper);
//		unPin4_4.addSpring(unPin4_3, 24f, springK, damper);
//		unPin4_4.addSpring(unPin4_5, 24f, springK, damper);
//		unPin4_4.addSpring(unPin5_4, 24f, springK, damper);
//
//		unPin4_5.addSpring(unPin3_5, 24f, springK, damper);
//		unPin4_5.addSpring(unPin4_4, 24f, springK, damper);
//		unPin4_5.addSpring(unPin4_6, 24f, springK, damper);
//		unPin4_5.addSpring(unPin5_5, 24f, springK, damper);
//
//		unPin4_6.addSpring(unPin3_6, 24f, springK, damper);
//		unPin4_6.addSpring(unPin4_5, 24f, springK, damper);
//		unPin4_6.addSpring(unPin5_6, 24f, springK, damper);

		unPin5_1.addSpring(unPin4_1, 24f, springK, damper);
		unPin5_1.addSpring(unPin5_2, 24f, springK, damper);

		unPin5_2.addSpring(unPin4_2, 24f, springK, damper);
		unPin5_2.addSpring(unPin5_1, 24f, springK, damper);
		unPin5_2.addSpring(unPin5_3, 24f, springK, damper);

		unPin5_3.addSpring(unPin4_3, 24f, springK, damper);
		unPin5_3.addSpring(unPin5_2, 24f, springK, damper);
//		unPin5_3.addSpring(unPin5_4, 24f, springK, damper);
//
//		unPin5_4.addSpring(unPin4_4, 24f, springK, damper);
//		unPin5_4.addSpring(unPin5_3, 24f, springK, damper);
//		unPin5_4.addSpring(unPin5_5, 24f, springK, damper);
//
//		unPin5_5.addSpring(unPin4_5, 24f, springK, damper);
//		unPin5_5.addSpring(unPin5_4, 24f, springK, damper);
//		unPin5_5.addSpring(unPin5_6, 24f, springK, damper);
//
//		unPin5_6.addSpring(unPin4_6, 24f, springK, damper);
//		unPin5_6.addSpring(unPin5_5, 24f, springK, damper);

		cloth.addLink(pin1);
		cloth.addLink(pin2);
		cloth.addLink(pin3);
//		cloth.addLink(pin4);
//		cloth.addLink(pin5);
//		cloth.addLink(pin6);

		cloth.addLink(unPin1_1);
		cloth.addLink(unPin1_2);
		cloth.addLink(unPin1_3);
//		cloth.addLink(unPin1_4);
//		cloth.addLink(unPin1_5);
//		cloth.addLink(unPin1_6);

		cloth.addLink(unPin2_1);
		cloth.addLink(unPin2_2);
		cloth.addLink(unPin2_3);
//		cloth.addLink(unPin2_4);
//		cloth.addLink(unPin2_5);
//		cloth.addLink(unPin2_6);

		cloth.addLink(unPin3_1);
		cloth.addLink(unPin3_2);
		cloth.addLink(unPin3_3);
//		cloth.addLink(unPin3_4);
//		cloth.addLink(unPin3_5);
//		cloth.addLink(unPin3_6);

		cloth.addLink(unPin4_1);
		cloth.addLink(unPin4_2);
		cloth.addLink(unPin4_3);
//		cloth.addLink(unPin4_4);
//		cloth.addLink(unPin4_5);
//		cloth.addLink(unPin4_6);

		cloth.addLink(unPin5_1);
		cloth.addLink(unPin5_2);
		cloth.addLink(unPin5_3);
//		cloth.addLink(unPin5_4);
//		cloth.addLink(unPin5_5);
//		cloth.addLink(unPin5_6);

	}

}
