package org.etieskrill.game.core.Entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class Entity {

    private final Card originCard;

    private final List<Card> moveSet;
    private int moveSetIndex;
    private final float baseHealth;
    private final List<StatusEffect> statusEffects;

    private float health;
    private float block; //Zeroed at beginning of own turn
    private float damageMult = 1;
    private float defenseMult = 1;

    public Entity(Card originCard, List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        this.originCard = originCard;
        this.moveSet = moveSet;
        this.baseHealth = baseHealth;
        this.statusEffects = statusEffects;
    }

    /**
     * @return false if the entity died during move, true otherwise
     */
    public boolean act() {
        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.BEFORE_TURN)
                statusEffect.apply(this);
        }

        if (isDead()) return false;

        if (moveSet.size() <= moveSetIndex) moveSetIndex = 0;
        else moveSetIndex++;

        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.AFTER_TURN)
                statusEffect.apply(this);
        }

        return !isDead();
    }

    private void updateMultipliers() {
    }

    public void damage(float damage) {
        if (damage - block <= 0) block -= damage;
        else {
            damage -= block;
            block = 0;
            this.health -= damage;
        }
    }

    public void damageIgnoreBlock(float damage) {
        this.health -= damage;
    }

    public float getHealth() {
        return health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public float getBaseHealth() {
        return baseHealth;
    }

    public float getBlock() {
        return block;
    }

}
