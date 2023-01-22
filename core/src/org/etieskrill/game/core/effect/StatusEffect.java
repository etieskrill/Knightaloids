package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.card.Card;

public abstract class StatusEffect extends Effect {

    public enum EffectStacking {
        INTENSITY,
        DURATION,
        NONE
    }

    public enum EffectApplication {
        BEFORE_TURN,
        AFTER_TURN
    }

    private final EffectStacking STACKING;
    private final EffectApplication APPLICATION;
    private int stacks;

    protected StatusEffect(EffectStacking stacking, EffectApplication application, int stacks) {
        this.STACKING = stacking;
        this.APPLICATION = application;
        this.stacks = stacks;
    }

    @Override
    public void apply(Card.TargetMode targetMode, Entity target) {
        apply(target);
    }

    public abstract void apply(Entity owner);

    public EffectStacking getStacking() {
        return STACKING;
    }

    public EffectApplication getApplication() {
        return APPLICATION;
    }

}
