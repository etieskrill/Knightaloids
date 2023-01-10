package org.etieskrill.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {

    private final AssetManager manager;

    public ResourceManager() {
        this.manager = new AssetManager();
        loadResources();
    }

    private void loadResources() {
        manager.load("skins/default/skin/uiskin.json", Skin.class);
        manager.load("parchment.png", Texture.class);
        manager.load("background_tundra.png", Texture.class);
        manager.load("knight_idle_1.png", Texture.class);
        manager.load("knight_idle_2.png", Texture.class);
    }

    public <T> T get(String fileName) {
        return manager.get(fileName);
    }

    public <T> T get(String fileName, Class<T> type) {
        return manager.get(fileName, type);
    }

    public AssetManager getManager() {
        return manager;
    }

    public void dispose() {
        manager.dispose();
    }

}
