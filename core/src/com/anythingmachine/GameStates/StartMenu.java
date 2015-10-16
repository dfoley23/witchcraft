package com.anythingmachine.GameStates;

import java.util.ArrayList;

import com.anythingmachine.physicsEngine.particleEngine.CloudEmitter;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class StartMenu extends Menu {
	private static TiledMapHelper tiledMapHelper;
	private CloudEmitter cloudE;
	private Sprite logo;

	public StartMenu() {
		WitchCraft.assetManager.load("data/logo.png", Texture.class);
		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/level1/menu.tmx", TiledMap.class);
		WitchCraft.assetManager.load("data/menu.atlas", TextureAtlas.class);
		WitchCraft.assetManager.finishLoading();
		uiimgs = new ArrayList<Sprite>();

		cloudE = new CloudEmitter(25);

		buildMenu();
	}

	@Override
	public void update(float dt) {
		cloudE.update(dt);
		super.update(dt);
	}

	@Override
	public void draw(Batch batch) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1f);

		tiledMapHelper.render();
		// if ( time > timeout ) {
		//// lighting.setColor(Color.WHITE);
		// if ( time > timeout+1.25f)
		// time = 0;
		// } else {
		//// lighting.setColor(Color.BLACK);
		// }
		super.draw(batch);
		batch.begin();
		logo.draw(batch);
		batch.end();
		// batch.enableBlending();
		// batch.b
		// cloudE.draw(batch);
		// batch.disableBlending();
	}

	@Override
	public void transistionOut() {
		tiledMapHelper.destroyMap();
		WitchCraft.assetManager.unload("data/world/level1/menu.tmx");
		WitchCraft.assetManager.unload("data/menu.atlas");
	}

	@Override
	public void resize(int width, int height) {
	}

	private void buildMenu() {
		Sprite sprite = new Sprite(
				WitchCraft.assetManager.get("data/menu.atlas", TextureAtlas.class).findRegion("begin"));
		sprite.setPosition(700, 300);
		uiimgs.add(sprite);
		sprite = new Sprite(WitchCraft.assetManager.get("data/menu.atlas", TextureAtlas.class).findRegion("continue"));
		sprite.setPosition(700, 200);
		uiimgs.add(sprite);

		logo = new Sprite(WitchCraft.assetManager.get("data/logo.png", Texture.class));
		logo.setPosition(250, 550);

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.loadMenu("");
		tiledMapHelper.prepareCamera(WitchCraft.screenWidth, WitchCraft.screenHeight);

	}
}
