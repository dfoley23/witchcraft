package com.anythingmachine.witchcraft.GameStates;

import java.util.ArrayList;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class StartMenu extends Menu {
	private Sprite logo;
	private Sprite lighting;
	private float timeout = 3.5f;
	private float time = 0;
	
	public StartMenu() {
		WitchCraft.assetManager.load("data/lightning.png", Texture.class);
		WitchCraft.assetManager.load("data/light.png", Texture.class);
		WitchCraft.assetManager.load("data/menu.atlas", TextureAtlas.class);
		WitchCraft.assetManager.finishLoading();
		uiimgs = new ArrayList<Sprite>();

		logo = new Sprite(WitchCraft.assetManager.get("data/lightning.png", Texture.class));
		logo.setScale(WitchCraft.viewport.width / logo.getWidth(), WitchCraft.viewport.height / logo.getHeight());
		logo.setPosition(WitchCraft.viewport.x+logo.getWidth()*0.25f, WitchCraft.viewport.y+32);

		lighting = new Sprite(WitchCraft.assetManager.get("data/light.png", Texture.class));
		lighting.setScale(WitchCraft.viewport.width / lighting.getWidth(), WitchCraft.viewport.height / lighting.getHeight());
		lighting.setPosition(WitchCraft.viewport.x+lighting.getWidth()*0.0f, WitchCraft.viewport.y+32);

		buildMenu();
	}

	@Override
	public void update(float dt) {
		time += dt;
		super.update(dt);
	}
	
	@Override
	public void draw(Batch batch) {
		batch.begin();
		if ( time > timeout ) {
			lighting.setColor(Color.WHITE);
			if ( time > timeout+1.25f)
				time = 0;
		} else {
			lighting.setColor(Color.BLACK);
		}
		lighting.draw(batch);
		logo.draw(batch);
		batch.end();
		super.draw(batch);
	}
	
	@Override
	public void transistionOut() {
		time =0;
		WitchCraft.assetManager.unload("data/lightning.png");
		WitchCraft.assetManager.unload("data/menu.atlas");
	}
	
	private void buildMenu() {
		Sprite sprite = new Sprite(WitchCraft.assetManager.get("data/menu.atlas", TextureAtlas.class).findRegion("begin"));
		sprite.setPosition(300, 300);
		uiimgs.add(sprite);
		sprite = new Sprite(WitchCraft.assetManager.get("data/menu.atlas", TextureAtlas.class).findRegion("continue"));
		sprite.setPosition(300, 200);
		uiimgs.add(sprite);		
	}
}
