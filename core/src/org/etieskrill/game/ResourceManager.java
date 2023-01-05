package org.etieskrill.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {

    private AssetManager manager;

    public ResourceManager() {
        this.manager = new AssetManager();
        loadResources();
    }

    private void loadResources() {
        manager.load("skins/default/skin/uiskin.json", Skin.class);
        manager.load("parchment.png", Texture.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    public void dispose() {
        manager.dispose();
    }

}
