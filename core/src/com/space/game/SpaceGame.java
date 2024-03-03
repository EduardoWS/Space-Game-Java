package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceGame extends ApplicationAdapter {
    private Spaceship spaceship;
    private Background background;
	SpriteBatch batch;


    @Override
    public void create() {
		batch = new SpriteBatch(); // Crie um novo objeto SpriteBatch
        spaceship = new Spaceship();
        background = new Background();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

        spaceship.update();
        background.render(batch);
        spaceship.render(batch);

		batch.end();
    }

    @Override
    public void dispose() {
        spaceship.dispose();
        background.dispose();
    }
}
