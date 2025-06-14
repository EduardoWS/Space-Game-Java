package com.space.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space.game.managers.GameStateManager;
import com.space.game.managers.GameStateManager.State;
import com.space.game.managers.MapManager;
import com.space.game.managers.SoundManager;
import com.space.game.managers.UIManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import com.space.game.managers.ScoreManager;
import com.badlogic.gdx.Input.Keys;
import java.util.HashMap;
import java.util.Map;

public class GameOverState implements GameStateInterface {

    private UIManager uiManager;
    private MapManager mapManager;
    private SoundManager soundManager;
    private GameStateManager gsm;
    private String playerName;  // Para armazenar o nome do jogador
    private boolean enterName = false;  
    private long lastBlinkTime;  // Variável para controlar o tempo de piscar
    private boolean showCursor = true;  // Variável para alternar a exibição do cursor
    private ScoreManager scoreManager;
    Map<Integer, String> keyToCharMap;
    private int whatHighScore;

    private float gameoverTimer = 0;
    private final float TIME_TO_GAMEOVER = 7; // Tempo em segundos antes da próxima onda

    public GameOverState(GameStateManager gsm, MapManager mapManager, UIManager uiManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.mapManager = mapManager;
        this.soundManager = soundManager;
        this.gsm = gsm;
        this.scoreManager = new ScoreManager();

        // Mapear teclas para caracteres correspondentes
        keyToCharMap = new HashMap<>();

        keyToCharMap.put(Keys.A, "A");
        keyToCharMap.put(Keys.B, "B");
        keyToCharMap.put(Keys.C, "C");
        keyToCharMap.put(Keys.D, "D");
        keyToCharMap.put(Keys.E, "E");
        keyToCharMap.put(Keys.F, "F");
        keyToCharMap.put(Keys.G, "G");
        keyToCharMap.put(Keys.H, "H");
        keyToCharMap.put(Keys.I, "I");
        keyToCharMap.put(Keys.J, "J");
        keyToCharMap.put(Keys.K, "K");
        keyToCharMap.put(Keys.L, "L");
        keyToCharMap.put(Keys.M, "M");
        keyToCharMap.put(Keys.N, "N");
        keyToCharMap.put(Keys.O, "O");
        keyToCharMap.put(Keys.P, "P");
        keyToCharMap.put(Keys.Q, "Q");
        keyToCharMap.put(Keys.R, "R");
        keyToCharMap.put(Keys.S, "S");
        keyToCharMap.put(Keys.T, "T");
        keyToCharMap.put(Keys.U, "U");
        keyToCharMap.put(Keys.V, "V");
        keyToCharMap.put(Keys.W, "W");
        keyToCharMap.put(Keys.X, "X");
        keyToCharMap.put(Keys.Y, "Y");
        keyToCharMap.put(Keys.Z, "Z");
    }

    @Override
    public void enter() {
        enterName = false;
        playerName = "";
        lastBlinkTime = TimeUtils.millis();  // Inicializa o tempo de piscar
        soundManager.stopMusic();
        soundManager.playGameOverMusic();
        whatHighScore = 0;
        gameoverTimer = 0;
    }

    @Override
    public void update(SpriteBatch batch) {
        if (enterName) {
            setupUI();
        } else {
            uiManager.displayGameOverInfo(mapManager.getSpaceship(), gameoverTimer, TIME_TO_GAMEOVER);
            gameoverTimer += Gdx.graphics.getDeltaTime();
            if (gameoverTimer >= TIME_TO_GAMEOVER) {
                gameoverTimer = TIME_TO_GAMEOVER;
            }
            handleInput();
        }
    }

    @Override
    public State getState() {
        return State.GAME_OVER;
    }

    @Override
    public void exit() {
        mapManager.freeSpaceship();
        mapManager.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // verificar se ja tem 10 scores salvos e se o score atual é menor que o ultimo salvo
            // whatHighScore = 0 -> não é highscore
            // whatHighScore = 1 -> é highscore local
            // whatHighScore = 2 -> é highscore global
            // whatHighScore = 3 -> é highscore local e global
            
            boolean isLocalHigh = scoreManager.isLocalHighScore(mapManager.getSpaceship().getKillCount());
            boolean isGlobalHigh = scoreManager.isDatabaseAvailable() && 
                                  scoreManager.isHighScore(mapManager.getSpaceship().getKillCount());
            
            if (isLocalHigh && isGlobalHigh) {
                whatHighScore = 3;
                setupUI();
            } else if (isGlobalHigh) {
                whatHighScore = 2;
                setupUI();
            } else if (isLocalHigh) {
                whatHighScore = 1;
                setupUI();
            } else {
                whatHighScore = 0;
                soundManager.stopGameOverMusic();
                gsm.setState(State.MENU);
            }
            
        }
    }

    private void setupUI() {
        // Alterna a exibição do cursor a cada 500 milissegundos
        if (TimeUtils.timeSinceMillis(lastBlinkTime) > 500) {
            showCursor = !showCursor;
            lastBlinkTime = TimeUtils.millis();
        }

        uiManager.displaySaveScore(mapManager.getSpaceship(), playerName, showCursor);

        // Verificar teclas pressionadas e atualizar o nome do jogador
        for (Map.Entry<Integer, String> entry : keyToCharMap.entrySet()) {
            if (Gdx.input.isKeyJustPressed(entry.getKey()) && playerName.length() < 10) {
                playerName += entry.getValue();
            }
        }

        // Verificar tecla BACKSPACE
        if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE) && playerName.length() > 0) {
            playerName = playerName.substring(0, playerName.length() - 1);
        }

        // Verificar tecla ENTER para salvar o score
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) && enterName) {
            if (playerName.isEmpty()) {
                System.out.println("Player name is empty, setting to UNKNOWN");
                playerName = "UNKNOWN";
            }
            saveScore(playerName, mapManager.getSpaceship().getKillCount());
            soundManager.stopGameOverMusic();
            gsm.setState(State.MENU);
            System.out.println("Score saved");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            soundManager.stopGameOverMusic();
            gsm.setState(State.MENU);
        }

        if (!enterName) {
            enterName = true;
        }
    }

    private void saveScore(String playerName, int score) {
        if (whatHighScore == 1) {
            scoreManager.saveLocalScore(playerName, score);
        } else if (whatHighScore == 2) {
            scoreManager.saveGlobalScore(playerName, score);
        } else if (whatHighScore == 3) {
            scoreManager.saveLocalScore(playerName, score);
            scoreManager.saveGlobalScore(playerName, score);
        } else {
            System.out.println("Score not saved");
        }
    }
}
