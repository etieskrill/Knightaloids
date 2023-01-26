package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public class AttackEffect extends EntityInstantEffect {

    private final int magnitude;

    public AttackEffect(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void apply(Entity caster, List<Entity> targets) {
        for (Entity target : targets) target.damage(magnitude * caster.getDamageMult());
    }

}
