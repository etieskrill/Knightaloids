package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class SummonableEntity extends Entity {

    private final Card originCard;

    public SummonableEntity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects, Card originCard) {
        super(moveSet, baseHealth, statusEffects);
        this.originCard = originCard;
    }

    public Card getOriginCard() {
        return originCard;
    }

}
