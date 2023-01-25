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
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        fb.begin();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        Texture tex = res.get("parchment.png", Texture.class);
        batch.draw(tex, 0, 0);
        BitmapFont font = res.getLargeFont(); //skin.getFont("default-font");
        font.setColor(Color.RED);
        font.draw(batch, card.getTitle(), 20, 550);
        font.draw(batch, Integer.toString(card.getCost()), 10, HEIGHT);
        batch.end();

        Pixmap pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);
        ByteBuffer buf = pixmap.getPixels();
        Gdx.gl.glReadPixels(0, 0, WIDTH, HEIGHT, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buf);

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

}
