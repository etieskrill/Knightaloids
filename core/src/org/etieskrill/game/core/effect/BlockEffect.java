package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;

public class BlockEffect extends EntityInstantEffect {

    @Override
    public void apply(Entity caster, Entity target) {
        caster.addBlock(6);
    }

}
