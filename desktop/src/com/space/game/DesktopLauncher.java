package com.space.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.space.game.SpaceGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Space Game");
		config.setWindowIcon("assets/images/spaceships/spaceship.png");
		config.setWindowSizeLimits(1280, 720, 1280, 720);
		config.setWindowedMode(1280, 720);
		// deixar tela cheia
		// config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

		new Lwjgl3Application(new SpaceGame(), config);
	}
}
