package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class NonPlayerEntity extends Entity {

    public NonPlayerEntity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
    }

    /**
     * @return false if the entity died during move, true otherwise
     */
    public boolean act(CombatAPI combat) {
        resetMultipliers();

        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.BEFORE_TURN)
                statusEffect.apply(this);
        }

        if (isDead()) return false;

        this.block = 0;

        entityAct(combat);

        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.AFTER_TURN)
                statusEffect.apply(this);
            statusEffect.reduceStacks();
            if (statusEffect.getStacks() <= 0) statusEffects.remove(statusEffect);
        }

        return !isDead();
    }

    private void resetMultipliers() {
        this.damageMult = 1f;
        this.defenseMult = 1f;
    }

    protected abstract void entityAct(CombatAPI combat);

}
