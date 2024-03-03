package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Spaceship {
    private Texture texture;
    private Vector2 position;
    private float speed = 200;
    private float angle = 0;
    private boolean isLeftPressed = false; // Variável de controle para o botão "A"
    private boolean isRightPressed = false; // Variável de controle para o botão "D"
    private float scale;
    

    public Spaceship() {
        texture = new Texture("assets/images/spaceships/spaceship.png");
        scale = Math.min(Gdx.graphics.getWidth() / (float)texture.getWidth(), Gdx.graphics.getHeight() / (float)texture.getHeight());
        scale *= 0.075f; // Ajuste este valor conforme necessário
        position = new Vector2(Gdx.graphics.getWidth() / 2f - texture.getWidth() / 2f * scale, Gdx.graphics.getHeight() / 2f - texture.getHeight() / 2f * scale);
    }

    public void update() {
        // Verifica se o botão "A" foi pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            isLeftPressed = true;
            isRightPressed = false;
            angle += 45; // Girar para a esquerda
        }

        // Verifica se o botão "D" foi pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            isLeftPressed = false;
            isRightPressed = true;
            angle -= 45; // Girar para a direita
        }

        // Verifica se nenhum botão está pressionado para parar a rotação
        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            isLeftPressed = false;
            isRightPressed = false;
        }

        // Reseta o ângulo se ambos os botões estiverem pressionados
        if (isLeftPressed && isRightPressed) {
            angle = 0;
        }

        // Atualiza a posição da nave com base na velocidade
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= speed * Gdx.graphics.getDeltaTime();
        }
    }

    public void render(SpriteBatch batch) {
        // Desenha a textura da nave com a rotação e a escala aplicadas
        batch.draw(texture, position.x, position.y, texture.getWidth() / 2, texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 
        scale, scale, angle, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }


    public void dispose() {
        texture.dispose();
    }
}
