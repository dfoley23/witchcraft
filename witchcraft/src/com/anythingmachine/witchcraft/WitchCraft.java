package com.anythingmachine.witchcraft;

import static com.anythingmachine.witchcraft.Util.Util.DEV_MODE;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.assets.AssetManager;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.Archer;
import com.anythingmachine.witchcraft.agents.Knight;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.anythingmachine.witchcraft.ground.Ground;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WitchCraft implements ApplicationListener {
	// the time the last frame was rendered, used for throttling framerate
	private long lastRender;
	private TiledMapHelper tiledMapHelper;
	public static AssetManager assetManager;
	private NonPlayer npc1;
	private NonPlayer npc2;
	private NonPlayer npc3;
	public static Ground ground;
	private Box2DDebugRenderer debugRenderer;
	private PolygonSpriteBatchWrap polygonBatch;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	public static Player player;
	public static World world;
	public static RK4Integrator rk4;
	private float xGrid;
	private int camWorldSize;
	private Calendar cal;
	private float dawnDuskProgress = 0;
	private LoadScript script;
	private float dt = 1f / 30f;
	private MyContactListener contactListener;
	private int screenWidth;
	private int screenHeight;

	public WitchCraft() {
		super();

		// Defer until create() when Gdx is initialized.
		screenWidth = -1;
		screenHeight = -1;
	}

	public WitchCraft(int width, int height) {
		super();

		screenWidth = width;
		screenHeight = height;
	}

	@Override
	public void create() {
		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		rk4 = new RK4Integrator();
		contactListener = new MyContactListener();
		world = new World(new Vector2(0.0f, -10.0f), false);
		world.setContactListener(contactListener);

		loadAssets();
		/**
		 * If the viewport's size is not yet known, determine it here.
		 */
		if (screenWidth == -1) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.setPackerDirectory("data/packer");
		tiledMapHelper.loadMap("data/world/level1/level.tmx");
		tiledMapHelper.prepareCamera(screenWidth, screenHeight);

		// overallTexture = new
		// Texture(Gdx.files.internal("data/tileSheet.png"));
		// overallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		spriteBatch = new SpriteBatch();
		polygonBatch = new PolygonSpriteBatchWrap();
		shapeRenderer = new ShapeRenderer();

		script = new LoadScript("helloworld.lua");

		debugRenderer = new Box2DDebugRenderer();

		ground = new Ground(world);
		ground.readCurveFile("data/groundcurves.txt", -240, -250);

		player = new Player();
		npc1 = new Knight("knight2", "npcsheet1", new Vector2(354.0f, 3.0f));
		npc2 = new Knight("knight1", "npcsheet1", new Vector2(800.0f, 3.0f));
		npc3 = new Archer("archer", "npcsheet1", new Vector2(300.0f, 3.0f));
		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER);

		lastRender = System.nanoTime();
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		long now = System.nanoTime();
		float dT = Gdx.graphics.getDeltaTime();

		world.step(dT, 1, 1);

		rk4.step(dt);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Color c = getTimeOfDay();
		Gdx.gl.glClearColor((float) c.getRed() / 255f,
				(float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 1f);

		player.update(dT);
		npc1.update(dT);
		npc2.update(dT);
		npc3.update(dT);

		Vector3 playerPos = player.getPosPixels();
		xGrid = tiledMapHelper.getCamera().position.x = playerPos.x;
		float yGrid = tiledMapHelper.getCamera().position.y = playerPos.y;
		if (xGrid < Gdx.graphics.getWidth() / 2) {
			xGrid = tiledMapHelper.getCamera().position.x = Gdx.graphics
					.getWidth() / 2;
		}
		// else if (xGrid >= tiledMapHelper.getWidth()
		// - Gdx.graphics.getWidth() / 2) {
		// xGrid = tiledMapHelper.getCamera().position.x =
		// tiledMapHelper.getWidth()
		// - Gdx.graphics.getWidth() / 2;
		// }

		if (yGrid < Gdx.graphics.getHeight() / 2) {
			tiledMapHelper.getCamera().position.y = Gdx.graphics.getHeight() / 2;
		}
		// if (tiledMapHelper.getCamera().position.y >=
		// tiledMapHelper.getHeight()
		// - Gdx.graphics.getHeight() / 2) {
		// tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight();
		// }

		camWorldSize = (int) (Gdx.graphics.getWidth() * tiledMapHelper
				.getCamera().zoom);

		tiledMapHelper.getCamera().update();

		tiledMapHelper.render(this);

		// debugRenderer.render(world,
		// tiledMapHelper.getCamera().combined.scale(
		// Util.PIXELS_PER_METER,
		// Util.PIXELS_PER_METER,
		// Util.PIXELS_PER_METER));

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
		Matrix4 combined = tiledMapHelper.getCamera().combined;
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
			shapeRenderer.setProjectionMatrix(combined);
			ground.drawDebugCurve(shapeRenderer);
		}

		npc2.draw(spriteBatch);
		npc1.draw(spriteBatch);
		npc3.draw(spriteBatch);

		player.draw(spriteBatch, combined);
		spriteBatch.end();
		player.drawCape(tiledMapHelper.getCamera().combined);

		//
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
		assetManager
				.addAtlas(
						"npcsheet1",
						new TextureAtlas(Gdx.files
								.internal("data/spine/knight.atlas")));
		assetManager
				.addAtlas(
						"player",
						new TextureAtlas(Gdx.files
								.internal("data/spine/player.atlas")));
	}
}
