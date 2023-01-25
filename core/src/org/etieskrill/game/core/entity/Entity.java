package org.etieskrill.game.core.entity;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import org.etieskrill.game.core.CombatAPI;
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

        if (this instanceof EnemyEntity) { //TODO i know this is very bad, but i just don't have the time right now
            doMove(moveSetIndex++);
            if (moveSet.size() <= moveSetIndex) moveSetIndex = 0;
        }

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
        if (damage - block <= 0) {
            block -= damage;
            for (EntityChangeListener listener : changeListeners) listener.receivedBlockDamage();
        } else {
            damage -= block;
            block = 0;
            this.health -= damage;
            for (EntityChangeListener listener : changeListeners) listener.receivedHealthDamage();
        }
    }

    public void damageIgnoreBlock(float damage) {
        damage /= defenseMult;
        this.health -= damage;

        for (EntityChangeListener listener : changeListeners) listener.receivedHealthDamage();
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

    /**
     * This is a garbage idea tbh, but eh. Reducing coupling is key, or so they say.
     */
    public interface EntityChangeListener {
        void receivedHealthDamage();

        void receivedBlockDamage();

        void statusEffectsChanged();
    }

    public static class EntityChangeAdapter implements EntityChangeListener {
        public void receivedHealthDamage() {
        }

        public void receivedBlockDamage() {
        }

        public void statusEffectsChanged() {
        }
    }

}
