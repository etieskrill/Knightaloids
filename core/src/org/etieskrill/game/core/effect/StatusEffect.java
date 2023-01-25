package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

public abstract class StatusEffect implements Effect {

    public enum EffectStacking {
        INTENSITY,
        DURATION,
        NONE
    }

    public enum EffectApplication {
        BEFORE_TURN,
        AFTER_TURN,
        CONSTANT
    }

    private final EffectStacking STACKING;
    private final EffectApplication APPLICATION;
    private int stacks;

    protected StatusEffect(EffectStacking stacking, EffectApplication application, int stacks) {
        this.STACKING = stacking;
        this.APPLICATION = application;
        this.stacks = stacks;
    }

    public abstract StatusEffect get();

    public void addStacks(int stacks) {
        this.stacks += stacks;
    }

    public void reduceStacks() {
        if (stacks > 0) stacks--;
    }

    public int getStacks() {
        return stacks;
    }

    @Override
    public void apply(Card.TargetMode targetMode, Entity caster, Entity target) {
        apply(target);
    }

    public abstract void apply(Entity target);

    public EffectStacking getStacking() {
        return STACKING;
    }

    public EffectApplication getApplication() {
        return APPLICATION;
    }

    public abstract String getAbbreviation();

}
