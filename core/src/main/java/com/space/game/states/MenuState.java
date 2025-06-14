package com.space.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space.game.managers.GameStateManager;
import com.space.game.managers.SoundManager;
import com.space.game.managers.UIManager;
import com.space.game.managers.ScoreManager;
import com.space.game.managers.GameStateManager.State;

public class MenuState implements GameStateInterface {
    private UIManager uiManager;
    private SoundManager soundManager;
    private GameStateManager gsm;
    private ScoreManager scoreManager;
    private boolean isPlaying;

    public MenuState(GameStateManager gsm, UIManager uiManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.soundManager = soundManager;
        this.gsm = gsm;
        this.scoreManager = new ScoreManager();
    }

    @Override
    public void enter() {
        soundManager.playMenuMusic();
        isPlaying = false;
    }

    @Override
    public void update(SpriteBatch batch) {
        if (isPlaying) {
            uiManager.displayGameControls();
        } else {
            uiManager.displayMenu(scoreManager.isDatabaseAvailable());
        }
        // Verificar entrada do usuário para iniciar o jogo
        handleInput();
    }

    @Override
    public State getState() {
        return State.MENU;
    }

    @Override
    public void exit() {
        // soundManager.stopMenuMusic();
    }

    private void handleInput() {
        if (!isPlaying){
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                isPlaying = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && scoreManager.isDatabaseAvailable()) {
                gsm.setState(State.GLOBAL_SCORES);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || 
                      (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && !scoreManager.isDatabaseAvailable())) {
                gsm.setState(State.LOCAL_SCORES);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                System.out.println("Key 0 pressed, exiting the game.");  // Depuração
                Gdx.app.exit();
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                soundManager.stopMenuMusic();
                soundManager.playMusic();
                gsm.setState(State.PLAYING);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                isPlaying = false;
            }
        }
    }
}

