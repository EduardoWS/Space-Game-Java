package com.space.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
//import com.space.game.SpaceGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Space Game");
		config.setWindowIcon("assets/images/spaceships/spaceship.png");

		// Obter a resolução da tela
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        System.out.println("A resolução da tela é: " + screenWidth + "x" + screenHeight);


		// Calcular o tamanho da janela como uma porcentagem da resolução do monitor
		int windowWidth = (int) (screenWidth * 0.85f);
		int windowHeight = (int) (windowWidth * 9 / 16);


		// config.setWindowSizeLimits(windowWidth, windowHeight, windowWidth, windowHeight);
		config.setWindowedMode(windowWidth, windowHeight);
		// config.setMaximized(true);
		config.setResizable(false);


		new Lwjgl3Application(new SpaceGame(), config);
	}
}
