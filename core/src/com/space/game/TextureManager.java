package com.space.game;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;

public class TextureManager {
    private HashMap<String, Texture> textures;

    public TextureManager() {
        textures = new HashMap<String, Texture>();
    }

    public void loadTexture(String key, String path) {
        Texture texture = new Texture(path);
        textures.put(key, texture);
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
    }
}