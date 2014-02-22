package com.anythingmachine.witchcraft;

import java.util.HashMap;

import com.anythingmachine.tiledMaps.Camera;
import com.anythingmachine.witchcraft.GameStates.Loading;
import com.anythingmachine.witchcraft.GameStates.PauseMenu;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.GameStates.StartMenu;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WitchCraft implements ApplicationListener {
	public static Camera cam;
	public static AssetManager assetManager;
	public static boolean ON_ANDROID;
	public static int VIRTUAL_WIDTH = 1366;
	public static int VIRTUAL_HEIGHT = 768;
	public static float dt = 1f/ 30f;
	public static float ASPECT_RATIO = (float) VIRTUAL_WIDTH
			/ (float) VIRTUAL_HEIGHT;
	public static int screenWidth;
	public static int screenHeight;
	public static Rectangle viewport;
	public static float scale = 0.5f;
	public static HashMap<String, Screen> screens;
	public static Screen currentScreen;
	private long lastRender;
	private Batch spriteBatch;


	public WitchCraft() {
		super();
		screenWidth = -1;
		screenHeight = -1;
	}

	public WitchCraft(int width, int height) {
		super();
	}

	@Override
	public void create() {
		ON_ANDROID = Gdx.app.getType() == ApplicationType.Android;
		if (ON_ANDROID) {
			screenWidth = VIRTUAL_WIDTH;
			screenHeight = VIRTUAL_HEIGHT;
			dt = 1f/15f;
		} else {
			screenWidth = Gdx.app.getGraphics().getWidth();
			screenHeight = Gdx.app.getGraphics().getHeight();
		}

		if ( viewport == null )
			viewport = new Rectangle(0, 0, screenWidth, screenHeight);

		cam = new Camera(screenWidth, screenHeight);

		WitchCraft.assetManager = new AssetManager();
		loadPlayAssets();
		
		screens = new HashMap<String, Screen>();
		
		addScreens();
		
		spriteBatch = new SpriteBatch();

		// script = new LoadScript("helloworld.lua");

		lastRender = System.nanoTime();

		
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		float dT = Gdx.graphics.getDeltaTime();

		// if ( ON_ANDROID ) {
		// Gdx.app.log("***************************frames per sec: ", ""
		// + Gdx.app.getGraphics().getFramesPerSecond());
		// }
		long now = System.nanoTime();
		if (now - lastRender > 30000000) { // 30 ms, ~33FPS
			currentScreen.update(dT);
		}
		currentScreen.draw(spriteBatch);
	}

	@Override
	public void resize(int width, int height) {

		float aspectRatio = (float) width / (float) height;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);
		if (aspectRatio > ASPECT_RATIO) {
			scale = (float) height / (float) VIRTUAL_HEIGHT;
			crop.x = (width - VIRTUAL_WIDTH * scale) / 2f;
		} else if (aspectRatio < ASPECT_RATIO) {
			scale = (float) width / (float) VIRTUAL_WIDTH;
			crop.y = (height - VIRTUAL_HEIGHT * scale) / 2f;
		} else {
			scale = (float) width / (float) VIRTUAL_WIDTH;
		}

		float w = (float) VIRTUAL_WIDTH * scale;
		float h = (float) VIRTUAL_HEIGHT * scale;
		viewport = new Rectangle(crop.x, crop.y, w, h);

	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}
	
	private void addScreens() {
		screens.put("play", new GamePlayManager());
		screens.put("start", new StartMenu());
		screens.put("pause", new PauseMenu());
		screens.put("load", new Loading());
		currentScreen = screens.get("start");
	}
	
	public void loadPlayAssets() {
		assetManager.load("data/spine/characters.atlas", TextureAtlas.class);
		assetManager.load("data/world/otherart.atlas", TextureAtlas.class);
		assetManager.load("data/dust.png", Texture.class);

		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/level1/level1.tmx", TiledMap.class);
		WitchCraft.assetManager.load("data/world/level1/level2.tmx", TiledMap.class);

		assetManager.finishLoading();
	}

}
