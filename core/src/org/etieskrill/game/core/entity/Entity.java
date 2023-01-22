package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class Entity {

    protected final List<Card> moveSet;
    protected int moveSetIndex;
    protected final float baseHealth;
    protected final List<StatusEffect> statusEffects;

    protected float health;
    protected float block; //Zeroed at beginning of own turn
    protected float damageMult = 1;
    protected float defenseMult = 1;

    public Entity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        this.moveSet = moveSet;
        this.baseHealth = baseHealth;
        this.health = baseHealth;
        this.statusEffects = statusEffects;
    }

    /**
     * @return false if the entity died during move, true otherwise
     */
    public boolean act() {
        updateMultipliers();

        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.BEFORE_TURN)
                statusEffect.apply(this);
        }

        if (isDead()) return false;

        this.block = 0;

        doMove(moveSetIndex++);
        if (moveSet.size() <= moveSetIndex) moveSetIndex = 0;

        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getApplication() == StatusEffect.EffectApplication.AFTER_TURN)
                statusEffect.apply(this);
            statusEffect.reduceStacks();
            if (statusEffect.getStacks() <= 0) statusEffects.remove(statusEffect);
        }

        updateMultipliers();
        return !isDead();
    }

    protected abstract void doMove(int move);

    private void updateMultipliers() {
    }

    public abstract String getName();

    public abstract String getDisplayName();

    public void damage(float damage) {
        damage /= defenseMult;
        if (damage - block <= 0) block -= damage;
        else {
            damage -= block;
            block = 0;
            this.health -= damage;
        }
    }

    public void damageIgnoreBlock(float damage) {
        damage /= defenseMult;
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

    public void addBlock(int amount) {
        this.block += amount;
    }

    public float getBlock() {
        return block;
    }

    public float getDamageMult() {
        return damageMult;
    }

    public void setDamageMult(float damageMult) {
        this.damageMult = damageMult;
    }

    public float getDefenseMult() {
        return defenseMult;
    }

    public void setDefenseMult(float defenseMult) {
        this.defenseMult = defenseMult;
    }

}
