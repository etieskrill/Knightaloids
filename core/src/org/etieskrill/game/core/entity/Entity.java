package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity {

    protected final List<Card> moveSet;
    protected int moveSetIndex;
    protected final float baseHealth;
    protected final List<StatusEffect> statusEffects;

    protected final List<EntityChangeListener> changeListeners;

    protected float health;
    protected float block; //Zeroed at beginning of own turn
    protected float damageMult = 1;
    protected float defenseMult = 1;

    public Entity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        this.moveSet = moveSet;
        this.baseHealth = baseHealth;
        this.health = baseHealth;
        this.statusEffects = statusEffects;

        this.changeListeners = new ArrayList<>(2);
    }

    public abstract String getName();

    public abstract String getDisplayName();

    public void damage(float damage) {
        damage /= defenseMult;
        if (damage - block <= 0) {
            block -= damage;
            for (EntityChangeListener listener : changeListeners) listener.receivedBlockDamage();
        } else {
            damage -= block;
            block = 0;
            this.health -= damage;
            for (EntityChangeListener listener : changeListeners) listener.receivedHealthDamage();
        }

        if (isDead()) for (EntityChangeListener listener : changeListeners) listener.entityDied();
    }

    public void damageIgnoreBlock(float damage) {
        damage /= defenseMult;
        this.health -= damage;

        for (EntityChangeListener listener : changeListeners) listener.receivedHealthDamage();
        if (isDead()) for (EntityChangeListener listener : changeListeners) listener.entityDied();
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

    public List<Card> getMoveSet() {
        return moveSet;
    }

    public List<StatusEffect> getStatusEffects() {
        return Collections.unmodifiableList(statusEffects);
    }

    public void addStatusEffect(StatusEffect effect) {
        for (StatusEffect statusEffect : statusEffects) {
            if (effect.getClass() == statusEffect.getClass()) {
                statusEffect.addStacks(effect.getStacks());
                return;
            }
        }

        this.statusEffects.add(effect.get()); //TODO figure this garbage out, probably requires a factory pattern
    }

    public void addChangeListener(EntityChangeListener listener) {
        this.changeListeners.add(listener);
    }

    public boolean removeChangeListener(EntityChangeListener listener) {
        return this.changeListeners.remove(listener);
    }

    public EntityChangeListener removeChangeListener(int index) {
        return this.changeListeners.remove(index);
    }

    public void clearChangeListeners() {
        this.changeListeners.clear();
    }

    /**
     * This is a garbage idea tbh, but eh. Reducing coupling is key, or so they say.
     */
    public interface EntityChangeListener {
        void receivedHealthDamage();

        void entityDied();

        void receivedBlockDamage();

        void statusEffectsChanged();
    }

    public static class EntityChangeAdapter implements EntityChangeListener {
        @Override
        public void receivedHealthDamage() {
        }

        @Override
        public void entityDied() {
        }

        @Override
        public void receivedBlockDamage() {
        }

        @Override
        public void statusEffectsChanged() {
        }
    }

}
