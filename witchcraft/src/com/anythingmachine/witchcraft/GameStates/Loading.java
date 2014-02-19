package com.anythingmachine.witchcraft.GameStates;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Loading extends Screen {
	private String nextscreen;
	
	@Override
	public void setParent(String screen) {
		nextscreen = screen;
	}

	@Override
	public void update(float dt) {
		if (WitchCraft.assetManager.update() ) {
			switchScreen(nextscreen);
		}
	}
		
	@Override
	public void loadMenuAssets() {
		WitchCraft.assetManager.load("data/lightning.png", Texture.class);

		WitchCraft.assetManager.finishLoading();		
	}

	@Override
	public void loadLevel(String level) {
		// load level
		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/"+level+"/"+level+".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();
	}

}
