package com.anythingmachine;

import java.util.ArrayList;
import java.util.Iterator;

import com.anythingmachine.physicsEngine.particleEngine.particles.UIParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class GamePlayUI {
	private Sprite sprite;
	public ArrayList<Sprite> uiByIndex;
	public ArrayList<UIParticle> uiByStack;
	private float stackwaittime = 5;

	public GamePlayUI() {
		uiByIndex = new ArrayList<Sprite>();
		uiByStack = new ArrayList<UIParticle>();
	}

	public void addSprite(Sprite spr) {
		uiByIndex.add(spr);
	}

	public void setSprite(int index) {
		sprite = uiByIndex.get(index);
	}

	public void addToStack(Vector3 pos, UILayout where, String region,
			String atlas) {
		Vector3 initpos = pos;
		int size = uiByStack.size() - 1;
		if ( size < 3 ) {
		if (size > 0) {
			switch (where) {
			case RIGHT:
				for (int i = size; i >= 0; i--) {
					UIParticle s = uiByStack.get(i);
					float height = s.getHeight();
					if (i == size || true) {
						// s.setTarget(Util.addVecs(new Vector3(0, height, 0),
						// s.getPos()));
						s.addPos(0, height);
					}
					// else {
					// UIParticle last = uiByStack.get(i);
					// // s.setTarget(new Vector3(0, last.getY()+height, 0));
					// s.setPos(0, last.getY()+height);
					// }
				}
				break;
			case LEFT:
				break;
			case BOTTOM:
				break;
			case TOP:
				break;
			default:
				break;
			}
		}
		uiByStack.add(new UIParticle(initpos, region, atlas, stackwaittime));
		}
	}

	public void update(float dt) {
		Iterator it = uiByStack.iterator();
		while (it.hasNext()) {
			UIParticle s = (UIParticle) it.next();
			if (s.isOverTime()) {
				it.remove();
			} else {
				s.update(dt);
			}
		}
	}

	public boolean isStacked() {
		return uiByStack.size() > 0;
	}

	public void draw(Batch batch) {
		for (UIParticle s : uiByStack) {
			s.draw(batch);
		}
	}

	public enum UILayout {
		TOP, RIGHT, BOTTOM, LEFT, CENTER
	}

}
