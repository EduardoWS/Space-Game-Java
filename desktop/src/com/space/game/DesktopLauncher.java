package com.space.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
//import com.space.game.SpaceGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Space Game");
		config.setWindowIcon("assets/images/spaceships/spaceship.png");


		// Obter a resolução do monitor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
		System.out.println("A resolução da tela é: " + screenWidth + "x" + screenHeight);


		// Calcular o tamanho da janela como uma porcentagem da resolução do monitor
		int windowWidth = (int) (screenWidth * 0.8);
		int windowHeight = (int) (windowWidth * 9 / 16);


		config.setWindowSizeLimits(windowWidth, windowHeight, windowWidth, windowHeight);
		config.setWindowedMode(windowWidth, windowHeight);


		new Lwjgl3Application(new SpaceGame(), config);
	}
}
