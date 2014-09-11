package com.anythingmachine.witchcraft.GameStates.Containers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.cinematics.Camera;
import com.anythingmachine.cinematics.CameraState;
import com.anythingmachine.cinematics.CinematicSleeper;
import com.anythingmachine.cinematics.CinematicTrigger;
import com.anythingmachine.cinematics.actions.AnimateTimed;
import com.anythingmachine.cinematics.actions.FaceLeft;
import com.anythingmachine.cinematics.actions.Lerp;
import com.anythingmachine.cinematics.actions.ParticleToGamePlay;
import com.anythingmachine.cinematics.actions.StartParticle;
import com.anythingmachine.collisionEngine.Entity;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.collisionEngine.ground.Stairs;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.CrowEmitter;
import com.anythingmachine.physicsEngine.particleEngine.FireEmitter;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.anythingmachine.physicsEngine.particleEngine.particles.AnimatedSpringParticle;
import com.anythingmachine.physicsEngine.particleEngine.particles.Particle;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.Util.Util.EntityType;
import com.anythingmachine.witchcraft.agents.States.Player.PlayerStateEnum;
import com.anythingmachine.witchcraft.agents.npcs.NPCStaticAnimation;
import com.anythingmachine.witchcraft.agents.npcs.NPCType;
import com.anythingmachine.witchcraft.agents.npcs.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

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
	public static ShapeRenderer shapeRenderer;
	private static ArrayList<CinematicTrigger> triggers;
	private static CinematicTrigger cinema;
	private static CinematicTrigger sleepCinematic;
	private static StorySegment storySegment =
	StorySegment.BEGINNING;
	private Color dayColor;
	public static float windx;
	private float windtimeout = 1.5f;
	private Random rand;

	// private CloudEmitter cloudE;
	private CrowEmitter crowE;

	// test fields
	private Box2DDebugRenderer debugRenderer;
	// npcs run throughout the game
	private static ArrayList<NonPlayer> npcs;
	//foreground entities are cleared each level
	private static ArrayList<Entity> entities;
	//background entities are cleared each level
	private static ArrayList<Entity> bgentities;

	public enum StorySegment {
		BEGINNING, POSTINTRODUCTION,
	}

	public GamePlayManager() {
		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		if (WitchCraft.ON_ANDROID)
			rk4 = new RK4Integrator(WitchCraft.dt);
		else
			rk4 = new RK4Integrator(WitchCraft.dt);
		rk4System = new ParticleSystem(rk4);

		rand = new Random();

		shapeRenderer = new ShapeRenderer();

		contactListener = new MyContactListener();
		world = new World(new Vector2(0.0f, 0.0f), false);
		world.setContactListener(contactListener);

		FileHandle handle =
		Gdx.files.internal("data/levelstate.txt");
		String[] fileContent =
		handle.readString().split("\n");

		currentlevel = Integer.parseInt(fileContent[3]) - 1;
		// debug set level
		// if( Util.DEV_MODE )
		currentlevel = 3;
		level = currentlevel;

		WitchCraft.assetManager.setLoader(TiledMap.class,
			new TmxMapLoader(
			new InternalFileHandleResolver()));
		WitchCraft.assetManager.load(
			"data/world/level1/level"
			+ (currentlevel + 1)
			+ ".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper
		.loadMap("level" + (currentlevel + 1));
		tiledMapHelper
		.prepareCamera(WitchCraft.screenWidth,
			WitchCraft.screenHeight);

		tiledMapHelper.loadCollisions(
			"data/collisions.txt", world,
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
		bgentities = new ArrayList<Entity>();
		player = new Player(rk4, new Vector2(256f, 128f));
		npcs = new ArrayList<NonPlayer>();
		npcs.add(new NonPlayer("knight1", new Vector2(
		354.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/knights/fredknight", NPCType.KNIGHT));
		npcs.add(new NonPlayer("knight1", new Vector2(
		800.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/knights/joeknight", NPCType.KNIGHT));
		npcs.add(new NonPlayer("archer", new Vector2(
		3000.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/other/pimlyarcher", NPCType.ARCHER));
		npcs.add(new NonPlayer("archer", new Vector2(
		300.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/other/joearcher", NPCType.ARCHER));

		// cloudE = new CloudEmitter(25);
		crowE = new CrowEmitter(1);
		windx = 0;
		// currentsound =
		// WitchCraft.assetManager.get("data/sounds/crickets.ogg");
		// currentsoundid = currentsound.loop(0.25f);

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

	public static void addEntity(Entity e, int index) {
		entities.add(index, e);
	}

	@Override
	public void update(float dt) {
		updateEnvironment(dt);
		if (level > 0) {
			switchLevel(level);
		}

		world.step(WitchCraft.dt, 1, 1);

		rk4.step();

		cinema.update(dt);

		player.update(dt);

		for (Entity e : entities)
			e.update(dt);

		for (Entity e : bgentities) {
			e.update(dt);
		}

		for (NonPlayer npc : npcs)
			npc.update(dt);

		// cloudE.update(dt);
		crowE.update(dt);

		Vector3 playerPos = player.getPosPixels();
		Camera.updateState(playerPos,
			levels.get(currentlevel));
		xGrid = Camera.camera.position.x;
		float yGrid = Camera.camera.position.y;
		if (xGrid < WitchCraft.screenWidth
		* (WitchCraft.scale)) {
			xGrid =
			Camera.camera.position.x =
			WitchCraft.screenWidth * (WitchCraft.scale);
		}
		if (yGrid < WitchCraft.screenHeight
		* (WitchCraft.scale)) {
			Camera.camera.position.y =
			WitchCraft.screenHeight * (WitchCraft.scale);
		}

	}

	public void updateEnvironment(float dt) {
		dayColor = getTimeOfDay();
		windx = 0;
		if (windtimeout > 0) {
			windx = rand.nextInt(1500);
			if (windx < 1100) {
				windx = 0;
			}
		} else if (windtimeout < -1) {
			windtimeout = 1.5f;
		}
		windtimeout -= dt;
	}

	@Override
	public void draw(Batch batch) {
		if (!initworld) {
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(dayColor.r, dayColor.g,
				dayColor.b, 1f);

			tiledMapHelper.render(this);
			batch.begin();
			player.drawUI(batch);
			batch.end();
			if (Util.DEV_MODE)
				debugRenderer.render(world,
					Camera.camera.combined.scale(
						Util.PIXELS_PER_METER,
						Util.PIXELS_PER_METER,
						Util.PIXELS_PER_METER));
		}
	}

	public void drawBackGround(Batch batch) {
		// spriteBatch.setProjectionMatrix(Camera.camera.combined);
		// spriteBatch.begin();
		// cloudE.draw(batch);
		// spriteBatch.end();
		for (Entity e : bgentities) {
			e.draw(batch);
		}
	}

	public void drawPlayerLayer(Batch batch) {

		for (Entity e : entities) {
			e.draw(batch);
		}

		for (NonPlayer npc : npcs) {
			npc.draw(batch);
			if (npc.getPosPixels().y < 0) {
				System.out.println(npc.toString()
				+ npc.npctype);
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
		float val =
		Math.min(hour > 12
		? 1 - ((float) (hour - 12) / 8f)
		: ((float) (hour - 4) / 8f), 0.9f);
		hour = hour > 19 || hour < 5 ? 0 : hour;
		// System.out.println(hour+" : "+val);
		return new Color(val, val, val, 1);
	}

	public static void switchLevel(int l) {
		oldlevel = currentlevel;

		WitchCraft.assetManager.setLoader(TiledMap.class,
			new TmxMapLoader(
			new InternalFileHandleResolver()));
		WitchCraft.assetManager
		.unload("data/world/level1/level"
		+ (oldlevel + 1)
		+ ".tmx");
		WitchCraft.assetManager.load(
			"data/world/level1/level" + (l) + ".tmx",
			TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		String levelstr = "level" + l;
		tiledMapHelper.destroyMap();
		tiledMapHelper.loadMap(levelstr);
		tiledMapHelper.loadCollisions(
			"data/collisions.txt", world,
			Util.PIXELS_PER_METER, l);

		// if level is a new level number
		// if its negative its the same level
		// only run this when switching levels
		float diff = 0;
		if (l < currentlevel + 1) {
			diff = levels.get(l - 1) - 32;
		} else {
			// diff -= levels.get(currentlevel) - WitchCraft.screenWidth * 0.5f;
			diff = 64;
		}
		// cloudE.moveByX(diff);
		player.switchLevel(diff);
		for (NonPlayer npc : npcs) {
			npc.checkInLevel();
		}

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
		currentlevel = level - 1;
		level = -1;
	}

	@Override
	public void transistionIn() {

		oldlevel = currentlevel + 1;
		WitchCraft.assetManager.setLoader(TiledMap.class,
			new TmxMapLoader(
			new InternalFileHandleResolver()));
		WitchCraft.assetManager.load(
			"data/world/level1/level"
			+ (currentlevel + 1)
			+ ".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		tiledMapHelper.destroyMap();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper
		.loadMap("level" + (currentlevel + 1));
		tiledMapHelper
		.prepareCamera(WitchCraft.screenWidth,
			WitchCraft.screenHeight);

		tiledMapHelper.loadCollisions(
			"data/collisions.txt", world,
			Util.PIXELS_PER_METER, currentlevel + 1);

		player.setState(PlayerStateEnum.LOADINGSTATE);
		while (initworld) {
			update(WitchCraft.dt);
		}
	}

	@Override
	public void transistionOut() {

	}

	public static void level1() {
		for (CinematicTrigger ct : triggers) {
			ct.destroyBody();
		}
		for (Entity e : entities) {
			e.destroyBody();
		}
		for (Entity e : bgentities) {
			e.destroyBody();
		}
		triggers.clear();
		entities.clear();
		bgentities.clear();

		bgentities.add(new NPCStaticAnimation(
		"treedweller", new Vector3(1772, 80, 0),
		new Vector2(1.2f, 0.95f),
		"data/npcdata/static/treedweller1",
		"data/spine/characters"));
		bgentities.add(new NPCStaticAnimation(
		"treedweller", new Vector3(4900, 80, 0),
		new Vector2(1.2f, 0.95f),
		"data/npcdata/static/treedweller2",
		"data/spine/characters"));

		WitchCraft.stopMusic();
		WitchCraft.playMusic("data/sounds/frogs.ogg");
	}

	public static void level2() {
		for (CinematicTrigger ct : triggers) {
			ct.destroyBody();
		}
		for (Entity e : entities) {
			e.destroyBody();
		}
		for (Entity e : bgentities) {
			e.destroyBody();
		}
		triggers.clear();
		entities.clear();
		bgentities.clear();

		bgentities.add(new NPCStaticAnimation(
		"treedweller", new Vector3(2772, 80, 0),
		new Vector2(1.2f, 0.95f),
		"data/npcdata/static/treedweller1",
		"data/spine/characters"));
		bgentities.add(new NPCStaticAnimation(
		"treedweller", new Vector3(3699, 50, 0),
		new Vector2(1.2f, 0.95f),
		"data/npcdata/static/treedweller3",
		"data/spine/characters"));

		entities.add(new NPCStaticAnimation("ox",
		new Vector3(5696, 212, 0), new Vector2(1.2f, 1.2f),
		"data/npcdata/static/idle_ox_1",
		"data/spine/animals"));

		// WitchCraft.stopMusic();
		// WitchCraft.playMusic("data/sounds/ambient.ogg");
	}

	public static void level3() {
		for (CinematicTrigger ct : triggers) {
			ct.destroyBody();
		}
		for (Entity e : entities) {
			e.destroyBody();
		}
		for (Entity e : bgentities) {
			e.destroyBody();
		}
		triggers.clear();
		entities.clear();
		bgentities.clear();

		entities.add(new NPCStaticAnimation("tiedwitch",
		new Vector3(3489, 350, 0),
		new Vector2(0.75f, 0.8f),
		"data/npcdata/static/tiedwitch",
		"data/spine/characters"));

		entities.add(new NonPlayer("civ_male", new Vector2(
		4250.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/civs/billciv", NPCType.CIV));
		entities.add(new NonPlayer("civ_female",
		new Vector2(5500.0f, 100.0f), new Vector2(0.6f,
		0.7f), "data/npcdata/civs/saraciv", NPCType.CIV));

		entities.add(new NPCStaticAnimation(
		"shackledmale1", new Vector3(5824, 118, 0),
		new Vector2(0.75f, 0.8f),
		"data/npcdata/static/shackledfred",
		"data/spine/characters"));

		bgentities.add(new Particle(new Vector3(4604, 620,
		0)));
		bgentities.add(new AnimatedSpringParticle(
		"tiedwitch", new Vector3(4607, 524, 0),
		new Vector2(0.75f, 0.8f),
		"data/npcdata/static/hungwitch")
		.addRope((Particle) bgentities.get(0), 400, 300,
			7f)
		.setMass(20));
		bgentities.get(1).setStable(true);

		switch (storySegment) {
		case BEGINNING:
			// burn witch
			triggers
			.add(new CinematicTrigger()
			.addAction(
				new Lerp(WitchCraft.cam, 0.0f, 0.005f,
				new Vector3(
				3150 + WitchCraft.screenWidth * 0.5f, 0, 0)))
			.addAction(
				new FaceLeft(entities.get(1), 0.0f, true))
			.addAction(
				new AnimateTimed("TORCHMOBBING", entities
				.get(1), 3.0f, 6.3f, true))
			.addAction(
				new AnimateTimed("CLEANING", entities
				.get(1), 8.0f, 10.0f, true))
			.addAction(
				new ParticleToGamePlay(8.7f,
				new FireEmitter(new Vector3(
				2770 + WitchCraft.screenWidth * 0.5f, 170,
				0), 15, 0.7f, 4.1f, false, 1.4f, 2.1f, 10,
				Util.FIRESPEED), 1))
			.addAction(
				new ParticleToGamePlay(9.7f,
				new FireEmitter(new Vector3(
				2787 + WitchCraft.screenWidth * 0.5f, 170,
				0), 15, 2.2f, 4.0f, false, 3.6f, 2.6f, 10,
				Util.FIRESPEED), 1))
			.addAction(
				new ParticleToGamePlay(10.7f,
				new FireEmitter(new Vector3(
				2787 + WitchCraft.screenWidth * 0.5f, 257,
				0), 100, 3.7f, 60.0f, false, 6.4f, 1.3f,
				10, Util.FIRESPEED), 1))
			.addAction(
				new ParticleToGamePlay(10.9f,
				new FireEmitter(new Vector3(
				2714 + WitchCraft.screenWidth * 0.5f, 205,
				0), 20, 1.7f, 60.0f, false, 4.4f, 2.3f, 10,
				Util.FIRESPEED), 0))
			.addAction(
				new ParticleToGamePlay(10.9f,
				new FireEmitter(new Vector3(
				2857 + WitchCraft.screenWidth * 0.5f, 205,
				0), 20, 1.7f, 60.0f, false, 4.4f, 2.3f, 10,
				Util.FIRESPEED), 0))
			// .addAction(
			// new ParticleToGamePlay(
			// 13.7f,
			// new SmokeEmitter(
			// new Vector3(
			// 2770 + WitchCraft.screenWidth * 0.5f,
			// 500, 0), 20, 4.5f, 56.0f,
			// 5f, 3f), 1))
			.addAction(
				new FaceLeft(entities.get(5), 11.5f, false))
			.addAction(
				new AnimateTimed("WALKING",
				entities.get(5), 12.0f, 14.7f, true))
			.addAction(
				new FaceLeft(entities.get(5), 14.9f, true))
			.addAction(
				new FaceLeft(entities.get(5), 18.7f, false))
			.addAction(
				new AnimateTimed("WALKING",
				entities.get(5), 19.0f, 24.0f, true))
			.addAction(
				new Lerp(WitchCraft.cam, 22.0f, 0.01f,
				new Vector3(2500, 0, 0)))
			.buildBody(2850, 1100, 8, 1000));
			// hang witch
			triggers.add(new CinematicTrigger()
			.addAction(
				new StartParticle(0.0f, bgentities.get(1)))
			.addAction(
				new AnimateTimed("swing2", bgentities
				.get(1), 5.0f, 5.7f, true))
			.addAction(
				new AnimateTimed("blink",
				bgentities.get(1), 7.0f, 7.7f, false))
			.buildBody(4200, 1100, 8, 1000));
			break;
		case POSTINTRODUCTION:
			break;
		default:
			break;
		}
		storySegment = StorySegment.POSTINTRODUCTION;
		WitchCraft.stopMusic();
		WitchCraft.playMusic("data/sounds/ambient.ogg");

	}

	public static void level4() {
		for (CinematicTrigger ct : triggers) {
			ct.destroyBody();
		}
		for (Entity e : entities) {
			e.destroyBody();
		}
		for (Entity e : bgentities) {
			e.destroyBody();
		}
		triggers.clear();
		entities.clear();
		bgentities.clear();

		entities
		.add(new NonPlayer("archer", new Vector2(920.0f,
		500.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/other/tower_archer1", NPCType.ARCHER));
		entities.get(0).faceLeft(true);
		entities.add(new NonPlayer("civ_male", new Vector2(
		4250.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/civs/tempMale4", NPCType.CIV));
		entities
		.add(new NonPlayer("civ_female", new Vector2(
		5500.0f, 100.0f), new Vector2(0.6f, 0.7f),
		"data/npcdata/civs/tempFemale4", NPCType.CIV));
		entities.add(new NonPlayer("knight1", new Vector2(
		354.0f, 100.0f), new Vector2(0.8f, 0.9f),
		"data/npcdata/knights/fredknight", NPCType.KNIGHT));
		entities.add(new NonPlayer("knight1", new Vector2(
		800.0f, 100.0f), new Vector2(0.7f, 0.7f),
		"data/npcdata/knights/joeknight", NPCType.KNIGHT));
		entities.add(new NonPlayer("archer", new Vector2(
		3000.0f, 100.0f), new Vector2(0.6f, 0.6f),
		"data/npcdata/other/pimlyarcher", NPCType.ARCHER));
		entities.add(new NonPlayer("archer", new Vector2(
		300.0f, 100.0f), new Vector2(0.64f, 0.9f),
		"data/npcdata/other/joearcher", NPCType.ARCHER));
		entities.add(new NonPlayer("civ_male", new Vector2(
		4250.0f, 100.0f), new Vector2(0.77f, 0.6f),
		"data/npcdata/civs/tempMale4", NPCType.CIV));
		entities
		.add(new NonPlayer("civ_female", new Vector2(
		6500.0f, 100.0f), new Vector2(0.5f, 0.5f),
		"data/npcdata/civs/tempFemale4", NPCType.CIV));
		WitchCraft.stopMusic();
		// WitchCraft.playMusic("data/sounds/ambient.ogg");
		Array<Body> bodies = new Array<Body>();// [GamePlayManager.world.getBodyCount()];
		GamePlayManager.world.getBodies(bodies);
		for (Body b : bodies) {
			if (b.getUserData() instanceof Entity) {
				Entity e = (Entity) b.getUserData();
				if (e.type == EntityType.STAIRS) {
					float posx = e.getPos().x;
					float posy = e.getPos().y;
					if (posx > 1695
					&& posx < 1697
					&& posy > 511
					&& posy < 513) {
						Stairs stairs = (Stairs) e;
						stairs.holdDownToWalkDown();
					}
				}
			}
		}

	}

}
