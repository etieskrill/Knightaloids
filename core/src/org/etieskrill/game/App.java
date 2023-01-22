package org.etieskrill.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.etieskrill.game.screen.LoadingScreen;
import org.etieskrill.game.util.ResourceManager;

public class App extends Game {

	private ResourceManager manager;
	private Skin skin;
	private InputMultiplexer multiplexer;


	@Override
	public void create() {
		this.manager = new ResourceManager.CombinedResourceManager(new AssetManager());
		this.skin = new Skin(Gdx.files.internal("skins/default/skin/uiskin.json"));
		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);

		this.setScreen(new LoadingScreen(this));
	}

	/*FrameBuffer frameBuffer;

	SpriteBatch spriteBatch;
	BitmapFont font;

	TextureRegion bufferTextureRegion;
	Texture texture;
	OrthographicCamera cam;

	@Override
	public void create() {

		cam=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.setToOrtho(false);

		spriteBatch=new SpriteBatch();
		texture=new Texture("parchment.png");
		font=new BitmapFont();

		int w=500;
		int h=750;

		frameBuffer=new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false) ;
		frameBuffer.begin();

		Gdx.gl.glClearColor(0f,0f,0f,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.draw(texture,0,0);
		font.draw(spriteBatch,"Score :100",100,100);
		spriteBatch.end();

		//bufferTextureRegion =new TextureRegion(frameBuffer.getColorBufferTexture(),0,0,frameBuffer.getWidth(),frameBuffer.getHeight());
		//bufferTextureRegion.flip(false,true);

		ByteBuffer buf;
		Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGB888);
		buf = pixmap.getPixels();
		Gdx.gl.glReadPixels(0, 0, w, h, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, buf);

		frameBuffer.end();

		PixmapIO.writePNG(Gdx.files.external("output.png"), pixmap);
	}*/


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

	public InputMultiplexer getMultiplexer() {
		return multiplexer;
	}

}
