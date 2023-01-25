package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.effect.StatusEffect;
import org.etieskrill.game.core.entity.Entity;

public class BleedingStatusEffect extends StatusEffect {

    private final float magnitude;

    public BleedingStatusEffect(float magnitude) {
        super(EffectStacking.INTENSITY, EffectApplication.BEFORE_TURN, 6);
        this.magnitude = magnitude;
    }

    @Override
    public StatusEffect get() {
        return new BleedingStatusEffect(magnitude);
    }

    @Override
    public void apply(Entity target) {
        target.damageIgnoreBlock(getStacks());
    }

    @Override
    public String getAbbreviation() {
        return "B";
    }

}
