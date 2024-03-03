package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
// import com.badlogic.gdx.scenes.scene2d.ui.List;
import java.util.List;
import java.util.ArrayList;


public class SpaceGame extends ApplicationAdapter {
    private Spaceship spaceship;
    private Background background;
    private List<Alien> aliens = new ArrayList<>();
	SpriteBatch batch;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font;
    private int hordas;
    

    @Override
    public void create() {
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
        font = generator.generateFont(parameter);

        hordas=1;
        
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
		batch.begin();
        
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
            spaceship.setAmmunitions(5);
        }
        
        for (Alien alien : aliens) {
            if (alien.getBounds().overlaps(spaceship.getBounds())) {
                // O alien colidiu com a nave
                System.out.println("Fim do jogo");
                // Aqui você pode terminar o jogo
            }else{
                alien.update(spaceship.getPosition());
                alien.render(batch);

            }
        }
        
        // desenhar texto embaixo esquerdo
        font.draw(batch, "Ammunitions: " + spaceship.getAmmunitions(), Gdx.graphics.getWidth()*(1/3) + 10, 30);
        // desenhar texto embaixo direito
        font.draw(batch, "Horda: " + hordas, Gdx.graphics.getWidth() - 200, 30);
        //desenhar texto embaixo centro
        font.draw(batch, "Kills: " + spaceship.kills, Gdx.graphics.getWidth()/2 - 20, 30);
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
}
