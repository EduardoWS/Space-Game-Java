package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.scenes.scene2d.ui.List;
import java.util.List;
import java.util.ArrayList;

public class SpaceGame extends ApplicationAdapter {
    private Spaceship spaceship;
    private Background background;
    // private Alien alien0, alien1, alien2, alien3;
    private List<Alien> aliens = new ArrayList<>();
	SpriteBatch batch;
    // private List<Bullet> bullets = new ArrayList<Bullet>();


    @Override
    public void create() {
		batch = new SpriteBatch(); // Crie um novo objeto SpriteBatch
        background = new Background();
        spaceship = new Spaceship(aliens);
        for (int i = 0; i < 4; i++) {
            aliens.add(new Alien(i, spaceship.getPosition()));
        }
        // alien0 = new Alien(0, spaceship.getPosition());
        // alien1 = new Alien(1, spaceship.getPosition());
        // alien2 = new Alien(2, spaceship.getPosition());
        // alien3 = new Alien(3, spaceship.getPosition());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

        // alien0.update(spaceship.getPosition());
        // alien1.update(spaceship.getPosition());
        // alien2.update(spaceship.getPosition());
        // alien3.update(spaceship.getPosition());

        spaceship.update();
        background.render(batch);
        spaceship.render(batch);

        // verificar se o vetor de aliens está vazio
        if (aliens.isEmpty()) {
            // se estiver vazio, adicione 4 novos aliens
            for (int i = 0; i < 4; i++) {
                aliens.add(new Alien(i, spaceship.getPosition()));
            }
        }

        for (Alien alien : aliens) {
            alien.update(spaceship.getPosition());
            alien.render(batch);
        }
        // alien0.render(batch);
        // alien1.render(batch);
        // alien2.render(batch);
        // alien3.render(batch);

		batch.end();
    }

    @Override
    public void dispose() {
        spaceship.dispose();
        background.dispose();
        for (Alien alien : aliens) {
            alien.dispose();
        }
        // alien0.dispose();
        // alien1.dispose();
        // alien2.dispose();
        // alien3.dispose();
        batch.dispose();

        
    }
}
