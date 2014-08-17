package com.anythingmachine.soundEngine;

import java.util.HashMap;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private HashMap<String, Sound> sounds;
	private HashMap<String, Music> musics;
	private HashMap<String, Long> playing;
	private Sound currentSound;
	private String currentFile;
	private Music currentMusic;
	private String currentMusicFile;
	private float timeout;
	private float time;

	public SoundManager() {
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, Music>();
		playing = new HashMap<String, Long>();
		currentFile = "";
		timeout = 1.5f;
	}

	public void addSound(String filename) {
		sounds.put(filename, WitchCraft.assetManager.get(filename, Sound.class));
	}


	public void addMusic(String filename) {
		musics.put(filename, WitchCraft.assetManager.get(filename, Music.class));
	}
	
	public void update(float dt) {
		time += dt;
		if ( time > timeout ) {
			time = 0;
			currentFile = "";			
		}
	}

	public void playSound(String name) {
		if (currentFile.equals("")) {
			time = 0;
			currentFile = name;
			currentSound = sounds.get(name);
			long id = currentSound.play(1.0f); // play new sound and keep handle
												// for further manipulation
			// playing.put(name, id);
		}
	}

	public void stopSound(String name) {
		sounds.get(name).stop(playing.get(name)); // stops the sound instance
													// immediately
		playing.remove(name);
	}
	
	public void stop() {
		currentFile = "";
		time = timeout+timeout;
	}
	
	public void startMusic(String name) {
//		if (currentFile.equals("")) {
//			time = 0;
//			currentFile = name;
			currentMusic = musics.get(name);
			currentMusic.play(); // play new sound and keep handle
												// for further manipulation
			currentMusic.setLooping(true);
//			// playing.put(name, id);
//		}		
	}
	
	public void stopMusic() {
		if( currentMusic != null ) // stops the sound instance
			currentMusic.stop();
		// immediately
		currentMusicFile = "";		
	}
}
