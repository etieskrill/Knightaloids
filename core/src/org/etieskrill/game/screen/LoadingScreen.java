package org.etieskrill.game.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.etieskrill.game.App;

public class LoadingScreen extends BaseScreen {

    public LoadingScreen(App app) {
        super(app);
    }

    @Override
    protected void init() {
        Label label = new Label("Loading...", skin);
        label.setName("labelProgress");
        label.setPosition(-label.getWidth() / 2f + stage.getWidth() / 2f,
                -label.getHeight() / 2f + stage.getHeight() / 2f);

        uiStage.addActor(label);
    }

    @Override
    protected void update(float delta) {
    }

    @Override
    protected void render() {
        if (manager.update()) {
            app.setScreen(new MainMenuScreen(app));
        }

        for (Actor actor : uiStage.getActors()) {
            if (actor.getName().equals("labelProgress")) {
                ((Label) actor).setText("Loading...%.0f%%".formatted(manager.getProgress() * 100));
            }
        }
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
