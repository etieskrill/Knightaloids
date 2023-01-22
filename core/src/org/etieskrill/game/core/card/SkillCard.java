package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public abstract class SkillCard extends EntityCard {

    protected SkillCard(String id, String backgroundNinePatch, String bannerTexture, String title, int cost, List<Effect> effects, CardDescription description, boolean hasFlavourText, CardDescription flavourText, TargetMode targetMode, Entity owner) {
        super(id, backgroundNinePatch, bannerTexture, title, cost, effects, description, hasFlavourText, flavourText, targetMode, owner);
    }

}
