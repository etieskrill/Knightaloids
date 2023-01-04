package org.etieskrill.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseScreen implements Screen {

    protected final SpriteBatch renderer = new SpriteBatch();

    protected final PerspectiveCamera camera = new PerspectiveCamera();
    protected final FillViewport viewport = new FillViewport(1920, 1080, camera);
    protected final Stage stage = new Stage(viewport, renderer);

    protected final OrthographicCamera uiCamera = new OrthographicCamera();
    protected final ScreenViewport uiViewport = new ScreenViewport(uiCamera);
    protected final Stage uiStage = new Stage(uiViewport, renderer);

    @Override
    public void render(float delta) {
        update(delta);
        render();
    }

    protected abstract void update(float delta);

    protected abstract void render();

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }

}
