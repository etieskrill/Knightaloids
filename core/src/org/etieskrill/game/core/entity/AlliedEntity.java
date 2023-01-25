package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class AlliedEntity extends NonPlayerEntity {

    private int baseMana;
    private int mana;

    public AlliedEntity(List<Card> moveSet, int baseMana, int baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
        this.baseMana = baseMana;
        this.mana = baseMana;
    }

    @Override
    protected void doMove(int move) {
        throw new UnsupportedOperationException("allied entity has no moveset");
    }

    public int getMana() {
        return mana;
    }

    public boolean useMana(int amount) {
        if (mana - amount <= 0) return false;

        this.mana -= amount;
        return true;
    }

}
