package com.space.game;


public class GameStateManager {
    
    public enum GameState {
        MENU, 
        PLAYING, 
        GAME_OVER
    }
    
    private GameState currentState;

    public GameStateManager() {
        this.currentState = GameState.MENU; // Estado inicial
    }

    public void setState(GameState newState) {
        this.currentState = newState;
    }

    public GameState getState() {
        return this.currentState;
    }
    
}
