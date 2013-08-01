package com.anythingmachine.witchcraft;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anythingmachine.gdxwrapper.PolygonSpriteBatchWrap;
import com.anythingmachine.witchcraft.Util.Util;
import com.anythingmachine.witchcraft.agents.NonPlayer;
import com.anythingmachine.witchcraft.agents.player.Player;
import com.anythingmachine.witchcraft.ground.Ground;
import com.anythingmachine.witchcraft.ground.Ground.GroundType;
import com.anythingmachine.witchcraft.tiledMaps.TiledMapHelper;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
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
	private NonPlayer npc1;
	private Ground ground;
	private PolygonSpriteBatchWrap polygonBatch;
	private SpriteBatch spriteBatch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private float xGrid;
	private int camWorldSize;
	private Calendar cal;
	private float dawnDuskProgress = 0;
	
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
		Date date = new Date();
		cal = GregorianCalendar.getInstance();
		cal.setTime(date);
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
					new Vector2(Util.cps[i+6]-240, Util.cps[i+7]-250), 7, -1, GroundType.DESERT);
		}

		player = new Player( world, ground );	
		npc1 = new NonPlayer( world, ground );
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
		Color c = getTimeOfDay();
		Gdx.gl.glClearColor(Math.min(c.getRed(), 0.1f), Math.min(c.getGreen(), 0.1f), Math.min(c.getBlue(), 0.5f), c.getAlpha());

		player.update(dT);
		npc1.update(dT);

		xGrid = tiledMapHelper.getCamera().position.x = Util.PIXELS_PER_METER
				* player.getPosMeters().x;
		float yGrid = tiledMapHelper.getCamera().position.y;
		
		if (xGrid < Gdx.graphics.getWidth() / 2) {
			xGrid = tiledMapHelper.getCamera().position.x = Gdx.graphics.getWidth() / 2;
		} else if (xGrid >= tiledMapHelper.getWidth()
				- Gdx.graphics.getWidth() / 2) {
			xGrid = tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
					- Gdx.graphics.getWidth() / 2;
		}

		if (yGrid < Gdx.graphics.getHeight() / 2) {
			tiledMapHelper.getCamera().position.y = Gdx.graphics.getHeight();
		}
//		if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
//				- Gdx.graphics.getHeight() / 2) {
//			tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight();
//		}

		camWorldSize = (int)(Gdx.graphics.getWidth() * tiledMapHelper.getCamera().zoom);

		tiledMapHelper.getCamera().update();
		
		tiledMapHelper.render(this);
		
		polygonBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		polygonBatch.begin();
		
		ground.draw(polygonBatch, 
				(int)(xGrid-(Gdx.graphics.getWidth()/2.f))/Util.curveLength, 
				camWorldSize / Util.curveLength );

		polygonBatch.end();
		
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

	public Color getTimeOfDay() {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if ( hour >= 20 || hour < 5) {
			return Color.DARK_GRAY;
		} else if ( hour >= 6 && hour < 19 ) {
			return Color.WHITE;
		} else {
			if( hour < 6 ){
				int min = cal.get(Calendar.MINUTE);				
				if( min == 0 ) {
					dawnDuskProgress = 0.25f;
				} else {
					dawnDuskProgress = ((float)min/60.f * .75f) + 0.25f;
				}
				return new Color(dawnDuskProgress,dawnDuskProgress,dawnDuskProgress);
			} else {
				int min = cal.get(Calendar.MINUTE);
				if( min == 0 ) {
					dawnDuskProgress = 1f;
				} else {
					dawnDuskProgress = ((1f-(float)min/60.f) * .75f) + 0.25f;
				}
				return new Color(dawnDuskProgress,dawnDuskProgress,dawnDuskProgress);
			}
		}
	}
	
	public void drawPlayerLayer() {	
		spriteBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		spriteBatch.begin();
		ground.drawGroundElems(spriteBatch, 
				(int)(xGrid-(Gdx.graphics.getWidth()/2.f))/Util.curveLength,
				camWorldSize / Util.curveLength );
		
		player.draw(spriteBatch);
		npc1.draw(spriteBatch);
		spriteBatch.end();
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
