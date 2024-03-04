package com.space.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.utils.SortedIntList.Iterator;
import java.util.Iterator;
import com.badlogic.gdx.math.Rectangle;


// import com.badlogic.gdx.scenes.scene2d.ui.List;
import java.util.ArrayList;
import java.util.List;

public class Spaceship {
    private Texture texture;
    private Vector2 position;
    private int ammunitions;
    public int kills = 0;
    private float angle = 0;
    private boolean isLeftPressed = false; // Variável de controle para o botão "A"
    private boolean isRightPressed = false; // Variável de controle para o botão "D"
    private float scale;
    private float x_nave, y_nave;
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private List<Alien> aliens;

    public Spaceship(List<Alien> aliens) {
        texture = new Texture("assets/images/spaceships/spaceship.png");
        scale = Math.min(Gdx.graphics.getWidth() / (float)texture.getWidth(), Gdx.graphics.getHeight() / (float)texture.getHeight());
        scale *= 0.075f; // Ajuste este valor conforme necessário
        x_nave = Gdx.graphics.getWidth() / 2f - texture.getWidth() / 2f * scale;
        y_nave = Gdx.graphics.getHeight() / 2f - texture.getHeight() / 2f * scale;
        // System.out.println("x_nave: " + x_nave + " y_nave: " + y_nave);
        position = new Vector2(x_nave, y_nave);
        this.aliens = aliens;
        ammunitions = 10;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setAmmunitions(int ammunitions) {
        this.ammunitions += ammunitions;
    }

    public int getAmmunitions() {
        return ammunitions;
    }

    public void update() {
        // Verifica se o botão "A" foi pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            isLeftPressed = true;
            isRightPressed = false;
            // angle += 45; // Girar para a esquerda
            angle = 90;
        }

        // Verifica se o botão "D" foi pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            isLeftPressed = false;
            isRightPressed = true;
            // angle -= 45; // Girar para a direita
            angle = -90;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            isLeftPressed = true;
            isRightPressed = false;
            // angle += 45; // Girar para a esquerda
            angle = 0;
        }

        // Verifica se o botão "S" foi pressionado
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            isLeftPressed = false;
            isRightPressed = true;
            // angle -= 45; // Girar para a direita
            angle = 180;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            isLeftPressed = true;
            isRightPressed = false;
            angle = 45; // Girar para a direita
            
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            isLeftPressed = false;
            isRightPressed = true;
            angle = -45; // Girar para a direita
            
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // verificar se ja tem 3 tiros na tela
            if (bullets.size() < 5 && ammunitions > 0) {
                bullets.add(new Bullet(getPosition(), angle, texture.getWidth() * scale, texture.getHeight() * scale));
                this.ammunitions--;
                // System.out.println("Ammunitions: " + ammunitions);
            }
        }

        // Atualiza a posição da nave com base na velocidade
        // if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        //     position.y += speed * Gdx.graphics.getDeltaTime();
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        //     position.y -= speed * Gdx.graphics.getDeltaTime();
        // }
    }

    public void render(SpriteBatch batch) {
        // Desenha a textura da nave com a rotação e a escala aplicadas
        batch.draw(texture, position.x, position.y, texture.getWidth() / 2, texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 
        scale, scale, angle, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.shouldRemove()) {
                bulletIterator.remove();
                bullet.dispose();
                continue; // Pula para a próxima iteração do laço
            }
            bullet.update();
            bullet.render(batch);
            
            boolean bulletHitAlien = false;
    
            // remove as balas que sairam da tela (nas 4 direções) ou que colidiram com um alien
            if (bullet.getPosition().x < 0 || bullet.getPosition().x > Gdx.graphics.getWidth() || bullet.getPosition().y < 0 || bullet.getPosition().y > Gdx.graphics.getHeight() || bulletHitAlien) {
                bulletIterator.remove();
                bullet.dispose();
                continue; // Pula para a próxima iteração do laço
            }


            Iterator<Alien> alienIterator = aliens.iterator();
            while (alienIterator.hasNext()) {
                Alien alien = alienIterator.next();
                if (alien.getBounds().overlaps(bullet.getBounds())) {
                    // A bala colidiu com um alien
                    bulletHitAlien = true;
                    alienIterator.remove();
                    alien.dispose();
                    bullet.markForRemoval();
                    // bulletIterator.remove();
                    // bullet.dispose();
                    kills++;
                    break;
                }
            }
            
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, texture.getWidth() * scale, texture.getHeight() * scale);
    }


    public void dispose() {
        texture.dispose();
    }
}
