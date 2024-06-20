package com.space.game.managers;

import com.space.game.states.GameStateInterface;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space.game.states.MenuState;
import com.space.game.states.PlayingState;
import com.space.game.states.GameOverState;
import com.space.game.states.PausedState;
import com.space.game.states.ScoresState;
import com.space.game.Game;

public class GameStateManager {
    public enum State {
        MENU, PLAYING, GAME_OVER, PAUSED, SCORES
    }

    private Map<State, GameStateInterface> states;
    private GameStateInterface currentState;

    public GameStateManager(Game game) {
        states = new HashMap<>();
        states.put(State.MENU, new MenuState(this, game.getUiManager(), game.getSoundManager()));
        states.put(State.PLAYING, new PlayingState(game, this, game.getUiManager(), game.getSoundManager()));
        states.put(State.GAME_OVER, new GameOverState(this, game.getMapManager(), game.getUiManager(), game.getSoundManager()));
        states.put(State.PAUSED, new PausedState(this, game.getMapManager(), game.getUiManager(), game.getSoundManager()));
        states.put(State.SCORES, new ScoresState(this, game.getUiManager()));

        setState(State.MENU);
    }

    public void setState(State newState) {
        if (currentState != null) {
            currentState.exit();
        }
        
        currentState = states.get(newState);
        if (currentState != null) {
            currentState.enter();
        }
    }

    public State getState() {
        return currentState.getState();
    }

    public void update(SpriteBatch batch) {
        if (currentState != null) {
            currentState.update(batch);
        }
    }

}
