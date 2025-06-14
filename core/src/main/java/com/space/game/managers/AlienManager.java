package com.space.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.space.game.SpaceGame;
import com.space.game.config.ConfigUtils;
import com.space.game.entities.Alien;
import com.space.game.entities.Bullet;
import com.space.game.entities.Spaceship;
import com.space.game.graphics.TextureManager;
import com.space.game.managers.GameStateManager.State;
import com.space.game.config.LevelConfig;

public class AlienManager {
    private float scale_screen = ConfigUtils.calcularFatorDeEscala();
    private List<Alien> aliens;
    private TextureManager textureManager;
    private float deltaTime;
    private Spaceship spaceship;
    private boolean endLevel;
    private int activeAlienCount;
    private int deadAliensCount;
    private boolean isSpaceshipNoMunition;

    private LevelConfig config;

    public AlienManager(TextureManager textureManager, Spaceship spaceship, LevelConfig config) {
        this.config = config;
        this.aliens = new ArrayList<>();
        this.textureManager = textureManager;
        this.deltaTime = Gdx.graphics.getDeltaTime();
        this.spaceship = spaceship;

        this.activeAlienCount = 0;
        this.deadAliensCount = 0;

        this.isSpaceshipNoMunition = false;

        this.endLevel = false;
    }

    public void addAlien(Vector2 position, float scale, int textureType, float speed, int movementPattern) {
        Alien newAlien = new Alien(textureManager, position, scale, textureType, speed, spaceship, movementPattern);
        aliens.add(newAlien);
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public boolean getEndLevel() {
        return endLevel;
    }

    public void setIsSpaceshipNoMunition(boolean isSpaceshipNoMunition) {
        this.isSpaceshipNoMunition = isSpaceshipNoMunition;
    }

    public void spawnAliens(Spaceship spaceship) {
        if (deadAliensCount >= config.getEnemyCount()) {
            this.endLevel = true;
            return;
        }

        activeAlienCount = 0;
        
        // Conta quantos aliens estão vivos
        for (Alien alien : this.getAliens()) {
            if (!alien.isDead()) {
                activeAlienCount++;
            }
            //
        }
        
        // Se houver menos de três aliens ativos, spawna mais
        if (activeAlienCount <= MathUtils.random(3, 7) && config.getEnemyMovementPatterns().size() > 0) {
            int contSpawn = 1;
            if (config.getEnemyMovementPatterns().size() > 7) {
                contSpawn = MathUtils.random(4, 7);
            } else {
                contSpawn = config.getEnemyMovementPatterns().size();
            }
            for (int i = 0; i < contSpawn; i++) {
                // Define posição inicial do alien baseada em 'i' para variar suas posições de spawn
                Vector2 alienPosition = calculateAlienSpawnPosition(i, spaceship.getPosition());
                // se config.enemySpeed for 100, então é para gerar numeros de 100 ate 130
                float speed = MathUtils.random(config.getEnemySpeed(), config.getEnemySpeed() + 5);
                float alienScale = 0.6f * scale_screen; 
                int textureType = 0; // Tipo de textura que pode ser variada para diferentes aliens
                
                this.addAlien(alienPosition, alienScale, textureType, speed, config.getEnemyMovementPatterns().get(0));
                config.getEnemyMovementPatterns().remove(0);
                // config.enemyCount--;
            }
        }
    }

    private Vector2 calculateAlienSpawnPosition(int index, Vector2 spaceshipPosition) {
        // fazer o modulo de index por 4 para que o valor de index seja sempre entre 0 e 3
        index = index % 4;

        float x = 0, y = 0;
        // Exemplo simples de spawn positions, pode ser ajustado conforme necessário
        switch (index % 4) {
            case 0: // Topo
                x = MathUtils.random(0, SpaceGame.getGame().getWorldWidth());
                y = SpaceGame.getGame().getWorldHeight() + SpaceGame.getGame().getWorldHeight()/20;
                break;
            case 1: // Direita
                x = SpaceGame.getGame().getWorldWidth() + SpaceGame.getGame().getWorldHeight()/20;
                y = MathUtils.random(0, SpaceGame.getGame().getWorldHeight());
                break;
            case 2: // Baixo
                x = MathUtils.random(0, SpaceGame.getGame().getWorldWidth());
                y = 0 - SpaceGame.getGame().getWorldHeight()/20;
                break;
            case 3: // Esquerda
                x = 0 - SpaceGame.getGame().getWorldHeight()/20;
                y = MathUtils.random(0, SpaceGame.getGame().getWorldHeight());
                break;
        }
        return new Vector2(x, y);
    }


    public void update(List<Bullet> bullets) {
        if (SpaceGame.getGame().getGsm().getState() != State.PLAYING) {
            return;
        }
        Iterator<Alien> alienIterator = aliens.iterator();
        while (alienIterator.hasNext()) {
            Alien alien = alienIterator.next();
            if(isSpaceshipNoMunition){
                alien.setMovementPattern(0);
                alien.setSpeed(SpaceGame.getGame().getWorldWidth()/11);
                setIsSpaceshipNoMunition(false);
            }
            alien.update(deltaTime, spaceship);

            // Remover o alien se ele atende aos critérios de remoção.
            if (alien.shouldRemove()) {
                deadAliensCount++;
                alienIterator.remove();
                alien.dispose();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Alien alien : aliens) {
            alien.render(batch);
        }
    }

    public void dispose() {
        for (Alien alien : aliens) {
            alien.dispose();
        }
        aliens.clear();
    }
}