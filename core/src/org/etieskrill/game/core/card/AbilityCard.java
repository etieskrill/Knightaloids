package org.etieskrill.game.core.card;

import com.badlogic.gdx.graphics.Pixmap;
import org.etieskrill.game.core.effect.Effect;

import java.util.List;

public abstract class AbilityCard extends Card {

    protected AbilityCard(String id, String backgroundNinePatch, String bannerTexture, String title, int cost, List<Effect> effects, CardDescription description, boolean hasFlavourText, CardDescription flavourText, TargetMode targetMode) {
        super(id, backgroundNinePatch, bannerTexture, title, cost, effects, description, hasFlavourText, flavourText, targetMode);
    }

    public AbilityCard(String id, int cost, List<Effect> effects, TargetMode targetMode) {
        super(id, cost, effects, targetMode);
    }

}
