package org.etieskrill.game.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.etieskrill.game.App;
import org.etieskrill.game.util.ResourceManager;

public abstract class BaseScreen implements Screen {

    protected final App app;
    protected final ResourceManager manager; //TODO consider making this static
    protected final Skin skin;

    protected final SpriteBatch batch = new SpriteBatch();

    protected final OrthographicCamera camera = new OrthographicCamera();
    protected final FillViewport viewport = new FillViewport(1280, 720, camera);
    protected final Stage stage = new Stage(viewport, batch);

    protected final OrthographicCamera uiCamera = new OrthographicCamera();
    protected final ScreenViewport uiViewport = new ScreenViewport(uiCamera);
    protected final Stage uiStage = new Stage(uiViewport, batch);

    public BaseScreen(App app) {
        this.app = app; //TODO encapsulate this data in some context idk
        this.manager = app.getManager();
        this.skin = app.getSkin();
        viewport.apply();
        uiViewport.apply();

        setInputProcessors(uiStage, stage);

        init();
    }

    protected abstract void init();

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        update(delta);
        stage.act(delta);
        uiStage.act(delta);

        render();
        stage.getViewport().apply();
        //batch.setProjectionMatrix(camera.combined);
        stage.draw();
        uiStage.getViewport().apply();
        //batch.setProjectionMatrix(uiCamera.combined);
        uiStage.draw();
    }

    protected abstract void update(float delta);

    protected abstract void render();

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
    }

    protected void addInputProcessor(int index, InputProcessor processor) {
        this.app.getMultiplexer().addProcessor(index, processor);
    }

    protected void setInputProcessors(InputProcessor... processors) {
        this.app.getMultiplexer().setProcessors(processors);
    }

    protected void removeInputProcessor(InputProcessor processor) {
        this.app.getMultiplexer().removeProcessor(processor);
    }

}
