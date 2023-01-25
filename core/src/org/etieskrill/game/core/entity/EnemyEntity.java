package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class EnemyEntity extends NonPlayerEntity {

    public EnemyEntity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
    }

}
