package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    private List<Alien> aliens = new ArrayList<>();
	SpriteBatch batch;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font_white, font_red;
    private int hordas;
    private String text_to_show;
    private GlyphLayout layout_text;

    private enum GameState {
        PLAYING, GAME_OVER
    }
    private GameState gameState;
    

    @Override
    public void create() {
        gameState = GameState.PLAYING;

		batch = new SpriteBatch(); // Crie um novo objeto SpriteBatch
        background = new Background();
        spaceship = new Spaceship(aliens);
        for (int i = 0; i < 4; i++) {
            aliens.add(new Alien(i, spaceship.getPosition()));
        }

        generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/nasalization-rg.otf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.WHITE;
        parameter.color = Color.BLACK;
        font_white = generator.generateFont(parameter);

        hordas=1;
        
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
		batch.begin();

        if (gameState == GameState.GAME_OVER) {
            parameter.borderColor = Color.RED;
            parameter.borderWidth = 2;
            parameter.size = 100;
            parameter.color = Color.BLACK;
            font_red = generator.generateFont(parameter);
            text_to_show = "GAME OVER";
            layout_text = new GlyphLayout(font_red, text_to_show);
            font_red.draw(batch, text_to_show, Gdx.graphics.getWidth()/2 - layout_text.width/2, Gdx.graphics.getHeight()/2);
            parameter.size = 30;
            font_red = generator.generateFont(parameter);
            text_to_show = "Press 'R' to restart";
            layout_text = new GlyphLayout(font_red, text_to_show);
            font_red.draw(batch, text_to_show, Gdx.graphics.getWidth()/2 - layout_text.width/2, Gdx.graphics.getHeight()/2 - 150);

            // desenhar texto embaixo esquerdo
            text_to_show = "AMMO: ";
            layout_text = new GlyphLayout(font_red, text_to_show + spaceship.getAmmunitions());
            font_red.draw(batch, text_to_show + spaceship.getAmmunitions(), 10, 30);
            // desenhar texto embaixo direito
            text_to_show = "Horda: ";
            layout_text = new GlyphLayout(font_red, text_to_show + hordas);
            font_red.draw(batch, text_to_show + hordas, Gdx.graphics.getWidth() - layout_text.width -10, 30);
            //desenhar texto embaixo centro
            text_to_show = "Total Kills: ";
            int total_kills = (spaceship.kills + (hordas-1)*20);
            layout_text = new GlyphLayout(font_red, text_to_show + total_kills);
            font_red.draw(batch, text_to_show + total_kills, Gdx.graphics.getWidth()/2 - layout_text.width/2, 30);


            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gameState = GameState.PLAYING;
                spaceship = new Spaceship(aliens);
                hordas = 1;
            }


        }
        else{
            spaceship.update();
            background.render(batch);
            spaceship.render(batch);
    
            if (spaceship.kills >= 20){
                hordas++;
                spaceship.kills = 0;
            }
            
            // verificar se o vetor de aliens está com dois aliens
            if (aliens.size() <= 2) {
                // se estiver vazio, adicione 4 novos aliens
                for (int i = 0; i < 4; i++) {
                    aliens.add(new Alien(i, spaceship.getPosition()));
                }
                if (spaceship.kills != 0)
                    spaceship.setAmmunitions(5);
            }
            
            for (Alien alien : aliens) {
                if (alien.getBounds().overlaps(spaceship.getBounds())) {
                    // O alien colidiu com a nave
                    gameState = GameState.GAME_OVER;
                    limparAliens();
                    break;
    
                }else{
                    alien.update(spaceship.getPosition());
                    alien.render(batch);
    
                }
            }
            
            // desenhar texto embaixo esquerdo
            text_to_show = "AMMO: ";
            layout_text = new GlyphLayout(font_white, text_to_show + spaceship.getAmmunitions());
            font_white.draw(batch, text_to_show + spaceship.getAmmunitions(), 10, 30);
            // desenhar texto embaixo direito
            text_to_show = "Horda: ";
            layout_text = new GlyphLayout(font_white, text_to_show + hordas);
            font_white.draw(batch, text_to_show + hordas, Gdx.graphics.getWidth() - layout_text.width -10, 30);
            //desenhar text_to_showo embaixo centro
            text_to_show = "Kills: ";
            layout_text = new GlyphLayout(font_white, text_to_show + spaceship.kills);
            font_white.draw(batch, text_to_show + spaceship.kills, Gdx.graphics.getWidth()/2 - layout_text.width/2, 30);
        }
		batch.end();
    }

    @Override
    public void dispose() {
        spaceship.dispose();
        background.dispose();
        for (Alien alien : aliens) {
            alien.dispose();
        }
        batch.dispose();

        
    }

    private void limparAliens() {
        for (Alien alien : aliens) {
            alien.dispose();
        }
        aliens.clear();
    }
}
