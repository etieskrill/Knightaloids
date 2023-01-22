package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public class Knight extends AlliedEntity {

    public Knight(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
    }

    @Override
    protected void doMove(int move) {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }


}
