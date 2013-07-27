package com.anythingmachine.witchcraft;

import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.Player;
import com.anythingmachine.witchcraft.ground.Ground;
import com.anythingmachine.witchcraft.ground.Ground.GroundType;
import com.anythingmachine.witchcraft.tiledMaps.TiledMapHelper;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WitchCraft implements ApplicationListener {
	//the time the last frame was rendered, used for throttling framerate
	private long lastRender;
	private TiledMapHelper tiledMapHelper;
	private Texture overallTexture;
	private Player player;
	private Ground ground;
	private PolygonSpriteBatchWrap polygonBatch;
	private SpriteBatch spriteBatch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	
	/**
	 * The screen's width and height. This may not match that computed by
	 * libgdx's gdx.graphics.getWidth() / getHeight() on devices that make use
	 * of on-screen menu buttons.
	 */
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

		/**
		 * Load up the overall texture and chop it in to pieces. In this case,
		 * piece.
		 */
		//overallTexture = new Texture(Gdx.files.internal("data/tileSheet.png"));
		//overallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		spriteBatch = new SpriteBatch();
		polygonBatch = new PolygonSpriteBatchWrap();

		/**
		 * You can set the world's gravity in its constructor. Here, the gravity
		 * is negative in the y direction (as in, pulling things down).
		 */
		world = new World(new Vector2(0.0f, -10.0f), true);	

		ground = new Ground(world);
		for ( int i=0; i<Util.cps.length-7; i+=8) {
			ground.createCurve(
					new Vector2(Util.cps[i]-240, Util.cps[i+1]-250),
					new Vector2(Util.cps[i+2]-240, Util.cps[i+3]-250),
					new Vector2(Util.cps[i+4]-240, Util.cps[i+5]-250),
					new Vector2(Util.cps[i+6]-240, Util.cps[i+7]-250), 7, -1, GroundType.GRASS);
		}

		player = new Player( world, ground );	
		tiledMapHelper.loadCollisions("data/collisions.txt", world,
				Util.PIXELS_PER_METER);

		debugRenderer = new Box2DDebugRenderer();

		lastRender = System.nanoTime();
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		long now = System.nanoTime();
		float dT = Gdx.graphics.getDeltaTime();
		world.step(dT, 3, 3);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0.0f, 0.0f, 0);

		player.update(dT);

		tiledMapHelper.getCamera().position.x = Util.PIXELS_PER_METER
				* player.getPosMeters().x;

		if (tiledMapHelper.getCamera().position.x < Gdx.graphics.getWidth() / 2) {
			tiledMapHelper.getCamera().position.x = Gdx.graphics.getWidth() / 2;
		}
		if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
				- Gdx.graphics.getWidth() / 2) {
			tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
					- Gdx.graphics.getWidth() / 2;
		}

		if (tiledMapHelper.getCamera().position.y < Gdx.graphics.getHeight() / 2) {
			tiledMapHelper.getCamera().position.y = Gdx.graphics.getHeight();
		}
//		if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
//				- Gdx.graphics.getHeight() / 2) {
//			tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight();
//		}


		tiledMapHelper.getCamera().update();

		tiledMapHelper.render();
		
		polygonBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		polygonBatch.begin();
		
		ground.draw(polygonBatch);
		
		polygonBatch.end();

		
		spriteBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		spriteBatch.begin();

		player.draw(spriteBatch);

		spriteBatch.end();
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
				Util.PIXELS_PER_METER,
				Util.PIXELS_PER_METER,
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

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}
}
