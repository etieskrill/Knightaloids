package org.etieskrill.game.core;

import org.etieskrill.game.core.effect.BaseEffect;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    private final int attack;
    private final int defense;
    private final List<BaseEffect> effects;
    private final List<StatusEffect> statusEffects = new ArrayList<>();

    public Entity(int attack, int defense, List<BaseEffect> effects) {
        this.attack = attack;
        this.defense = defense;
        this.effects = effects;
        applyStartingEffects();
    }

    private void applyStartingEffects() {
    }

    protected void applyEffect(BaseEffect effect) {
    }

}
