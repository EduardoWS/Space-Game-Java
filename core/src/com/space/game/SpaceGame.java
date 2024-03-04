package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.space.game.GameStateManager.GameState;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
// import com.badlogic.gdx.scenes.scene2d.ui.List;
import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.Input;


public class SpaceGame extends ApplicationAdapter {
    private Spaceship spaceship;
    private Background background;
    private List<Alien> aliens;
	SpriteBatch batch;

    private BitmapFont font30, font100, font150;
    private int hordas;
    private String text_to_show;
    private GlyphLayout layout_text;

    GameStateManager gsm;
    

    @Override
    public void create() {
        gsm = new GameStateManager();
		batch = new SpriteBatch(); // Crie um novo objeto SpriteBatch
        background = new Background();
        spaceship = new Spaceship(aliens);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/nasalization-rg.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Cria uma fonte de tamanho 30
        parameter.size = 30;
        font30 = generator.generateFont(parameter);

        // Cria uma fonte de tamanho 100
        parameter.size = 100;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.RED;
        parameter.color = Color.BLACK;
        font100 = generator.generateFont(parameter);

        // Cria uma fonte de tamanho 150
        parameter.size = 150;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.WHITE;
        parameter.color = Color.BLACK;
        font150 = generator.generateFont(parameter);

        generator.dispose();

        gsm.setState(GameState.MENU);
        
        
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
		batch.begin();

        switch (gsm.getState()) {
            case MENU:
                updateMenuState();
                break;
            case PLAYING:
                updatePlayingState();
                break;
            case GAME_OVER:
                updateGameOverState();
                break;
        }

		batch.end();
    }


    private void updateMenuState() {
        
        // Desenha o título "SPACE GAME"
        String title = "SPACE GAME";
        GlyphLayout titleLayout = new GlyphLayout(font150, title);
        float title_x = Gdx.graphics.getWidth() / 2 - titleLayout.width / 2;
        float title_y = Gdx.graphics.getHeight()/2 + titleLayout.height;
        font150.draw(batch, title, title_x,  title_y);
    
        // Desenha o botão "New Game"
        String buttonText = "1. New Game";
        GlyphLayout buttonLayout = new GlyphLayout(font30, buttonText);
        float buttonX = Gdx.graphics.getWidth() / 2 - buttonLayout.width / 2;
        float buttonY = title_y - titleLayout.height*2;
        font30.setColor(Color.WHITE);
        font30.draw(batch, buttonText, buttonX, buttonY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            startNewGame();
        }
    
        // Verifica se o botão "New Game" foi pressionado
        // if (Gdx.input.isTouched()) {
        //     float touchX = Gdx.input.getX();
        //     float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Coordenadas do libGDX são invertidas no eixo Y
        //     if (touchX >= buttonX && touchX <= buttonX + buttonLayout.width && touchY >= buttonY && touchY <= buttonY + buttonLayout.height) {
        //         startNewGame();
        //     }
        // }
    }
    


    private void updatePlayingState() {
        background.render(batch);
        spaceship.render(batch);
        spaceship.update();
    
        // Verifica se é hora de iniciar uma nova horda
        if (spaceship.kills >= 20) {
            hordas++;
            spaceship.kills = 0;
        }
    
        // Adiciona novos aliens se necessário
        if (aliens.size() <= 2) {
            for (int i = 0; i < 4; i++) {
                aliens.add(new Alien(i, spaceship.getPosition()));
            }
            if (spaceship.kills != 0)
                spaceship.setAmmunitions(5);
        }
    
        // Atualização e renderização de cada alienígena
        for (Alien alien : aliens) {
            if (alien.getBounds().overlaps(spaceship.getBounds())) {
                gsm.setState(GameState.GAME_OVER);
                limparAliens();
                break;
            } else {
                alien.update(spaceship.getPosition());
                alien.render(batch);
            }
        }
    
        // Exibir informações do jogo
        displayGameInfo();
    }

