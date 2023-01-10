package org.etieskrill.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.etieskrill.game.App;
import org.etieskrill.game.util.AnimatedImage;

public class MainMenuScreen extends BaseScreen {

    Animation<TextureRegion> animation;

    public MainMenuScreen(App app) {
        super(app);
    }

    @Override
    protected void init() {
        stage.addActor(new Image(manager.get("background_tundra.png", Texture.class)));
        animation = new Animation<>(0.6f,
                new TextureRegion(manager.get("knight_idle_1.png", Texture.class)),
                new TextureRegion(manager.get("knight_idle_2.png", Texture.class)));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        AnimatedImage knight = new AnimatedImage(animation);
        knight.setSize(200, 200);
        stage.addActor(knight);
    }

    @Override
    protected void update(float delta) {
    }

    @Override
    protected void render() {
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
