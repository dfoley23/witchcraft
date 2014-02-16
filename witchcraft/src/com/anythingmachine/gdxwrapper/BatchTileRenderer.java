package com.anythingmachine.gdxwrapper;

import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public abstract class BatchTileRenderer implements TileMapRenderer, Disposable {
	protected TiledMap map;

	protected float unitScale;

	protected Batch spriteBatch;

	protected Rectangle viewBounds;

	protected boolean ownsSpriteBatch;

	public TiledMap getMap () {
		return map;
	}

	public void setMap (TiledMap map) {
		this.map = map;
	}

	public float getUnitScale () {
		return unitScale;
	}

	public Batch getSpriteBatch () {
		return spriteBatch;
	}

	public Rectangle getViewBounds () {
		return viewBounds;
	}

	public BatchTileRenderer (TiledMap map) {
		this(map, 1.0f);
	}

	public BatchTileRenderer (TiledMap map, float unitScale) {
		this.map = map;
		this.unitScale = unitScale;
		this.viewBounds = new Rectangle();
		this.spriteBatch = new SpriteBatch();
		this.ownsSpriteBatch = true;
	}

	public BatchTileRenderer (TiledMap map, Batch batch) {
		this(map, 1.0f, batch);
	}

	public BatchTileRenderer (TiledMap map, float unitScale, Batch batch) {
		this.map = map;
		this.unitScale = unitScale;
		this.viewBounds = new Rectangle();
		this.spriteBatch = batch;
		this.ownsSpriteBatch = false;
	}

	@Override
	public void setView (OrthographicCamera camera) {
		spriteBatch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}

	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteBatch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}

	@Override
	public void render (GamePlayManager main) {
		boolean drewBG = false;
		beginRender();
		for (MapLayer layer : map.getLayers()) {
			if ( drewBG && layer.getProperties().containsKey("player") ) {
//				endRender();
//				Gdx.gl.glDisable(GL10.GL_BLEND);
				main.drawPlayerLayer(spriteBatch);
//				beginRender();
//				Gdx.gl.glEnable(GL10.GL_BLEND);
			}
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					//System.out.println("Render layer: " + layer.getName());
					renderTileLayer((TiledMapTileLayer)layer);
				} else {
					for (MapObject object : layer.getObjects()) {
						renderObject(object);
					}
				}
			}
			if ( !drewBG && layer.getProperties().containsKey("bg") ) {
				drewBG = true;
//				endRender();
//				Gdx.gl.glDisable(GL10.GL_BLEND);
				main.drawBackGround(spriteBatch);
//				beginRender();
//				Gdx.gl.glEnable(GL10.GL_BLEND);
			}
		}
		endRender();
	}

	@Override
	public void render (int[] layers) {
		beginRender();
		for (int layerIdx : layers) {
			MapLayer layer = map.getLayers().get(layerIdx);
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					renderTileLayer((TiledMapTileLayer)layer);
				} else {
					for (MapObject object : layer.getObjects()) {
						renderObject(object);
					}
				}
			}
		}
		endRender();
	}

	/** Called before the rendering of all layers starts. */
	protected void beginRender () {
		AnimatedTiledMapTile.updateAnimationBaseTime();
		spriteBatch.begin();
	}

	/** Called after the rendering of all layers ended. */
	protected void endRender () {
		spriteBatch.end();
	}

	@Override
	public void dispose () {
		if (ownsSpriteBatch) {
			spriteBatch.dispose();
		}
	}
}
