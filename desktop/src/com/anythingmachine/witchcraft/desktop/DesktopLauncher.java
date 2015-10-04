package com.anythingmachine.witchcraft.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.anythingmachine.witchcraft.WitchCraft;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "witchcraft";
		cfg.width = 1366;
		cfg.height = 768;
		

		new LwjglApplication(new WitchCraft(), cfg);
	}
}
