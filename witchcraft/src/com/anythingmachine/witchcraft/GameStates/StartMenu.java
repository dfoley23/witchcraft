package com.anythingmachine.witchcraft.GameStates;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class StartMenu extends Menu {
	private Sprite logo;
	
	public StartMenu() {
		WitchCraft.assetManager.load("data/lightning.png", Texture.class);

		WitchCraft.assetManager.finishLoading();

		logo = new Sprite(WitchCraft.assetManager.get("data/lightning.png", Texture.class));
		
	}

	@Override
	public void transistionOut() {
		WitchCraft.assetManager.unload("data/lightning.png");
	}
	@Override
	public void draw(Batch batch) {
		batch.begin();
		logo.draw(batch);
		batch.end();
	}
}
