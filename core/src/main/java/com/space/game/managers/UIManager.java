package com.space.game.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.space.game.config.ConfigUtils;
import com.space.game.entities.Spaceship;
import com.badlogic.gdx.Gdx;
import java.util.List;
import com.space.game.Game;

public class UIManager {
    private BitmapFont font30, font100, font150;
    private Game game;
    private SpriteBatch batch;
    private int hordas;
    private final int const_larg = 21;
    private Color cian_color;

    public UIManager(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.cian_color = new Color(0.0f, 1.0f, 1.0f, 1.0f);

        initializeFonts();

        
    }

    private void initializeFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nasalization-rg.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        float scaleFactor = ConfigUtils.calcularFatorDeEscala();

        parameter.size = (int) (30 * scaleFactor);
        font30 = generator.generateFont(parameter);

        parameter.size = (int) (100 * scaleFactor);
        parameter.borderWidth = 4 * scaleFactor;
        parameter.borderColor = cian_color;
        parameter.color = Color.BLACK;
        font100 = generator.generateFont(parameter);

        parameter.size = (int) (150 * scaleFactor);
        parameter.borderWidth = 4 * scaleFactor;
        font150 = generator.generateFont(parameter);

        generator.dispose();
    }

    public void displayMenu(boolean isDatabaseAvailable) {
        // Desenha o título "SPACE GAME"
        String title = "SPACE GAME";
        GlyphLayout titleLayout = new GlyphLayout(font150, title);
        float title_x = game.getWorldWidth() / const_larg;
        float title_y = game.getWorldHeight()/1.5f + titleLayout.height;
        font150.draw(batch, title, title_x,  title_y);

        // Desenha o botão "New Game"
        String buttonText = "1. Start Arcade Mode";
        GlyphLayout buttonLayout = new GlyphLayout(font30, buttonText);
        float buttonX = game.getWorldWidth() / const_larg;
        float buttonY = title_y - titleLayout.height*3;
        font30.setColor(cian_color);
        font30.draw(batch, buttonText, buttonX, buttonY);

        // Condicional para mostrar ou esconder Global Scores
        if (isDatabaseAvailable) {
            // Desenha o botão "Global Scores"
            buttonText = "2. Global Scores";
            buttonLayout = new GlyphLayout(font30, buttonText);
            buttonX = game.getWorldWidth() / const_larg;
            buttonY = buttonY - buttonLayout.height*3;
            font30.draw(batch, buttonText, buttonX, buttonY);

            // Desenha o botão "Local Scores"
            buttonText = "3. Local Scores";
            buttonLayout = new GlyphLayout(font30, buttonText);
            buttonX = game.getWorldWidth() / const_larg;
            buttonY = buttonY - buttonLayout.height*3;
            font30.draw(batch, buttonText, buttonX, buttonY);
        } else {
            // Quando não há conexão, Local Scores fica como opção 2
            buttonText = "2. Local Scores";
            buttonLayout = new GlyphLayout(font30, buttonText);
            buttonX = game.getWorldWidth() / const_larg;
            buttonY = buttonY - buttonLayout.height*3;
            font30.draw(batch, buttonText, buttonX, buttonY);
        }

        // Desenha o botão "Exit"
        buttonText = "0. Exit";
        buttonLayout = new GlyphLayout(font30, buttonText);
        buttonX = game.getWorldWidth() / const_larg;
        buttonY = buttonY - buttonLayout.height*12;
        font30.draw(batch, buttonText, buttonX, buttonY);
    }

    public void displayGameControls() {
        float scaleFactor = ConfigUtils.calcularFatorDeEscala();
    
        String title = "GAME CONTROLS";
        GlyphLayout titleLayout = new GlyphLayout(font100, title);
        float title_x = game.getWorldWidth() / const_larg;
        float title_y = game.getWorldHeight() / 1.2f + titleLayout.height * scaleFactor;
        font100.draw(batch, title, title_x, title_y);
    
        font30.setColor(cian_color);
        float startY = game.getWorldHeight() / 2 + 3 * 30 * scaleFactor; // 3 é o número de controles
    
        // Desenhar cabeçalhos da tabela
        String actionHeader = "Action";
        String controlHeader = "Control";
    
        GlyphLayout controlLayout = new GlyphLayout(font30, controlHeader);
        GlyphLayout actionLayout = new GlyphLayout(font30, actionHeader);
    
        float actionX = game.getWorldWidth() / const_larg; // Espaçamento entre colunas
        float controlX = actionX + actionLayout.width + 100 * scaleFactor;
    
        float headerY = startY + 60 * scaleFactor; // Cabeçalhos um pouco acima da lista de controles
        font30.draw(batch, controlHeader, controlX + controlLayout.width/2, headerY);
        font30.draw(batch, actionHeader, actionX, headerY);
    
        // Controles do jogo
        String[] actions = { "Turn Left", "Turn Right", "Shoot", "Pause Game", "Prev. Song", "Pause Song", "Next Song"};
        String[] controls = { "A | Left Arrow", "D | Right Arrow", "Spacebar", "P", "Q", "W", "E"};
    
        // Desenhar controles
        float y = startY;
    
        for (int i = 0; i < controls.length; i++) {
            String control = controls[i];
            String action = actions[i];
            
            GlyphLayout controlTextLayout = new GlyphLayout(font30, control);
            GlyphLayout actionTextLayout = new GlyphLayout(font30, action);
            
            
            font30.draw(batch, control, controlX+ controlLayout.width/2, y);
            font30.draw(batch, action, actionX, y);
    
            y -= 50 * scaleFactor;
        }
    
        // Desenha as instruções de iniciar e voltar na parte inferior da tela
        String startText = "Enter. Start";
        GlyphLayout startLayout = new GlyphLayout(font30, startText);
        // float start_x = game.getWorldWidth() / 2 + game.getWorldWidth() / 4 - startLayout.width / 2;
        float start_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - startLayout.width ;
        float start_y = game.getWorldHeight() * 0.1f; // Posição inferior
        font30.draw(batch, startText, start_x, start_y);

        String backText = "Backspace. Back";
        GlyphLayout backLayout = new GlyphLayout(font30, backText);
        // float back_x = game.getWorldWidth() / 2 - game.getWorldWidth() / 4 - backLayout.width / 2;
        float back_x = game.getWorldWidth() / const_larg;
        float back_y = start_y;
        font30.draw(batch, backText, back_x, back_y);
    }
    

    public void displayGameInfo(Spaceship spaceship) {
        // Exibir informações do jogo como munição e hordas
        // Colocar cor branca
        font30.setColor(cian_color);
        String ammoText = "AMMO: " + spaceship.getAmmunitions();
        GlyphLayout ammoLayout = new GlyphLayout(font30, ammoText);
        float ammo_x = game.getWorldWidth() / const_larg;
        float ammo_y = ammoLayout.height/2 + ammoLayout.height; // Posição inferior
        font30.draw(batch, ammoText, ammo_x, ammo_y);

        String hordasText = "WAVE: " + hordas;
        GlyphLayout hordasLayout = new GlyphLayout(font30, hordasText);
        float hordas_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - hordasLayout.width;
        float hordas_y = hordasLayout.height/2 + hordasLayout.height; // Posição inferior
        font30.draw(batch, hordasText, hordas_x, hordas_y);

        // Posição do X igual, mas Y no topo
        String killsText = "SCORE: " + (spaceship.getKillCount());
        GlyphLayout killsLayout = new GlyphLayout(font30, killsText);
        float kills_x = game.getWorldWidth() / const_larg;
        float kills_y = game.getWorldHeight() - killsLayout.height;
        font30.draw(batch, killsText, kills_x, kills_y); 

        String streakText = "STREAK: x" + spaceship.getStreakCount();
        GlyphLayout streakLayout = new GlyphLayout(font30, streakText);
        float streak_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - streakLayout.width;
        float streak_y = game.getWorldHeight() - streakLayout.height;
        font30.draw(batch, streakText, streak_x, streak_y);

    }

    public void displayError(String error) {
        GlyphLayout errorLayout = new GlyphLayout(font30, error);
        float error_x = game.getWorldWidth() / 2 - errorLayout.width / 2;
        float error_y = game.getWorldHeight() / 2 + errorLayout.height;
        font30.draw(batch, error, error_x, error_y);

        String backText = "Backspace. Back";
        GlyphLayout backLayout = new GlyphLayout(font30, backText);
        // float back_x = game.getWorldWidth() / 2 - game.getWorldWidth() / 4 - backLayout.width / 2;
        float back_x = game.getWorldWidth() / 2 - backLayout.width / 2;
        float back_y = game.getWorldHeight() * 0.1f;
        font30.draw(batch, backText, back_x, back_y);
    }

    public void displayGameOverInfo(Spaceship spaceship, float gameoverTimer, float TIME_TO_GAMEOVER) {
        // Calcular a porcentagem do tempo decorrido
        float progress = gameoverTimer / TIME_TO_GAMEOVER;
        float alpha;

        alpha = progress;


        String gameOverText = "GAME OVER";
        GlyphLayout gameOverLayout = new GlyphLayout(font100, gameOverText);
        float gameOver_x = game.getWorldWidth() / 2 - gameOverLayout.width / 2;
        float gameOver_y = game.getWorldHeight() / 2 + gameOverLayout.height;
        font100.setColor(0, 1, 1, alpha);
        font100.draw(batch, gameOverText, gameOver_x, gameOver_y);
        font100.setColor(0, 1, 1, 1); // Restaurar a cor padrão

        String restartText = "Press Enter to Continue";
        GlyphLayout restartLayout = new GlyphLayout(font30, restartText);
        font30.setColor(0, 1, 1, alpha);
        font30.draw(batch, restartText, game.getWorldWidth() / 2 - restartLayout.width / 2, gameOver_y - gameOverLayout.height * 2);
        font30.setColor(0, 1, 1, 1); // Restaurar a cor padrão

    }

    public void displayPausedInfo(Spaceship spaceship) {
        String pausedText = "PAUSED";
        GlyphLayout pausedLayout = new GlyphLayout(font100, pausedText);
        font100.draw(batch, pausedText, game.getWorldWidth() / 2 - pausedLayout.width / 2, game.getWorldHeight() / 1.3f + pausedLayout.height);

        font30.setColor(cian_color);
        String restartText = "Backspace. Exit   |   Enter. Resume";
        GlyphLayout restartLayout = new GlyphLayout(font30, restartText);
        font30.draw(batch, restartText, game.getWorldWidth() / 2 - restartLayout.width / 2, game.getWorldHeight() / 1.3f - restartLayout.height * 3);

        // Exibir informações do jogo como munição e hordas
        String ammoText = "AMMO: " + spaceship.getAmmunitions();
        GlyphLayout ammoLayout = new GlyphLayout(font30, ammoText);
        float ammo_x = game.getWorldWidth() / const_larg;
        float ammo_y = ammoLayout.height/2 + ammoLayout.height; // Posição inferior
        font30.draw(batch, ammoText, ammo_x, ammo_y);

        String hordasText = "WAVE: " + hordas;
        GlyphLayout hordasLayout = new GlyphLayout(font30, hordasText);
        float hordas_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - hordasLayout.width;
        float hordas_y = hordasLayout.height/2 + hordasLayout.height; // Posição inferior
        font30.draw(batch, hordasText, hordas_x, hordas_y);

        // Posição do X igual, mas Y no topo
        String killsText = "SCORE: " + (spaceship.getKillCount());
        GlyphLayout killsLayout = new GlyphLayout(font30, killsText);
        float kills_x = game.getWorldWidth() / const_larg;
        float kills_y = game.getWorldHeight() - killsLayout.height;
        font30.draw(batch, killsText, kills_x, kills_y); 

        String streakText = "STREAK: x" + spaceship.getStreakCount();
        GlyphLayout streakLayout = new GlyphLayout(font30, streakText);
        float streak_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - streakLayout.width;
        float streak_y = game.getWorldHeight() - streakLayout.height;
        font30.draw(batch, streakText, streak_x, streak_y);

    }

    public void displayNewLevel(float waveTimer, float TIME_TO_WAVE) {
        // Calcular a porcentagem do tempo decorrido
        float progress = waveTimer / TIME_TO_WAVE;
        float alpha;
        
        // Se a progressão está na primeira metade
        if (progress <= 0.5f) {
            // Interpolação linear de 0 a 1
            alpha = progress * 2;
        } else {
            // Interpolação linear de 1 a 0
            alpha = 1 - ((progress - 0.5f) * 2);
        }


    
        // Definir a posição do texto
        String newLevelText = "WAVE " + hordas;
        GlyphLayout newLevelLayout = new GlyphLayout(font100, newLevelText);
        float newLevel_x = game.getWorldWidth() / 2 - newLevelLayout.width / 2;
        float newLevel_y = game.getWorldHeight() / 1.3f + newLevelLayout.height;
    
        // Desenhar o texto com a opacidade atualizada
        font100.setColor(1, 1, 1, alpha);
        font100.draw(batch, newLevelText, newLevel_x, newLevel_y);
        font100.setColor(1, 1, 1, 1); // Restaurar a cor padrão
    
        
    }

    public void displaySaveScore(Spaceship spaceship, String playerName, boolean showCursor) {
        String highscore = "HIGH SCORE: " + (spaceship.getKillCount());
        GlyphLayout highscoreLayout = new GlyphLayout(font100, highscore);
        float highscore_x = game.getWorldWidth() / 2 - highscoreLayout.width / 2;
        float highscore_y = game.getWorldHeight() / 1.3f + highscoreLayout.height;
        font100.draw(batch, highscore, highscore_x, highscore_y);

        // String scoreText = "Score: " + (spaceship.getKillCount());
        // GlyphLayout scoreLayout = new GlyphLayout(font100, scoreText);
        // font100.draw(batch, scoreText, game.getWorldWidth() / 2 - scoreLayout.width / 2, highscore_y - highscoreLayout.height * 2);
    
        font30.setColor(cian_color);
        String playerText = "Player: " + playerName + (showCursor ? "_" : "  ");
        GlyphLayout playerLayout = new GlyphLayout(font30, playerText);
        float player_x = game.getWorldWidth() / 2 - playerLayout.width / 2;
        float player_y = game.getWorldHeight() / 2;
        font30.draw(batch, playerText, player_x, player_y);
    
        String continueText = "Enter. Save";
        GlyphLayout continueLayout = new GlyphLayout(font30, continueText);
        float continue_x = (const_larg-1)*(game.getWorldWidth() / const_larg) - continueLayout.width;
        float continue_y = continueLayout.height/2 + continueLayout.height;
        font30.draw(batch, continueText, continue_x, continue_y);

        String cancelText = "0. Cancel";
        GlyphLayout cancelLayout = new GlyphLayout(font30, cancelText);
        float cancel_x = game.getWorldWidth() / const_larg;
        float cancel_y = cancelLayout.height/2 + cancelLayout.height;
        font30.draw(batch, cancelText, cancel_x, cancel_y);
    }


    public void displayScores(List<ScoreManager.ScoreEntry> scoresList, boolean isGlobal) {
        float scaleFactor = ConfigUtils.calcularFatorDeEscala();
        String title;
        if (isGlobal) {
            title = "GLOBAL HIGH SCORES";
        } else {
            title = "LOCAL HIGH SCORES";
        }
        GlyphLayout titleLayout = new GlyphLayout(font100, title);
        float title_x = game.getWorldWidth() / const_larg;
        float title_y = game.getWorldHeight() / 1.2f + titleLayout.height * scaleFactor;
        font100.draw(batch, title, title_x, title_y);
        
        font30.setColor(cian_color);
        float startY = game.getWorldHeight() / 2 + (scoresList.size() / 2) * 30 * scaleFactor;
        
        // Desenhar cabeçalhos da tabela
        String rankHeader = "Rank";
        String playerHeader = "Player";
        String scoreHeader = "Score";
        
        GlyphLayout rankLayout = new GlyphLayout(font30, rankHeader);
        GlyphLayout playerLayout = new GlyphLayout(font30, playerHeader);
        GlyphLayout scoreLayout = new GlyphLayout(font30, scoreHeader);
        
        float rankX = game.getWorldWidth() / const_larg;
        float playerX = rankX + rankLayout.width + 20 * scaleFactor;  // Espaçamento entre colunas
        float scoreX = playerX + playerLayout.width + 200 * scaleFactor;
        
        float headerY = startY + 60 * scaleFactor;  // Cabeçalhos um pouco acima da lista de scores
        font30.draw(batch, rankHeader, rankX, headerY);
        font30.draw(batch, playerHeader, playerX, headerY);
        font30.draw(batch, scoreHeader, scoreX, headerY);
        
        // Determinar a largura máxima da coluna Rank
        float maxRankWidth = rankLayout.width * scaleFactor;
        for (int i = 0; i < scoresList.size(); i++) {
            String rank = (i + 1) + ".";
            GlyphLayout rankTextLayout = new GlyphLayout(font30, rank);
            if (rankTextLayout.width * scaleFactor > maxRankWidth) {
                maxRankWidth = rankTextLayout.width * scaleFactor;
            }
        }
        
        // Desenhar scores
        float y = startY;
        
        for (int i = 0; i < scoresList.size(); i++) {
            ScoreManager.ScoreEntry entry = scoresList.get(i);
            String rank = (i + 1) + ".";
            String player = entry.playerName;
            String score = String.valueOf(entry.score);
        
            GlyphLayout rankTextLayout = new GlyphLayout(font30, rank);
            GlyphLayout playerTextLayout = new GlyphLayout(font30, player);
            GlyphLayout scoreTextLayout = new GlyphLayout(font30, score);
        
            float rankXAdjusted = rankX + maxRankWidth - rankTextLayout.width * scaleFactor;
        
            font30.draw(batch, rank, rankXAdjusted, y);
            font30.draw(batch, player, playerX, y);
            font30.draw(batch, score, scoreX, y);
        
            y -= 50 * scaleFactor;
        }
    
        String continueText = "Backspace. Back";
        GlyphLayout continueLayout = new GlyphLayout(font30, continueText);
        font30.draw(batch, continueText, game.getWorldWidth() / const_larg, game.getWorldHeight() * 0.1f);
    }
    

    public void dispose() {
        font30.dispose();
        font100.dispose();
        font150.dispose();
    }

    public void setHordas(int hordas) {
        this.hordas = hordas;
    }

    public int getHordas() {
        return hordas;
    }
}