    private void updateGameOverState() {
        // Configuração e exibição da mensagem de GAME OVER
        displayGameOverInfo();
    
        // Reiniciar jogo se 'R' for pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            gsm.setState(GameState.MENU);
        }
    }

    private void displayGameInfo() {
        // Código para exibir AMMO, Horda, e Kills
        
        // desenhar texto embaixo esquerdo
        text_to_show = "AMMO: ";
        layout_text = new GlyphLayout(font30, text_to_show + spaceship.getAmmunitions());
        font30.setColor(Color.WHITE);
        font30.draw(batch, text_to_show + spaceship.getAmmunitions(), 10, 30);
        // desenhar texto embaixo direito
        text_to_show = "Horda: ";
        layout_text = new GlyphLayout(font30, text_to_show + hordas);
        font30.draw(batch, text_to_show + hordas, Gdx.graphics.getWidth() - layout_text.width -10, 30);
        //desenhar text_to_showo embaixo centro
        text_to_show = "Kills: ";
        layout_text = new GlyphLayout(font30, text_to_show + spaceship.kills);
        font30.draw(batch, text_to_show + spaceship.kills, Gdx.graphics.getWidth()/2 - layout_text.width/2, 30);
    }
    
    private void displayGameOverInfo() {
        // Código para exibir GAME OVER e 'Press 'R' to restart', junto com AMMO, Horda, e Total Kills
        
        text_to_show = "GAME OVER";
        layout_text = new GlyphLayout(font100, text_to_show);
        font100.draw(batch, text_to_show, Gdx.graphics.getWidth()/2 - layout_text.width/2, Gdx.graphics.getHeight()/2+layout_text.height);

        font30.setColor(Color.RED);
        text_to_show = "press enter to continue";
        layout_text = new GlyphLayout(font30, text_to_show);
        font30.draw(batch, text_to_show, Gdx.graphics.getWidth()/2 - layout_text.width/2, Gdx.graphics.getHeight()/2 - layout_text.height*3);

        // desenhar texto embaixo esquerdo
        text_to_show = "AMMO: ";
        layout_text = new GlyphLayout(font30, text_to_show + spaceship.getAmmunitions());
        font30.draw(batch, text_to_show + spaceship.getAmmunitions(), 10, 30);
        // desenhar texto embaixo direito
        text_to_show = "Horda: ";
        layout_text = new GlyphLayout(font30, text_to_show + hordas);
        font30.draw(batch, text_to_show + hordas, Gdx.graphics.getWidth() - layout_text.width -10, 30);
        //desenhar texto embaixo centro
        text_to_show = "Total Kills: ";
        int total_kills = (spaceship.kills + (hordas-1)*20);
        layout_text = new GlyphLayout(font30, text_to_show + total_kills);
        font30.draw(batch, text_to_show + total_kills, Gdx.graphics.getWidth()/2 - layout_text.width/2, 30);
    }

    private void startNewGame() {
        // Código para iniciar um novo jogo
        gsm.setState(GameState.PLAYING);
        // se spaceship não for null, então limpe a nave
        if (spaceship != null) {
            spaceship.dispose();
        }
        aliens = new ArrayList<>(); 
        spaceship = new Spaceship(aliens);

        for (int i = 0; i < 4; i++) {
            aliens.add(new Alien(i, spaceship.getPosition()));
        }

        hordas=1;
    }



    // SEÇÃO DE LIMPEZA ----------------------------------------------

    private void limparAliens() {
        for (Alien alien : aliens) {
            alien.dispose();
        }
        aliens.clear();
    }

    @Override
    public void dispose() {
        spaceship.dispose();
        background.dispose();
        for (Alien alien : aliens) {
            alien.dispose();
        }
        aliens.clear();
        batch.dispose();

        font30.dispose();
        font100.dispose();
        font150.dispose();


        
    }

    // FIM DA SEÇÃO DE LIMPEZA ---------------------------------------
    
}
