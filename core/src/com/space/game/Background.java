package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Background {
    private Texture texture;
    private Texture starTexture;
    private Star[] stars;
    private float alpha_background=0.2f;
    private boolean alpha_bool = false;
    private static final int NUM_STARS = 500; // Número de estrelas
   

    public Background(TextureManager textureManager) {
        texture = textureManager.getTexture("background");
        starTexture = textureManager.getTexture("star");

        stars = new Star[NUM_STARS];
        for (int i = 0; i < NUM_STARS; i++) {
            stars[i] = new Star();
            stars[i].brightness = MathUtils.random(1, 100);
            stars[i].duration = MathUtils.random(77, 777);
            stars[i].brightness_f = stars[i].brightness / 100f;
        }
    }

    private class Star {
        float x, y;
        int brightness;
        float brightness_f;
        int size;
        int duration;
        

        public Star() {
            x = MathUtils.random(Gdx.graphics.getWidth());
            y = MathUtils.random(Gdx.graphics.getHeight());
            // brightness = MathUtils.random(1, 100);
            // brightness_f = brightness / 100f;
            // brightness = 0;
            size = MathUtils.random(2, 5);
            // duration = 0;
        }

        public void update() {
            duration--;
            if (brightness > 0){
                brightness--;
            }

            if (duration <= 0) {
                duration = MathUtils.random(77, 777);
                brightness = MathUtils.random(1, 100);
            }
            brightness_f = brightness / 100f;
            // x -= 60 * Gdx.graphics.getDeltaTime(); // Move a estrela para a esquerda
            // brightness = MathUtils.random(0, 5); // E muda o brilho
            // if (x < 0) {
            //     x = Gdx.graphics.getWidth(); // Se a estrela sair da tela, reposiciona ela para a direita
            //     y = MathUtils.random(Gdx.graphics.getHeight()); // E muda a posição vertical
            //     brightness = MathUtils.random(); // E muda o brilho
            // }
        }
    }


    public void update() {
        for (Star star : stars) {
            star.update();
        }

        if(alpha_bool == false){
            alpha_background = MathUtils.clamp(alpha_background +0.000009f, 0.2f, 0.4f);
            if(alpha_background >= 0.4f){
                alpha_bool = true;
            }
        }
        else{
            alpha_background = MathUtils.clamp(alpha_background -  0.000009f, 0.2f, 0.4f);
            if(alpha_background <= 0.2f){
                alpha_bool = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        // Desenha as estrelas
        batch.setColor(Color.WHITE); // Restaura a cor padrão
        for (Star star : stars) {
            batch.setColor(1, 1, 1, star.brightness_f); // Usa o brilho para ajustar a transparência
            // randomizar o tamanho das estrelas
            batch.draw(starTexture, star.x, star.y, starTexture.getWidth()/star.size, starTexture.getHeight()/star.size); // Desenha uma pequena textura para cada estrela
        }


        
        batch.setColor(Color.WHITE); // Restaura a cor padrão
        batch.setColor(1, 1, 1, alpha_background);
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE); // Restaura a cor padrão para evitar afetar outras texturas desenhadas na tela

        
    }

    public void dispose() {
        // texture.dispose();
    }
}
