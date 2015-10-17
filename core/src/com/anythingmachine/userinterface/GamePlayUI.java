package com.anythingmachine.userinterface;

import java.util.ArrayList;
import java.util.Iterator;

import com.anythingmachine.GameStates.Containers.GamePlayManager;
import com.anythingmachine.physicsEngine.particleEngine.particles.UIParticle;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class GamePlayUI {
	private Sprite sprite;
	public ArrayList<Sprite> uiByIndex;
	public ArrayList<UIText> uiText;
	public ArrayList<UIParticle> uiByStack;
	private float stackwaittime = 5;
	private BitmapFont uiFont;
	private float widestText = 0;
	private float totalTextHeight = 0;
	private float startText = 999999;

	public GamePlayUI() {
		uiByIndex = new ArrayList<Sprite>();
		uiByStack = new ArrayList<UIParticle>();
		uiText = new ArrayList<UIText>();
		uiFont = new BitmapFont(Gdx.files.internal("data/mytimes.fnt"), Gdx.files.internal("data/mytimes.png"),
				false);
	}

	public void addSprite(Sprite spr) {
		uiByIndex.add(spr);
	}

	public void setSprite(int index) {
		sprite = uiByIndex.get(index);
	}

	public void addText(CharSequence str, UILayout where) {
		int size = uiText.size();
		float totalHeight = 0;
		for (int i = 0; i < size; i++) {
			UIText s = uiText.get(i);
			totalHeight += s.getHeight();
			if ( widestText < s.getWidth() ) {
				widestText = s.getWidth();
			}
			if ( startText > s.getX() ) {
				startText = s.getX();
			}
			float height = uiFont.getLineHeight();
			s.setMoveY(height);
		}
		UIText newUIText = new UIText(str, uiFont, where);
		totalHeight += newUIText.getHeight();
		if ( widestText < newUIText.getWidth() ) {
			widestText = newUIText.getWidth();
		}
		this.widestText += 32;
		this.totalTextHeight = totalHeight + uiFont.getLineHeight();
		uiText.add(newUIText);
	}

	public void clearText() {
		uiText.clear();
	}

	public void addToStack(Vector3 pos, UILayout where, String region, String atlas) {
		Vector3 initpos = pos;
		int size = uiByStack.size() - 1;
		if (size < 3) {
			if (size > 0) {
				switch (where) {
				case RIGHT:
					for (int i = size; i >= 0; i--) {
						UIParticle s = uiByStack.get(i);
						float height = s.getHeight();
						if (i == size || true) {
							// s.setTarget(Util.addVecs(new Vector3(0, height,
							// 0),
							// s.getPos()));
							s.addPos(0, height);
						}
						// else {
						// UIParticle last = uiByStack.get(i);
						// // s.setTarget(new Vector3(0, last.getY()+height,
						// 0));
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

	public void drawUIShapes() {
		if ( uiText.size() > 0 ) {
			GamePlayManager.shapeRenderer.setColor(Color.DARK_GRAY);
			GamePlayManager.shapeRenderer.begin(ShapeType.Filled);
			GamePlayManager.shapeRenderer.rect(startText, WitchCraft.viewport.height /2f, this.widestText, this.totalTextHeight);
			GamePlayManager.shapeRenderer.end();
		}
	}
	public void draw(Batch batch) {
		for (UIParticle s : uiByStack) {
			s.draw(batch);
		}
		for (UIText text : uiText) {
			uiFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			uiFont.draw(batch, text.getText(), text.getX(), text.getY());
//			text.draw(batch, uiFont);
		}
	}

	public enum UILayout {
		TOP, RIGHT, BOTTOM, LEFT, CENTER
	}

}
