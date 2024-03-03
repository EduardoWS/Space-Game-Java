package com.space.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SpaceGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img, tNave;
	private Sprite nave;
    OrthographicCamera camera;

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("assets\\images\\scenario\\background.png");
		tNave = new Texture("assets\\images\\spaceships\\spaceship.png");
		nave = new Sprite(tNave);

        // Inicializa a câmera com a largura e altura da tela
        camera = new OrthographicCamera(1280, 720);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    @Override
    public void render () {
        ScreenUtils.clear(0, 0, 0, 1);
		// Define a cor da textura do plano de fundo com transparência desejada
		batch.setColor(1, 1, 1, 0.4f);

        // Define a matriz de projeção da câmera
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Desenha a imagem ajustada à tela
        batch.draw(img, 0, 0, camera.viewportWidth, camera.viewportHeight);

		// Restaura a cor padrão para evitar afetar outras texturas desenhadas na tela
		batch.setColor(Color.WHITE);

		// Define a cor de desenho da nave
		batch.setColor(1, 1, 1, 1); // Opacidade total (alpha = 1)
		// Desenha a nave
		batch.draw(nave, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // Atualiza o tamanho da tela da câmera
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
		tNave.dispose();
    }
}
