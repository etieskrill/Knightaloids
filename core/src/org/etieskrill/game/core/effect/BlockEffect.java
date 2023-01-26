package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public class BlockEffect extends EntityInstantEffect {

    private final float magnitude;

    public BlockEffect(float magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void apply(Entity caster, List<Entity> target) {
        caster.addBlock((int) magnitude);
    }

}
