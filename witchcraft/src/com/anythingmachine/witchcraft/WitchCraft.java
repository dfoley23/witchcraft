package com.anythingmachine.witchcraft;

import static com.anythingmachine.witchcraft.Util.Util.DEV_MODE;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.anythingmachine.tiledMaps.Camera;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.ParticleEngine.CloudEmitter;
import com.anythingmachine.witchcraft.ParticleEngine.CrowEmitter;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.Archer;
import com.anythingmachine.witchcraft.agents.Knight;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WitchCraft implements ApplicationListener {
	public static Player player;
	public static World world;
	public static Camera cam;
	private static RK4Integrator rk4;
	public static ParticleSystem rk4System;
	public static AssetManager assetManager;
	public static Ground ground;
	public static boolean ON_ANDROID;
	public static int VIRTUAL_WIDTH = 1366;
	public static int VIRTUAL_HEIGHT = 768;
	public static float ASPECT_RATIO = (float) VIRTUAL_WIDTH
			/ (float) VIRTUAL_HEIGHT;
	public static float dt = 1f / 30f;
	public Rectangle viewport;
	public float scale = 0.5f;

	private long lastRender;
	private TiledMapHelper tiledMapHelper;
	private Batch spriteBatch;
	private float xGrid;
	private int camWorldSize;
	private Calendar cal;
	private float dawnDuskProgress = 0;
	private int screenWidth;
	private int screenHeight;
	private MyContactListener contactListener;
	private LoadScript script;

	private CloudEmitter cloudE;
	private CrowEmitter crowE;

	// test fields
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private NonPlayer npc1;
	private NonPlayer npc2;
	private NonPlayer npc3;
	private NonPlayer npc4;
	private NonPlayer npc5;

	private float r = 0.7f;
	private float g = 0.7f;
	private float b = 0.7f;
	private float daynight = 1.2f;

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
		} else {
			screenWidth = Gdx.app.getGraphics().getWidth();
			screenHeight = Gdx.app.getGraphics().getHeight();
		}

		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		rk4 = new RK4Integrator();
		rk4System = new ParticleSystem(rk4);

		contactListener = new MyContactListener();
		world = new World(new Vector2(0.0f, -10.0f), false);
		world.setContactListener(contactListener);

		loadAssets();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.loadMap();
		cam = new Camera(screenWidth, screenHeight);
		tiledMapHelper.prepareCamera(screenWidth, screenHeight);

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		// script = new LoadScript("helloworld.lua");

		debugRenderer = new Box2DDebugRenderer();
		// ground = new Ground(world);
		// ground.readCurveFile("data/groundcurves.txt", -240, -320);

		player = new Player(rk4);
		npc1 = new Knight("knight2", "characters", new Vector2(354.0f, 3.0f),
				new Vector2(0.6f, 0.7f));
		npc2 = new Knight("knight1", "characters", new Vector2(800.0f, 3.0f),
				new Vector2(0.6f, 0.7f));
		npc3 = new Archer("archer", "characters", new Vector2(300.0f, 3.0f),
				new Vector2(0.6f, 0.7f));
		npc4 = new NonPlayer("civmalebrown", "characters", new Vector2(300.0f,
				3.0f), new Vector2(0.6f, 0.7f));
		npc5 = new NonPlayer("civfemaleblack-hood", "characters", new Vector2(
				300.0f, 3.0f), new Vector2(0.6f, 0.7f));

		cloudE = new CloudEmitter(17);
		crowE = new CrowEmitter(1);

		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER, 1);

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

			world.step(dT, 1, 1);

			rk4.step(dt);

			Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
					(int) viewport.width, (int) viewport.height);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			if (daynight > 0) {
				daynight -= dt * .001;
				r -= dt * .001;
				g -= dt * .001;
				b -= dt * .001;
				if (r < 0) {
					r = 0f;
				}
				if (g < 0) {
					g = 0f;
				}
				if (b < 0) {
					g = 0;
				}
				Gdx.gl.glClearColor(r, g, b, 1f);
			} else if (daynight > -1.2f) {
				daynight -= dt * .001;
				r += dt * .001;
				g += dt * .001;
				b += dt * .001;
				if (r < 0) {
					r = 0f;
				}
				if (g < 0) {
					g = 0f;
				}
				if (b < 0) {
					g = 0;
				}
				Gdx.gl.glClearColor(r, g, b, 1f);
			} else {
				Gdx.gl.glClearColor(r, g, b, 1f);
				daynight = 1.2f;
			}
			// Color c = getTimeOfDay();

			player.update(dT);
			npc1.update(dT);
			npc2.update(dT);
			npc3.update(dT);
			npc4.update(dT);
			npc5.update(dT);

			cloudE.update(dT);
			crowE.update(dT);

			Vector3 playerPos = player.getPosPixels();
			xGrid = Camera.camera.position.x = playerPos.x;
			float yGrid = Camera.camera.position.y = playerPos.y;
			if (xGrid < screenWidth * (scale)) {
				xGrid = Camera.camera.position.x = screenWidth * (scale);
			}
			if (yGrid < screenHeight * (scale)) {
				Camera.camera.position.y = screenHeight * (scale);
			}

			camWorldSize = (int) (screenWidth * 1);

			cam.update();
		}

		tiledMapHelper.render(this);
		spriteBatch.begin();
		player.drawUI(spriteBatch);
		spriteBatch.end();
		if (DEV_MODE)
			debugRenderer.render(world, Camera.camera.combined.scale(
					Util.PIXELS_PER_METER, Util.PIXELS_PER_METER,
					Util.PIXELS_PER_METER));

	}

	public Color getTimeOfDay() {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 20 || hour < 5) {
			return Color.DARK_GRAY;
		} else if (hour >= 6 && hour < 19) {
			return Color.LIGHT_GRAY;
		} else {
			if (hour < 6) {
				int min = cal.get(Calendar.MINUTE);
				if (min == 0) {
					dawnDuskProgress = 0.25f;
				} else {
					dawnDuskProgress = ((float) min / 60.f * .5f);
				}
				return new Color(dawnDuskProgress, dawnDuskProgress,
						dawnDuskProgress);
			} else {
				int min = cal.get(Calendar.MINUTE);
				if (min == 0) {
					dawnDuskProgress = 1f;
				} else {
					dawnDuskProgress = ((1f - (float) min / 60.f) * .5f);
				}
				return new Color(dawnDuskProgress, dawnDuskProgress,
						dawnDuskProgress);
			}
		}
	}

	public void drawBackGround(Batch batch) {
//		spriteBatch.setProjectionMatrix(Camera.camera.combined);
//		spriteBatch.begin();
		cloudE.draw(batch);
		crowE.draw(batch);
//		spriteBatch.end();
	}

	public void drawPlayerLayer(Batch batch) {
		// Matrix4 combined = Camera.camera.combined;
		// polygonBatch.setProjectionMatrix(combined);
		// polygonBatch.begin();
		//
		// ground.draw(polygonBatch,
		// (int) (xGrid - (screenWidth / 2.f))
		// / Util.curveLength, camWorldSize / Util.curveLength);

		// polygonBatch.end();

//		spriteBatch.setProjectionMatrix(Camera.camera.combined);
//		spriteBatch.begin();
		// ground.drawGroundElems(spriteBatch,
		// (int) (xGrid - (screenWidth / 2.f))
		// / Util.curveLength, camWorldSize / Util.curveLength);
		if (DEV_MODE) {
			// ground.drawDebugCurve(shapeRenderer);
		}

		npc2.draw(batch);
		npc1.draw(batch);
		npc3.draw(batch);
		npc4.draw(batch);
		npc5.draw(batch);

		player.draw(batch);

		rk4System.draw(batch);

//		spriteBatch.end();
		batch.end();
		player.drawCape(Camera.camera.combined);
		batch.begin();
		// player.drawCape(shapeRenderer);
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

	public void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load("data/spine/characters.atlas", TextureAtlas.class);
		assetManager.load("data/world/otherart.atlas", TextureAtlas.class);
		assetManager.load("data/dust.png", Texture.class);
		// load level
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		assetManager.load("data/world/level1/level1.tmx", TiledMap.class);
		assetManager.finishLoading();
	}
}
