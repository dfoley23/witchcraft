package com.anythingmachine.witchcraft.GameStates.Containers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.LuaEngine.LoadScript;
import com.anythingmachine.collisionEngine.MyContactListener;
import com.anythingmachine.physicsEngine.RK4Integrator;
import com.anythingmachine.physicsEngine.particleEngine.ParticleSystem;
import com.anythingmachine.tiledMaps.Camera;
import com.anythingmachine.tiledMaps.TiledMapHelper;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Screen;
import com.anythingmachine.witchcraft.ParticleEngine.CloudEmitter;
import com.anythingmachine.witchcraft.ParticleEngine.CrowEmitter;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NPCType;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
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
	private TiledMapHelper tiledMapHelper;
	private float xGrid;
	private Calendar cal;
	private MyContactListener contactListener;
	private LoadScript script;
	private String currentlevel;

	private CloudEmitter cloudE;
	private CrowEmitter crowE;

	// test fields
	private Box2DDebugRenderer debugRenderer;
	private NonPlayer npc1;
	private NonPlayer npc2;
	private NonPlayer npc3;
	private NonPlayer npc4;
	private NonPlayer npc5;

	public GamePlayManager() {
		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		if ( WitchCraft.ON_ANDROID )	
			rk4 = new RK4Integrator(1f /17f);			
		else
			rk4 = new RK4Integrator(1f /30f);
		rk4System = new ParticleSystem(rk4);

		contactListener = new MyContactListener();
		world = new World(new Vector2(0.0f, 0.0f), false);
		world.setContactListener(contactListener);

		currentlevel = "level1";
		WitchCraft.assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		WitchCraft.assetManager.load("data/world/"+currentlevel+"/"+currentlevel+".tmx", TiledMap.class);
		WitchCraft.assetManager.finishLoading();

		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.loadMap();
		tiledMapHelper.prepareCamera(WitchCraft.screenWidth,
				WitchCraft.screenHeight);

		debugRenderer = new Box2DDebugRenderer();
		// ground = new Ground(world);
		// ground.readCurveFile("data/groundcurves.txt", -240, -320);

		player = new Player(rk4);
		npc1 = new NonPlayer("knight2", "characters",
				new Vector2(354.0f, 3.0f), new Vector2(0.6f, 0.7f),
				"data/npcdata/knights/fredknight", NPCType.KNIGHT);
		npc2 = new NonPlayer("knight1", "characters",
				new Vector2(800.0f, 3.0f), new Vector2(0.6f, 0.7f),
				"data/npcdata/knights/fredknight", NPCType.KNIGHT);
		npc3 = new NonPlayer("archer", "characters", new Vector2(300.0f, 3.0f),
				new Vector2(0.6f, 0.7f), "data/npcdata/other/pimlyarcher",
				NPCType.ARCHER);
		npc4 = new NonPlayer("civmalebrown", "characters", new Vector2(300.0f,
				3.0f), new Vector2(0.6f, 0.7f), "data/npcdata/civs/billciv",
				NPCType.CIV);
		npc5 = new NonPlayer("civfemaleblack-hood", "characters", new Vector2(
				300.0f, 3.0f), new Vector2(0.6f, 0.7f),
				"data/npcdata/civs/saraciv", NPCType.CIV);

		cloudE = new CloudEmitter(17);
		crowE = new CrowEmitter(1);

		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER, 1);

	}

	@Override
	public void update(float dt) {
		world.step(dt, 1, 1);

		rk4.step();

		Gdx.gl.glViewport((int) WitchCraft.viewport.x, (int) WitchCraft.viewport.y,
				(int) WitchCraft.viewport.width, (int) WitchCraft.viewport.height);

		player.update(dt);
		npc1.update(dt);
		npc2.update(dt);
		npc3.update(dt);
		npc4.update(dt);
		npc5.update(dt);

		cloudE.update(dt);
		crowE.update(dt);
		
		Vector3 playerPos = player.getPosPixels();
		xGrid = Camera.camera.position.x = playerPos.x;
		float yGrid = Camera.camera.position.y = playerPos.y;
		if (xGrid < WitchCraft.screenWidth * (WitchCraft.scale)) {
			xGrid = Camera.camera.position.x = WitchCraft.screenWidth
					* (WitchCraft.scale);
		}
		if (yGrid < WitchCraft.screenHeight * (WitchCraft.scale)) {
			Camera.camera.position.y = WitchCraft.screenHeight
					* (WitchCraft.scale);
		}

		WitchCraft.cam.update();


	}

	@Override
	public void draw(Batch batch) {
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

	public void drawBackGround(Batch batch) {
		// spriteBatch.setProjectionMatrix(Camera.camera.combined);
		// spriteBatch.begin();
		cloudE.draw(batch);
		// spriteBatch.end();
	}

	public void drawPlayerLayer(Batch batch) {

		npc2.draw(batch);
		npc1.draw(batch);
		npc3.draw(batch);
		npc4.draw(batch);
		npc5.draw(batch);

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
		hour = hour > 19 || hour < 5 ? 0 : hour;
		float val = Math.min(hour > 12 ? 2 -((float) hour / 12f)
				: ((float) hour / 12f), 0.9f);
//		System.out.println(hour+" : "+val);
		return new Color(val, val, val, 1);
	}

}
