package com.anythingmachine.witchcraft;

import static com.anythingmachine.witchcraft.Util.Util.DEV_MODE;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.anythingmachine.tiledMaps.Camera;
import com.anythingmachine.tiledMaps.TiledMapHelper;
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
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
	public static float dt = 1f / 30f;

	private long lastRender;
	private TiledMapHelper tiledMapHelper;
	private PolygonSpriteBatchWrap polygonBatch;
	private SpriteBatch spriteBatch;
	private float xGrid;
	private int camWorldSize;
	private Calendar cal;
	private float dawnDuskProgress = 0;
	private int screenWidth;
	private int screenHeight;
	private MyContactListener contactListener;
	private LoadScript script;

	//test fields
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private NonPlayer npc1;
	private NonPlayer npc2;
	private NonPlayer npc3;
	private NonPlayer npc4;
	private NonPlayer npc5;

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
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
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
		tiledMapHelper.loadMap(spriteBatch);
		cam = new Camera(screenWidth, screenHeight);
		tiledMapHelper.prepareCamera(screenWidth, screenHeight);

		spriteBatch = new SpriteBatch();
		polygonBatch = new PolygonSpriteBatchWrap();
		shapeRenderer = new ShapeRenderer();

		//script = new LoadScript("helloworld.lua");

		debugRenderer = new Box2DDebugRenderer();
		ground = new Ground(world);
		ground.readCurveFile("data/groundcurves.txt", -240, -320);

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

		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER, 1);

		lastRender = System.nanoTime();

		ON_ANDROID = Gdx.app.getType() == ApplicationType.Android;
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		long now = System.nanoTime();
		float dT = Gdx.graphics.getDeltaTime();

		if ( ON_ANDROID ) {
			Gdx.app.log("***************************frames per sec: ", ""
				+ Gdx.app.getGraphics().getFramesPerSecond());
		}
		world.step(dT, 1, 1);

		rk4.step(dt);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		 Color c = getTimeOfDay();
//		 Gdx.gl.glClearColor((float) c.getRed() / 255f,
//		 (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 1f);

		player.update(dT);
		npc1.update(dT);
		npc2.update(dT);
		npc3.update(dT);
		npc4.update(dT);
		npc5.update(dT);

		Vector2 playerPos = player.getPosPixels();
		xGrid = Camera.camera.position.x = playerPos.x;
		float yGrid = Camera.camera.position.y = playerPos.y;
		if (xGrid < Gdx.graphics.getWidth() / 2) {
			xGrid = Camera.camera.position.x = Gdx.graphics.getWidth() / 2;
		}
		if (yGrid < Gdx.graphics.getHeight() / 2) {
			Camera.camera.position.y = Gdx.graphics.getHeight() / 2;
		}

		camWorldSize = (int) (Gdx.graphics.getWidth() * Camera.camera.zoom);

		cam.update();

		tiledMapHelper.render(this);
		spriteBatch.begin();
		player.drawUI(spriteBatch);
		spriteBatch.end();
		if (DEV_MODE)
			debugRenderer.render(world, Camera.camera.combined.scale(
					Util.PIXELS_PER_METER, Util.PIXELS_PER_METER,
					Util.PIXELS_PER_METER));

		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}

		lastRender = now;
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

	public void drawPlayerLayer() {
		Matrix4 combined = Camera.camera.combined;
		polygonBatch.setProjectionMatrix(combined);
		polygonBatch.begin();

		ground.draw(polygonBatch,
				(int) (xGrid - (Gdx.graphics.getWidth() / 2.f))
						/ Util.curveLength, camWorldSize / Util.curveLength);

		polygonBatch.end();

		spriteBatch.setProjectionMatrix(combined);
		spriteBatch.begin();
		ground.drawGroundElems(spriteBatch,
				(int) (xGrid - (Gdx.graphics.getWidth() / 2.f))
						/ Util.curveLength, camWorldSize / Util.curveLength);
		if (DEV_MODE) {
			//ground.drawDebugCurve(shapeRenderer);
		}

		npc2.draw(spriteBatch);
		npc1.draw(spriteBatch);
		npc3.draw(spriteBatch);
		npc4.draw(spriteBatch);
		npc5.draw(spriteBatch);

		player.draw(spriteBatch, combined);

		rk4System.draw(spriteBatch);

		spriteBatch.end();
		player.drawCape(Camera.camera.combined);
		// player.drawCape(shapeRenderer);

	}

	@Override
	public void resize(int width, int height) {
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

		// load level
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		assetManager.load("data/world/level1/level1.tmx", TiledMap.class);
		assetManager.finishLoading();
	}
}
