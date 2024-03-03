package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture texture;

    public Background() {
        texture = new Texture("assets/images/scenario/background.png");
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 0.4f);
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE); // Restaura a cor padrão para evitar afetar outras texturas desenhadas na tela
    }

    public void dispose() {
        texture.dispose();
    }
}
