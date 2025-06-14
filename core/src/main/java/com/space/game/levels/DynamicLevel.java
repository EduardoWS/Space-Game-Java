package com.space.game.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space.game.config.LevelConfig;
import com.space.game.entities.Spaceship;
import com.space.game.graphics.Background;
import com.space.game.graphics.TextureManager;
import com.space.game.managers.AlienManager;
import com.space.game.managers.BulletManager;
import com.space.game.managers.CollisionManager;
import com.space.game.managers.GameStateManager;
import com.space.game.managers.InputManager;
import com.space.game.managers.SoundManager;
import com.space.game.managers.UIManager;
import com.badlogic.gdx.Gdx;
import com.space.game.SpaceGame;

public class DynamicLevel implements Level {
    private Spaceship spaceship;
    private BulletManager bulletManager;
    private AlienManager alienManager;
    private CollisionManager collisionManager;
    private TextureManager textureManager;
    private SoundManager soundManager;
    private UIManager uiManager;
    private GameStateManager gsm;
    private LevelConfig config;
    private InputManager inputManager;
    private boolean endLevel;
    private boolean isSpaceshipNoMunition;
    private int lastKillCount; // variável para rastrear o último valor de kills em que a munição foi incrementada

    public DynamicLevel(LevelConfig config) {
        this.textureManager = SpaceGame.getGame().getTextureManager();
        this.uiManager = SpaceGame.getGame().getUiManager();
        this.gsm = SpaceGame.getGame().getGsm();
        this.config = config;
        this.soundManager = SpaceGame.getGame().getSoundManager();

        // background = new Background(textureManager, game);
        bulletManager = new BulletManager(textureManager, soundManager, gsm);
        spaceship = new Spaceship(textureManager, bulletManager);

        inputManager = new InputManager(gsm, spaceship);
        Gdx.input.setInputProcessor(inputManager);

        alienManager = new AlienManager(textureManager, spaceship, config);
        collisionManager = new CollisionManager(bulletManager, alienManager, spaceship, soundManager);

        alienManager.spawnAliens(spaceship);
        uiManager.setHordas(config.getLevelNumber());

        if (config.getLevelNumber() != 1) {
            spaceship.incrementAmmunitions(config.getAmmunitions());
        }else{
            spaceship.setAmmunitions(config.getAmmunitions());
        }
        spaceship.setStreakCount(config.getStreak());
        spaceship.setConsecutiveKills(config.getConsecutiveKills());
        spaceship.setKillCount(config.getKills());

        lastKillCount = -1;

        isSpaceshipNoMunition = false;

        endLevel = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        spaceship.render(batch);
        bulletManager.render(batch);
        alienManager.render(batch);
    }

    @Override
    public void update() {
        if (alienManager.getEndLevel() == true) {
            endLevel = true;
            return;
        }

        if(spaceship.getAmmunitions() == 0 && !isSpaceshipNoMunition){
            alienManager.setIsSpaceshipNoMunition(true);
            isSpaceshipNoMunition = true;
        }

        spaceship.update();
        bulletManager.update();
        alienManager.update(bulletManager.getBullets());
        collisionManager.checkBulletCollisions();

        if (collisionManager.checkSpaceshipCollisions()) {
            gsm.setState(GameStateManager.State.GAME_OVER);
        }

        alienManager.spawnAliens(spaceship);

        if (spaceship.getKillCount()>0 && (spaceship.getKillCount() % 7 == 0 && spaceship.getKillCount() != lastKillCount)) {
            spaceship.incrementAmmunitions(14);
            lastKillCount = spaceship.getKillCount();
        }

        inputManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        if(spaceship != null){
            // spaceship.dispose();
        }
        
        bulletManager.dispose();
        alienManager.dispose();
        collisionManager = null;
    }

    @Override
    public Spaceship getSpaceship() {
        return spaceship;
    }

    public AlienManager getAlienManager() {
        return alienManager;
    }

    public LevelConfig getConfig() {
        return config;
    }

    public boolean getEndLevel() {
        return endLevel;
    }

    public void freeSpaceship() {
        spaceship = null;
    }
}
