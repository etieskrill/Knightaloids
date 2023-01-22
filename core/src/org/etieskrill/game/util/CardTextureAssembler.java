package org.etieskrill.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.etieskrill.game.core.card.Card;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardTextureAssembler {

    public static final int WIDTH = 500;
    public static final int HEIGHT = (int) (1.5f * WIDTH);

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private ResourceManager.CardResourceManager res;
    private Skin skin;
    private FrameBuffer fb;

    private static CardTextureAssembler assembler;

    private CardTextureAssembler() {}

    public static CardTextureAssembler getInstance() {
        if (assembler == null) {
            assembler = new CardTextureAssembler();
        }

        return assembler;
    }

    public Pixmap get(Card card) {
        return get(card, new SpriteBatch());
    }

    public Pixmap get(Card card, SpriteBatch batch) {
        if (res == null) {
            res = new ResourceManager.CardResourceManager(new AssetManager());

            float prog = 0f;
            while (!res.getManager().update()) {
                if (res.getManager().getProgress() != prog) {
                    prog = res.getManager().getProgress();
                    logger.log(Level.FINE, String.format("Loading Resources... %.0f%%%n",
                                    res.getManager().getProgress() * 100));
                }
            }
            logger.info("Done loading resources.");
        }
        if (skin == null) skin = res.get("skins/default/skin/uiskin.json");
        if (fb == null) fb = new FrameBuffer(Pixmap.Format.RGBA8888,
                WIDTH, HEIGHT, false);
        System.out.println("Fb size: " + fb.getWidth() + " " + fb.getHeight());

        fb.begin();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        Texture tex = res.get("parchment.png", Texture.class);
        System.out.println("Tex size: " + tex.getWidth() + " " + tex.getHeight());
        System.out.println("TexData size: " + tex.getTextureData().getWidth() + " " + tex.getTextureData().getHeight());
        batch.draw(tex, 0, 0);
        BitmapFont font = new BitmapFont(); //skin.getFont("default-font");
        //font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(4);
        font.setColor(Color.RED);
        font.draw(batch, card.getTitle(), 20, 550);
        batch.end();

        //TextureRegion fbTr = new TextureRegion(fb.getColorBufferTexture());
        //fbTr.flip(false,true);

        Pixmap pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);
        ByteBuffer buf = pixmap.getPixels();
        Gdx.gl.glReadPixels(0, 0, WIDTH, HEIGHT, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buf);
        System.out.println("Pixmap size: " + pixmap.getWidth() + " " + pixmap.getHeight());

        fb.end();

        return pixmap;
    }

    /*public void save(Texture tex, String path) {
        if (!tex.getTextureData().isPrepared()) tex.getTextureData().prepare();
        PixmapIO.writePNG(Gdx.files.external(path), tex.getTextureData().consumePixmap());
    }*/

    public void dispose() {
        res.dispose();
        res = null;
        skin.dispose();
        skin = null;
        fb.dispose();
        fb = null;
    }

    //Code snippet i appropriated from stackoverflow, though does not do what i need it to it seems
    /*private float fboScaler = 1f;
    private boolean fboEnabled = true;
    private FrameBuffer fbo;
    private TextureRegion fboRegion;

    public void render(SpriteBatch spriteBatch) {
        if(fboEnabled) { // enable or disable the supersampling
            if(fbo == null) {
                // m_fboScaler increase or decrease the antialiasing quality

                fbo = new FrameBuffer(Pixmap.Format.RGB565, (int)(WIDTH * fboScaler), (int)(HEIGHT * fboScaler), false);
                fboRegion = new TextureRegion(fbo.getColorBufferTexture());
                fboRegion.flip(false, true);
            }

            fbo.begin();
        }

        // this is the main render function
        my_render_impl();

        if(fbo != null) {
            fbo.end();

            spriteBatch.begin();
            spriteBatch.draw(fboRegion, 0, 0, WIDTH, HEIGHT);
            spriteBatch.end();
        }
    }*/

}
