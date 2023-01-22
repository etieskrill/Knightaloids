package org.etieskrill.game.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class ResourceManager {

    //TODO there must be a design pattern for this
    protected AssetManager manager;

    public ResourceManager(AssetManager manager) {
        this.manager = manager;
        //the skin was moved back here, since it is so universally used
        //TODO think about what to do with the skin/s
        manager.load("skins/default/skin/uiskin.json", Skin.class);
        loadResources();
    }

    protected abstract void loadResources();

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

    public static class CombinedResourceManager extends ResourceManager {
        public CombinedResourceManager(AssetManager manager) {
            super(manager);
        }

        @Override
        protected void loadResources() {
            new CardResourceManager(manager);
            new SpriteResourceManager(manager);
        }
    }

    public static class CardResourceManager extends ResourceManager {
        public CardResourceManager(AssetManager manager) {
            super(manager);
        }

        @Override
        protected void loadResources() {
            manager.load("parchment.png", Texture.class);
        }
    }

    public static class SpriteResourceManager extends ResourceManager {
        public SpriteResourceManager(AssetManager manager) {
            super(manager);
        }

        @Override
        protected void loadResources() {
            manager.load("background_tundra.png", Texture.class);
            manager.load("knight_idle_1.png", Texture.class);
            manager.load("knight_idle_2.png", Texture.class);
            manager.load("entity/enemy/goblin/idle.png", Texture.class);
            manager.load("entity/enemy/skeleton/idle.png", Texture.class);
        }
    }

}
