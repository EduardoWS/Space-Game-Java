package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private float angle;
    private float scale;
    private float speed = 800; 
    private Rectangle bounds;
    private boolean shouldRemove = false;

    public Bullet(TextureManager textureManager, Vector2 spaceshipPosition, float angle, float spaceshipWidth, float spaceshipHeight, float scale) {
        this.angle = angle + 90;
        this.scale = scale;

        texture = textureManager.getTexture("bullet");
        float bulletWidth = texture.getWidth();
        float bulletHeight = texture.getHeight();
    
        float bullet_x = spaceshipPosition.x + spaceshipWidth / 2 - bulletWidth / 2 + (MathUtils.cos(MathUtils.degreesToRadians * (angle + 90)) * spaceshipWidth/1.5f);
        float bullet_y = spaceshipPosition.y + spaceshipHeight / 2 - bulletHeight / 2 + (MathUtils.sin(MathUtils.degreesToRadians * (angle + 90)) * spaceshipHeight/1.5f);
        
        position = new Vector2(bullet_x-2.5f, bullet_y-2.5f);
        bounds = new Rectangle(position.x, position.y, bulletWidth, bulletHeight);
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void markForRemoval() {
        shouldRemove = true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update() {
        position.x += speed * Math.cos(Math.toRadians(angle)) * Gdx.graphics.getDeltaTime();
        position.y += speed * Math.sin(Math.toRadians(angle)) * Gdx.graphics.getDeltaTime();
        bounds.setPosition(position);
    }

    public void render(SpriteBatch batch) {
        if (shouldRemove) return;
        batch.draw(texture, 
        position.x, position.y, 
        texture.getWidth() / 2, texture.getHeight() / 2, 
        texture.getWidth(), texture.getHeight(), 
        this.scale, this.scale, 
        angle+90, 0, 0, 
        texture.getWidth(), texture.getHeight(), 
        false, false);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        // texture.dispose();

    }
}
