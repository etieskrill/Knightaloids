package org.etieskrill.game.core.card;

import com.badlogic.gdx.graphics.Pixmap;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public abstract class EntityCard extends Card {

    private final Entity owner;

    protected EntityCard(String id, String backgroundNinePatch, String bannerTexture, String title, int cost, List<Effect> effects, CardDescription description, boolean hasFlavourText, CardDescription flavourText, TargetMode targetMode, Entity owner) {
        super(id, backgroundNinePatch, bannerTexture, title, cost, effects, description, hasFlavourText, flavourText, targetMode);
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
    }

}
