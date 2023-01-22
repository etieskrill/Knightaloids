package org.etieskrill.game.core.Entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public class AlliedEntity extends Entity {

    public AlliedEntity(Card originCard, List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        super(originCard, moveSet, baseHealth, statusEffects);
    }

}
