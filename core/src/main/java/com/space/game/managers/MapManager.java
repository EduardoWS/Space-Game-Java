package com.space.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space.game.entities.Spaceship;
import com.space.game.levels.Level;
import com.space.game.levels.LevelFactory;
import com.space.game.Game;
import com.space.game.SpaceGame;

public class MapManager {
    //private Game game;
    private Level currentLevel;
    private LevelFactory levelFactory;
    private float waveTimer = 0;
    private final float TIME_TO_WAVE = 3; // Tempo em segundos antes da próxima onda
    private boolean waveActive;

    public MapManager(Game game) {
        this.levelFactory = new LevelFactory();
        // this.game = game;
    }

    public void loadLevel(int levelNumber) {
        if (currentLevel != null) {
            currentLevel.dispose();
        }
        currentLevel = levelFactory.createLevel(levelNumber);
        waveActive = false;

        if (currentLevel == null) {
            throw new IllegalArgumentException("Invalid level number: " + levelNumber);
        }
        
    }

    public void render(SpriteBatch batch) {
        if (currentLevel != null) {
            currentLevel.render(batch);
            if (!waveActive) {
                SpaceGame.getGame().getUiManager().displayNewLevel(waveTimer, TIME_TO_WAVE);
                // System.out.println("Wave Timer: " + waveTimer);               
            }
        }

    }

    public void update() {
        if (currentLevel != null && currentLevel.getEndLevel()) {
            loadLevel(currentLevel.getConfig().getLevelNumber() + 1);
        }
        if (currentLevel != null && waveActive) {
            currentLevel.update();
        } 
        else if (currentLevel != null && !waveActive) {
            // System.out.println("Wave Timer: " + waveTimer);
            waveTimer += Gdx.graphics.getDeltaTime();
            if (waveTimer >= TIME_TO_WAVE) {
                waveActive = true;
                waveTimer = 0;
            }
        }
    }

    public void dispose() {
        if (currentLevel != null) {
            currentLevel.dispose();
            currentLevel = null;
            levelFactory.dispose();
        }
    }

    public Spaceship getSpaceship() {
        return currentLevel != null ? currentLevel.getSpaceship() : null;
    }

    public void freeSpaceship() {
        if (currentLevel != null) {
            currentLevel.freeSpaceship();
        }
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    
}
