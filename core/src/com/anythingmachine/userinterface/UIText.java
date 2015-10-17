package com.anythingmachine.userinterface;

import com.anythingmachine.userinterface.GamePlayUI.UILayout;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class UIText {
	private CharSequence text;
	private float width;
	private float height;
	private float x;
	private float y;
	GlyphLayout layout = new GlyphLayout(); 
	
	public UIText(CharSequence txt, BitmapFont uiFont, UILayout where) {
		this.text = txt;
		layout.setText(uiFont, text);
		width = layout.width;
		height = layout.height;
		x = WitchCraft.viewport.width - width;
		y = WitchCraft.viewport.height /2f;
	}

	public void setMoveY(float dy) {
		y += dy;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public CharSequence getText() {
		return text;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void draw(Batch batch, BitmapFont font ) {
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, text, x, y);
	}

}
