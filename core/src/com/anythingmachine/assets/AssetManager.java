package com.anythingmachine.assets;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetManager {
	private HashMap<String, TextureAtlas> atlases;
	private HashMap<String, Texture> textures;
	
	public AssetManager() {
		atlases = new HashMap<String, TextureAtlas>();
		textures = new HashMap<String, Texture>();
	}
	
	public void addAtlas(String name, TextureAtlas atlas) {
		atlases.put(name, atlas);
	}
	
	public void addTexture(String name, Texture tex) {
		textures.put(name, tex);
	}
	
	public TextureAtlas getAtlas(String name) {
		return atlases.get(name);
	}
	
	public Texture getTexture(String name) {
		return textures.get(name);
	}
	
}
