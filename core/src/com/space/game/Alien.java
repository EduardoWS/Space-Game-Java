package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Alien {
    private Texture texture;
    private TextureManager textureManager;
    private Vector2 position;
    private float speed = 200;
    private boolean death = false;
    private float scale;
    private Rectangle bounds;
    private float deathTimer;
    private final float TIME_TO_REMOVE = 2; // Tempo em segundos antes da remoção
    private float deltaTime = Gdx.graphics.getDeltaTime();

    public Alien(TextureManager textureManager, int pos, Vector2 spaceshipPosition) {
        texture = textureManager.getTexture("alien");
        this.textureManager = textureManager;
        scale = Math.min(Gdx.graphics.getWidth() / (float)texture.getWidth(), Gdx.graphics.getHeight() / (float)texture.getHeight());
        scale *= 0.035f; // Ajuste este valor conforme necessário

        // pos = 0: top, 1: right, 2: bottom, 3: left
        if (pos == 0) {
            // alien no topo
            position = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth() - texture.getWidth()), Gdx.graphics.getHeight() - texture.getHeight() * scale);
            speed = 100;
        } else if (pos == 1) {
            // alien na direita
            position = new Vector2(Gdx.graphics.getWidth() - texture.getWidth()* scale, spaceshipPosition.y);
            speed = 200;
        } else if (pos == 2) {
            // alien embaixo
            position = new Vector2(spaceshipPosition.x, 0);
            speed = 150;
        } else if (pos == 3) {
            // alien na esquerda
            position = new Vector2(0, spaceshipPosition.y);
        }

        bounds = new Rectangle(position.x, position.y, texture.getWidth() * scale, texture.getHeight() * scale);
    }

    public void markDeath() {
        death = true;
        deathTimer = 0;
    }

    public boolean isDead() {
        return death;
    }

    public boolean shouldRemove() {
        return this.isDead() && deathTimer > TIME_TO_REMOVE;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setTextureToDraw(String key) {
        texture = this.textureManager.getTexture(key);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDirection(Vector2 direction) {
        position.x += direction.x * speed * Gdx.graphics.getDeltaTime();
        position.y += direction.y * speed * Gdx.graphics.getDeltaTime();
        bounds.setPosition(position);
    }

    public void render(SpriteBatch batch) {
        // Desenha a textura do alien com a escala aplicada
        batch.draw(texture, position.x, position.y, texture.getWidth() / 2, texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 
        scale, scale, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        
    }

    public void update(Vector2 spaceshipPosition) {
        Vector2 direction = new Vector2(spaceshipPosition.x - position.x, spaceshipPosition.y - position.y);
        direction.nor(); // Normaliza o vetor de direção
        position.x += direction.x * speed * Gdx.graphics.getDeltaTime();
        position.y += direction.y * speed * Gdx.graphics.getDeltaTime();
        bounds.setPosition(position);
        if (this.isDead()) {
            this.deathTimer += this.deltaTime;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public void dispose() {
        // textureManager.dispose();
        // texture.dispose();
    }
}
