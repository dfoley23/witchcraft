package com.anythingmachine.witchcraft.ParticleEngine;

import java.util.ArrayList;
import java.util.Random;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CrowEmitter {
	private Random rand;
	private ArrayList<Crow> crows;

	public CrowEmitter(int limit) {
		rand = new Random();
		crows = new ArrayList<Crow>();
		Rectangle rect = WitchCraft.cam.getBounds();
		for (int i = 0; i < limit; i++) {
			Vector3 pos = new Vector3(rand.nextInt((int) rect.getWidth())
					+ rect.x, rect.y + rand.nextInt((int) rect.getHeight()-200)+200, 0f);
			float speed = rand.nextInt(2) == 0 ? -200 : 200;
			crows.add(new Crow(pos, speed));
		}
	}

	public void update(float dt) {
		Rectangle rect = WitchCraft.cam.getBounds();
		rect.x -= 400;
		rect.y -= 400;
		rect.width += 400;
		rect.height += 400;
		for (Crow c : crows) {
			Vector3 pos = c.getPos();
			if (pos.x < rect.x || pos.x > rect.width + rect.x || pos.y < rect.y
					|| pos.y > rect.y + rect.height) {
				float speed = rand.nextInt(2) == 0 ? -200 : 200;
				float x;
				float y;
				if ( speed > 0 ) {
					x = rect.x+8;
					y = rect.y + rand.nextInt((int) rect.height-200)+200;
				} else {
					x = rect.x+rect.width-8;
					y = rect.y + rand.nextInt((int) rect.height-200)+200;
				}
				c.setPos(x, y);
			}
			c.update(dt);
		}
	}

	public void draw(Batch batch) {
		for (Crow c : crows) {
			c.draw(batch);
		}
	}

}
