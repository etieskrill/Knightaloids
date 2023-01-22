package org.etieskrill.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Random;

public class AnimatedImage extends Image {

    public static final float DEFAULT_FRAME_DURATION = 0.16f;

    private final Animation<TextureRegion> animation;

    private float stateTime;

    public AnimatedImage(Animation<TextureRegion> animation, boolean randomStartingPoint) {
        super(animation.getKeyFrame(0));
        if (randomStartingPoint) stateTime += new Random().nextFloat(0, animation.getAnimationDuration());
        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        setDrawable(new TextureRegionDrawable(animation.getKeyFrame(stateTime += delta, true)));
        super.act(delta);
    }

    public static AnimatedImage fromPackedTexture(Texture texture, int H_FRAMES, int V_FRAMES) {
        TextureRegion[][] regions = TextureRegion.split(
                texture, texture.getWidth() / H_FRAMES, texture.getHeight() / V_FRAMES);
        TextureRegion[] sequence = new TextureRegion[H_FRAMES * V_FRAMES];
        int index = 0;
        for (int i = 0; i < V_FRAMES; i++) {
            for (int j = 0; j < H_FRAMES; j++) {
                sequence[index] = regions[i][j];
                sequence[index++].flip(true, false);
            }
        }
        Animation<TextureRegion> animation = new Animation<>(DEFAULT_FRAME_DURATION, sequence);
        return new AnimatedImage(animation, true);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

}
