package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;

public class AttackEffect extends EntityInstantEffect {

    private final int magnitude;

    public AttackEffect(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void apply(Entity caster, Entity target) {
        target.damage(magnitude * caster.getDamageMult());
    }

}
