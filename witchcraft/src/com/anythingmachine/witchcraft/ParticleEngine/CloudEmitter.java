package com.anythingmachine.witchcraft.ParticleEngine;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CloudEmitter {
	private Random rand;
	private ArrayList<CloudParticle> clouds;

	public CloudEmitter(int limit) {
		rand = new Random();
		clouds = new ArrayList<CloudParticle>();
		Rectangle rect = WitchCraft.cam.getBounds();
		for (int i = 0; i < limit; i++) {
			Vector3 pos = new Vector3(rand.nextInt((int) rect.getWidth())
					+ rect.x, rect.y + rand.nextInt((int) rect.getHeight()), 0f);
			float speed = rand.nextInt(30) + 5;
			float scale = Math.max(0.25f, rand.nextInt(3) * rand.nextFloat());
			float rot = rand.nextInt(360);
			clouds.add(new CloudParticle(pos, scale, rot, -speed));
		}
	}

	public void update(float dt) {
		Rectangle rect = WitchCraft.cam.getBounds();
		rect.x -= 400;
		rect.y -= 400;
		rect.width += 400;
		rect.height += 400;
		for (CloudParticle c : clouds) {
			Vector3 pos = c.getPos();
			if (pos.x < rect.x) {
				float x = rect.x+rect.width-8;
				float y = rect.y + rand.nextInt((int) rect.height);
				float rot = rand.nextInt(360);
				c.setPos(x, y);
				c.setRotation(rot);
			} else if ( pos.x > rect.width+rect.x ) {
				float x = rect.x+8;
				float y = rect.y + rand.nextInt((int) rect.height);
				float rot = rand.nextInt(360);
				c.setPos(x, y);
				c.setRotation(rot);				
			} else if ( pos.y < rect.y ) {
				float y = rect.y+rect.height-8;
				float x = rect.x + rand.nextInt((int) rect.width);
				float rot = rand.nextInt(360);
				c.setPos(x, y);
				c.setRotation(rot);
			}else if ( pos.y > rect.y+rect.height) {
				float y = rect.y+8;
				float x = rect.x + rand.nextInt((int) rect.width);
				float rot = rand.nextInt(360);
				c.setPos(x, y);
				c.setRotation(rot);
			}
			c.update(dt);
		}
	}

	public void draw(Batch batch) {
		for (CloudParticle c : clouds) {
			c.draw(batch);
		}
	}
}
