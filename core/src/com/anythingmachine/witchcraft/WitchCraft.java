package com.anythingmachine.witchcraft;

import java.util.HashMap;

import com.anythingmachine.cinematics.Camera;
import com.anythingmachine.soundEngine.SoundManager;
import com.anythingmachine.witchcraft.GameStates.Loading;
import com.anythingmachine.witchcraft.GameStates.PauseMenu;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.GameStates.StartMenu;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WitchCraft extends ApplicationAdapter {
	public static Camera cam;
	public static AssetManager assetManager;
	public static boolean ON_ANDROID;
	public static int VIRTUAL_WIDTH = 1366;
	public static int VIRTUAL_HEIGHT = 768;
	public static float dt = 1f / 60f;
	public static float targetFrameTime = 1f / 60f;
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
	private Controller controller;
	public static SoundManager soundManager;

	private boolean paused = false;
	private float pressedPaused = 1.0f;

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
			targetFrameTime = 1f / 60f;
		} else {
			screenWidth = Gdx.app.getGraphics().getWidth();
			screenHeight = Gdx.app.getGraphics().getHeight();
		}

		if (viewport == null)
			viewport = new Rectangle(0, 0, screenWidth, screenHeight);

		cam = new Camera(screenWidth, screenHeight);

		WitchCraft.assetManager = new AssetManager();

		soundManager = new SoundManager();

		loadPlayAssets();
		
		screens = new HashMap<String, Screen>();

		addScreens();

		spriteBatch = new SpriteBatch();

		// script = new LoadScript("helloworld.lua");

		accum = 0;

		if (Controllers.getControllers().size > 0) {
			this.controller = Controllers.getControllers().get(0);
		}

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

	public static void playSound(String name) {
		soundManager.playSound(name);
	}
	
	public static void stopSound() {
		soundManager.stop();
	}
	
	public static void playMusic(String name) {
		soundManager.startMusic(name);
	}
	
	public static void stopMusic() {
		soundManager.stopMusic();
	}
	
	@Override
	public void render() {
		float actualDT = Gdx.graphics.getDeltaTime();
		if (!ON_ANDROID && pressedPaused > 0.44f && Gdx.input.isKeyPressed(Keys.P)) {
			paused = !paused;
			System.out.println("paused");
			pressedPaused = 0.0f;
		} else if( ON_ANDROID && pressedPaused > 0.44f && controller.getButton(82)) {
			paused = !paused;
			System.out.println("paused");
			pressedPaused = 0.0f;
		}
		pressedPaused = pressedPaused > 0.5f ? 1.0f : pressedPaused + actualDT;
		if (!paused || true) {
			accum += actualDT;
			if (ON_ANDROID) {
				Gdx.app.log("***************************frames per sec: ", ""
						+ Gdx.app.getGraphics().getFramesPerSecond());
			}

			Gdx.gl.glViewport((int) WitchCraft.viewport.x,
					(int) WitchCraft.viewport.y,
					(int) WitchCraft.viewport.width,
					(int) WitchCraft.viewport.height);
			
			soundManager.update(actualDT);
			
			if (accum >= targetFrameTime) {
				// if ( accum > dt || ON_ANDROID ) {
				// currentScreen.update(accum);
				// accum = accum - targetFrameTime;
				// } else {
				currentScreen.update(dt);
				accum = targetFrameTime;
				// }
			}

			WitchCraft.cam.update();
			currentScreen.draw(spriteBatch);

		}
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
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		assetManager.load("data/spine/animals.atlas", TextureAtlas.class);
		assetManager.load("data/world/otherart.atlas", TextureAtlas.class);
		 assetManager.load("data/sounds/crickets.ogg", Sound.class);
		assetManager.load("data/sounds/wind.wav", Sound.class);
		assetManager.load("data/sounds/alert.ogg", Sound.class);		
		assetManager.load("data/sounds/frogs.ogg", Music.class);		
		assetManager.load("data/sounds/ambient.ogg", Music.class);

		assetManager.finishLoading();
		
		soundManager.addSound("data/sounds/alert.ogg");
		soundManager.addSound("data/sounds/wind.wav");
		soundManager.addSound("data/sounds/crickets.ogg");
		soundManager.addMusic("data/sounds/frogs.ogg");
		soundManager.addMusic("data/sounds/ambient.ogg");
	}

}

