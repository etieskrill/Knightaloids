package org.etieskrill.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.etieskrill.game.App;

public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(App app) {
        super(app);
    }

    @Override
    protected void init() {
        uiStage.addActor(new Image(manager.get("parchment.png", Texture.class)));
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
