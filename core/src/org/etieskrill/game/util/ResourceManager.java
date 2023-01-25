package org.etieskrill.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class ResourceManager {

    //TODO there must be a design pattern for this
    protected AssetManager manager;

    protected BitmapFont kirsty16;
    protected BitmapFont kirsty32;
    protected BitmapFont kirsty48;
    protected BitmapFont kirsty64;

    public ResourceManager(AssetManager manager) {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.LIGHT_GRAY;
        param.borderWidth = 2;
        param.size = 32;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kirsty.ttf"));
        kirsty32 = generator.generateFont(param);
        param.size = 16;
        kirsty16 = generator.generateFont(param);
        param.size = 48;
        kirsty48 = generator.generateFont(param);
        param.size = 64;
        kirsty64 = generator.generateFont(param);
        generator.dispose();

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

    public BitmapFont getSmallFont() {
        return kirsty16;
    }

    public BitmapFont getFont() {
        return kirsty32;
    }

    public BitmapFont getLargeFont() {
        return kirsty48;
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
            manager.load("entity/entity_sprite_error.png", Texture.class);
            manager.load("background_tundra.png", Texture.class);
            manager.load("focus_indicator.png", Texture.class);
            manager.load("entity/ally/knight/idle.png", Texture.class);
            manager.load("entity/enemy/goblin/idle.png", Texture.class);
            manager.load("entity/enemy/skeleton/idle.png", Texture.class);
            manager.load("glow.png", Texture.class);
        }
    }

}
