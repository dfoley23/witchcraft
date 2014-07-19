package com.anythingmachine.witchcraft.GameStates.Containers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.cinematics.Camera;
import com.anythingmachine.cinematics.CameraState;
import com.anythingmachine.cinematics.CinematicSleeper;
import com.anythingmachine.cinematics.CinematicTrigger;
import com.anythingmachine.cinematics.actions.AnimateTimed;
import com.anythingmachine.cinematics.actions.FaceLeft;
import com.anythingmachine.cinematics.actions.Lerp;
import com.anythingmachine.cinematics.actions.ParticleToGamePlay;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.ParticleEngine.CloudEmitter;
import com.anythingmachine.witchcraft.ParticleEngine.CrowEmitter;
import com.anythingmachine.witchcraft.ParticleEngine.FireEmitter;
import com.anythingmachine.witchcraft.States.Player.PlayerStateEnum;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.npcs.NPCStaticAnimation;
import com.anythingmachine.witchcraft.agents.npcs.NPCType;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GamePlayManager extends Screen {
	public static Player player;
	public static World world;
	private static RK4Integrator rk4;
	public static ParticleSystem rk4System;
	private static TiledMapHelper tiledMapHelper;
	private float xGrid;
	private Calendar cal;
	private MyContactListener contactListener;
	private LoadScript script;
	public static int level = -1;
	public static int currentlevel;
	public static int oldlevel;
	public static ArrayList<Float> levels;
	public static boolean initworld = true;
	public static Sound currentsound;
	public static long currentsoundid;
	private static ArrayList<CinematicTrigger> triggers;
	private static CinematicTrigger cinema;
	private static CinematicTrigger sleepCinematic;
	private static StorySegment storySegment = StorySegment.BEGINNING;

	private CloudEmitter cloudE;
	private CrowEmitter crowE;

	// test fields
	private Box2DDebugRenderer debugRenderer;
	private static ArrayList<NonPlayer> npcs;
	private static ArrayList<Entity> entities;
	private NPCStaticAnimation shackled;
	private NPCStaticAnimation tied;

	public enum StorySegment {
		BEGINNING, POSTINTRODUCTION,
	}

	public GamePlayManager() {
		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		if (WitchCraft.ON_ANDROID)
			rk4 = new RK4Integrator(1f / 15f);
		else
			rk4 = new RK4Integrator(1f / 30f);
		rk4System = new ParticleSystem(rk4);

		contactListener = new MyContactListener();
		world = new World(new Vector2(0.0f, 0.0f), false);
		world.setContactListener(contactListener);

		FileHandle handle = Gdx.files.internal("data/levelstate.txt");
		String[] fileContent = handle.readString().split("\n");

		currentlevel = Integer.parseInt(fileContent[3]) - 1;

		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/level1/level"
				+ (currentlevel + 1) + ".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.loadMap("level" + (currentlevel + 1));
		tiledMapHelper.prepareCamera(WitchCraft.screenWidth,
				WitchCraft.screenHeight);

		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER, currentlevel + 1);

		debugRenderer = new Box2DDebugRenderer();
		// ground = new Ground(world);
		// ground.readCurveFile("data/groundcurves.txt", -240, -320);

		levels = new ArrayList<Float>();
		String[] levelstr = fileContent[1].split(":");
		for (String s : levelstr) {
			levels.add(Float.parseFloat(s));
		}

		triggers = new ArrayList<CinematicTrigger>();
		sleepCinematic = new CinematicSleeper();
		cinema = sleepCinematic;

		entities = new ArrayList<Entity>();
		player = new Player(rk4);
		npcs = new ArrayList<NonPlayer>();
		npcs.add(new NonPlayer("knight1", new Vector2(354.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/knights/fredknight",
				NPCType.KNIGHT));
		npcs.add(new NonPlayer("knight1", new Vector2(800.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/knights/joeknight",
				NPCType.KNIGHT));
		npcs.add(new NonPlayer("archer", new Vector2(3000.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/other/pimlyarcher",
				NPCType.ARCHER));
		npcs.add(new NonPlayer("archer", new Vector2(300.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/other/joearcher",
				NPCType.ARCHER));

		npcs.add(new NonPlayer("civmalebrown", new Vector2(4250.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/civs/billciv",
				NPCType.CIV));
		npcs.add(new NonPlayer("civfemaleblack-hood",
				new Vector2(5500.0f, 3.0f), new Vector2(0.6f, 0.7f),
				"data/npcdata/civs/saraciv", NPCType.CIV));
		shackled = new NPCStaticAnimation("shackledmale1", new Vector3(1696,
				118, 0), new Vector2(0.75f, 0.8f),
				"data/npcdata/static/shackledfred");
		tied = new NPCStaticAnimation("tiedwitch", new Vector3(3489, 350, 0),
				new Vector2(0.75f, 0.8f), "data/npcdata/static/tiedwitch");

		cloudE = new CloudEmitter(25);
		crowE = new CrowEmitter(1);

		// currentsound =
		// WitchCraft.assetManager.get("data/sounds/crickets.ogg");
		// currentsoundid = currentsound.loop(0.25f);

		level1();
	}

	public static void setCinematic(CinematicTrigger c) {
		cinema.transistionOut();
		player.setState(PlayerStateEnum.CINEMATIC);
		for (NonPlayer npc : npcs) {
			npc.setCinematic();
		}
		WitchCraft.cam.setState(CameraState.CINEMATIC);
		cinema = c;
	}

	public static void sleepCinematic() {
		WitchCraft.cam.state = CameraState.FOLLOWPLAYER;
		player.setParentState();
		for (NonPlayer npc : npcs) {
			npc.setParentState();
		}
		cinema = sleepCinematic;
	}

	public static void addEntity(Entity e) {
		entities.add(e);
	}

	@Override
	public void update(float dt) {
		world.step(dt, 1, 1);

		rk4.step();

		cinema.update(dt);

		player.update(dt);

		for (Entity e : entities)
			e.update(dt);

		for (NonPlayer npc : npcs)
			npc.update(dt);

		shackled.update(dt);
		tied.update(dt);

		cloudE.update(dt);
		crowE.update(dt);

		// if level is a new level number
		// if its negative its the same level
		// only run this when switching levels
		if (level > 0) {
			float diff = 0;
			if (level < currentlevel + 1) {
				diff = levels.get(level - 1) - WitchCraft.screenWidth * 0.5f;
				player.setX(diff - 64);
			} else {
				diff -= levels.get(currentlevel) - WitchCraft.screenWidth
						* 0.5f;
				player.setX(64);
			}
			cloudE.moveByX(diff);
			player.switchLevel();
			oldlevel = currentlevel;
			switchLevel(level);
			currentlevel = level - 1;
			level = -1;
			for (NonPlayer npc : npcs) {
				npc.checkInLevel();
			}
		}
		Vector3 playerPos = player.getPosPixels();
		Camera.updateState(playerPos, levels.get(currentlevel));
		xGrid = Camera.camera.position.x;
		float yGrid = Camera.camera.position.y;
		if (xGrid < WitchCraft.screenWidth * (WitchCraft.scale)) {
			xGrid = Camera.camera.position.x = WitchCraft.screenWidth
					* (WitchCraft.scale);
		}
		if (yGrid < WitchCraft.screenHeight * (WitchCraft.scale)) {
			Camera.camera.position.y = WitchCraft.screenHeight
					* (WitchCraft.scale);
		}

	}

	@Override
	public void draw(Batch batch) {
		if (!initworld) {
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			Color dayColor = getTimeOfDay();
			Gdx.gl.glClearColor(dayColor.r, dayColor.g, dayColor.b, 1f);

			tiledMapHelper.render(this);
			batch.begin();
			player.drawUI(batch);
			batch.end();
			if (Util.DEV_MODE)
				debugRenderer.render(world, Camera.camera.combined.scale(
						Util.PIXELS_PER_METER, Util.PIXELS_PER_METER,
						Util.PIXELS_PER_METER));
		}
	}

	public void drawBackGround(Batch batch) {
		// spriteBatch.setProjectionMatrix(Camera.camera.combined);
		// spriteBatch.begin();
		cloudE.draw(batch);
		// spriteBatch.end();
	}

	public void drawPlayerLayer(Batch batch) {
		shackled.draw(batch);
		tied.draw(batch);

		for (Entity e : entities) {
			e.draw(batch);
		}

		for (NonPlayer npc : npcs) {
			npc.draw(batch);
			if (npc.getPosPixels().y < 0) {
				System.out.println(npc.toString() + npc.npctype);
			}
		}
		// npc2.draw(batch);
		// npc3.draw(batch);
		// npc4.draw(batch);
		// npc5.draw(batch);

		player.draw(batch);

		crowE.draw(batch);

		rk4System.draw(batch);

		// spriteBatch.end();
		batch.end();
		player.drawCape(Camera.camera.combined);
		batch.begin();
		// player.drawCape(shapeRenderer);
	}

	public Color getTimeOfDay() {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		float val = Math.min(hour > 12 ? 1 - ((float) (hour - 12) / 8f)
				: ((float) (hour - 4) / 8f), 0.9f);
		hour = hour > 19 || hour < 5 ? 0 : hour;
		// System.out.println(hour+" : "+val);
		return new Color(val, val, val, 1);
	}

	public static void switchLevel(int l) {
		if (!world.isLocked()) {
			WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
					new InternalFileHandleResolver()));
			WitchCraft.assetManager.unload("data/world/level1/level"
					+ (oldlevel + 1) + ".tmx");
			WitchCraft.assetManager.load("data/world/level1/level" + (level)
					+ ".tmx", TiledMap.class);
			WitchCraft.assetManager.finishLoading();

			String levelstr = "level" + l;
			tiledMapHelper.destroyMap();
			tiledMapHelper.loadMap(levelstr);
			tiledMapHelper.loadCollisions("data/collisions.txt", world,
					Util.PIXELS_PER_METER, l);
			switch (l) {
			case 1:
				level1();
				break;
			case 2:
				level2();
				break;
			case 3:
				level3();
				break;
			case 4:
				level4();
				break;
			}
		} else {
			level = l;
		}
	}

	@Override
	public void transistionIn() {
		float accum = 0;
		oldlevel = currentlevel + 1;
		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/level1/level"
				+ (currentlevel + 1) + ".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		tiledMapHelper.destroyMap();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.loadMap("level" + (currentlevel + 1));
		tiledMapHelper.prepareCamera(WitchCraft.screenWidth,
				WitchCraft.screenHeight);

		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER, currentlevel + 1);

		player.setState(PlayerStateEnum.LOADINGSTATE);
		while (player.getState() != PlayerStateEnum.LOADINGSTATE) {
			update(WitchCraft.dt);
		}
	}

	@Override
	public void transistionOut() {

	}

	public static void level1() {
		triggers.clear();
	}

	public static void level2() {
		triggers.clear();
	}

	public static void level3() {
		triggers.clear();
		switch (storySegment) {
		case BEGINNING:
			triggers.add(new CinematicTrigger()
					.addAction(
							new Lerp(WitchCraft.cam, 0.0f, 0.005f, new Vector3(
									3150 + WitchCraft.screenWidth * 0.5f, 0, 0)))
					.addAction(new FaceLeft(npcs.get(4), 0.0f, true))
					.addAction(
							new AnimateTimed("WALKING", 6.3f, npcs.get(4), 3.0f))
					.addAction(
							new AnimateTimed("CLEANING", 10.0f, npcs.get(4),
									8.0f))
					.addAction(
							new ParticleToGamePlay(
									10.0f,
									new FireEmitter(
											new Vector3(
													2650 + WitchCraft.screenWidth * 0.5f,
													30, 0), 50, 3.0f, 60.0f,
											0.5f, 0.7f)))
					.addAction(new FaceLeft(npcs.get(4), 11.5f, false))
					.addAction(
							new AnimateTimed("WALKING", 14.7f, npcs.get(4),
									12.0f))
					.addAction(new FaceLeft(npcs.get(4), 14.9f, true))
					.addAction(new FaceLeft(npcs.get(4), 18.7f, false))
					.addAction(
							new AnimateTimed("WALKING", 24.0f, npcs.get(4),
									19.0f))
					.addAction(
							new Lerp(WitchCraft.cam, 22.0f, 0.003f,
									new Vector3(2800,
											0, 0))).buildBody(2850, 200, 8, 70));
			break;
		case POSTINTRODUCTION:
			break;
		}
	}

	public static void level4() {
		triggers.clear();
	}
}
