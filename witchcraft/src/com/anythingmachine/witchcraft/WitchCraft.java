package com.anythingmachine.witchcraft;

import java.util.HashMap;

import com.anythingmachine.cinematics.Camera;
import com.anythingmachine.cinematics.CinematicTrigger;
import com.anythingmachine.witchcraft.GameStates.Loading;
import com.anythingmachine.witchcraft.GameStates.PauseMenu;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.GameStates.StartMenu;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WitchCraft implements ApplicationListener {
	public static Camera cam;
	public static AssetManager assetManager;
	public static boolean ON_ANDROID;
	public static int VIRTUAL_WIDTH = 1366;
	public static int VIRTUAL_HEIGHT = 768;
	public static float dt = 1f / 60f;
	public static float ASPECT_RATIO = (float) VIRTUAL_WIDTH
			/ (float) VIRTUAL_HEIGHT;
	public static int screenWidth;
	public static int screenHeight;
	public static Rectangle viewport;
	public static float scale = 0.5f;
	public static HashMap<String, Screen> screens;
	public static Screen currentScreen;
	private float accum;
	private Batch spriteBatch;

	/**** miX test ****/
	// SpriteBatch batch;
	// float time;
	// Array<Event> events = new Array();
	//
	// SkeletonRenderer renderer;
	// SkeletonRendererDebug debugRenderer;
	//
	// SkeletonData skeletonData;
	// Skeleton skeleton;
	// Animation walkAnimation;
	// Animation jumpAnimation;

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
			dt = 1f / 60f;
		} else {
			screenWidth = Gdx.app.getGraphics().getWidth();
			screenHeight = Gdx.app.getGraphics().getHeight();
		}

		if (viewport == null)
			viewport = new Rectangle(0, 0, screenWidth, screenHeight);

		cam = new Camera(screenWidth, screenHeight);

		WitchCraft.assetManager = new AssetManager();
		
		loadPlayAssets();

		screens = new HashMap<String, Screen>();

		addScreens();

		spriteBatch = new SpriteBatch();

		// script = new LoadScript("helloworld.lua");

		accum = 0;

		/******************************* MIX TEST *******************/
		// batch = new SpriteBatch();
		// renderer = new SkeletonRenderer();
		// debugRenderer = new SkeletonRendererDebug();
		//
		//
		// TextureAtlas atlas = new
		// TextureAtlas(Gdx.files.internal("data/spine/characters.atlas"));
		//
		// SkeletonBinary binary = new SkeletonBinary(atlas);
		// // binary.setScale(2);
		// skeletonData =
		// binary.readSkeletonData(Gdx.files.internal("data/spine/characters.skel"));
		// walkAnimation = skeletonData.findAnimation("walk");
		// jumpAnimation = skeletonData.findAnimation("drawbow");
		//
		// skeleton = new Skeleton(skeletonData);
		// skeleton.updateWorldTransform();
		// skeleton.setX(150);
		// skeleton.setY(120);

	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		float dT = Gdx.graphics.getDeltaTime();
		accum += dT;
//		if (ON_ANDROID || true) {
//			 Gdx.app.log("***************************frames per sec: ", ""
//			 + Gdx.app.getGraphics().getFramesPerSecond());
//		}

		Gdx.gl.glViewport((int) WitchCraft.viewport.x,
				(int) WitchCraft.viewport.y, (int) WitchCraft.viewport.width,
				(int) WitchCraft.viewport.height);
		if (accum >= dt) {
			currentScreen.update(dT);
			accum = accum - dt;
		}
		

		WitchCraft.cam.update();
		currentScreen.draw(spriteBatch);

		/* mix test */
		// float delta = Gdx.graphics.getDeltaTime() * 0.25f; // Reduced to make
		// mixing easier to see.
		//
		// float jump = jumpAnimation.getDuration();
		// float beforeJump = 1f;
		// float blendIn = 0.4f;
		// float blendOut = 0.4f;
		// float blendOutStart = beforeJump + jump - blendOut;
		// float total = 3.75f;
		//
		// time += delta;
		//
		// float speed = 180;
		// if (time > beforeJump + blendIn && time < blendOutStart) speed = 360;
		// skeleton.setX(skeleton.getX() + speed * delta);
		//
		// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//
		// // This shows how to manage state manually. See AnimationStatesTest.
		// if (time > total) {
		// // restart
		// time = 0;
		// skeleton.setX(-50);
		// } else if (time > beforeJump + jump) {
		// // just walk after jump
		// walkAnimation.apply(skeleton, time, time, true, events);
		// } else if (time > blendOutStart) {
		// // blend out jump
		// walkAnimation.apply(skeleton, time, time, true, events);
		// jumpAnimation.mix(skeleton, time - beforeJump, time - beforeJump,
		// false, events, 1 - (time - blendOutStart) / blendOut);
		// } else if (time > beforeJump + blendIn) {
		// // just jump
		// jumpAnimation.apply(skeleton, time - beforeJump, time - beforeJump,
		// false, events);
		// } else if (time > beforeJump) {
		// // blend in jump
		// walkAnimation.apply(skeleton, time, time, true, events);
		// jumpAnimation.mix(skeleton, time - beforeJump, time - beforeJump,
		// false, events, (time - beforeJump) / blendIn);
		// } else {
		// // just walk before jump
		// walkAnimation.apply(skeleton, time, time, true, events);
		// }
		//
		// skeleton.updateWorldTransform();
		// skeleton.update(Gdx.graphics.getDeltaTime());
		//
		// batch.begin();
		// renderer.draw(batch, skeleton);
		// batch.end();
		//
		// debugRenderer.draw(skeleton);

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
		currentScreen.resize(width, height);
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
//		assetManager.load("data/sounds/crickets.ogg", Sound.class);
		assetManager.load("data/sounds/wind.wav", Sound.class);
		
		assetManager.finishLoading();
	}

}
