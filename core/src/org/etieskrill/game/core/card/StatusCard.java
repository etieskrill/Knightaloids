package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.Effect;

import java.util.List;

public abstract class StatusCard extends Card {

    protected StatusCard(String id, String backgroundNinePatch, String bannerTexture, String title, int cost, List<Effect> effects, CardDescription description, boolean hasFlavourText, CardDescription flavourText, TargetMode targetMode) {
        super(id, backgroundNinePatch, bannerTexture, title, cost, effects, description, hasFlavourText, flavourText, targetMode);
    }

}
