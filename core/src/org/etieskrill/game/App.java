package org.etieskrill.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.etieskrill.game.screen.LoadingScreen;

public class App extends Game {

	private ResourceManager manager;
	private Skin skin;

	@Override
	public void create() {
		this.manager = new ResourceManager();
		this.skin = new Skin(Gdx.files.internal("skins/default/skin/uiskin.json"));
		this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		manager.dispose();
	}

	public ResourceManager getManager() {
		return manager;
	}

	public Skin getSkin() {
		return skin;
	}

}
