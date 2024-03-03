package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private float angle;
    private float speed = 800; 
    private Rectangle bounds;

    public Bullet(Vector2 spaceshipPosition, float angle, float spaceshipWidth, float spaceshipHeight) {
        this.angle = angle + 90;
        texture = new Texture("assets/images/guns/tiro1.png");
        float bulletWidth = texture.getWidth();
        float bulletHeight = texture.getHeight();
        float bullet_x = spaceshipPosition.x + spaceshipWidth / 2 - bulletWidth / 2;
        float bullet_y = spaceshipPosition.y + spaceshipHeight / 2 - bulletHeight / 2;
        position = new Vector2(bullet_x, bullet_y);
        bounds = new Rectangle(position.x, position.y, bulletWidth, bulletHeight);
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
        batch.draw(texture, position.x, position.y, texture.getWidth() / 2, texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 
        2, 2, angle+90, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}
