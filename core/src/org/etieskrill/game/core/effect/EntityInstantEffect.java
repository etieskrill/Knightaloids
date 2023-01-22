package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

public abstract class EntityInstantEffect extends InstantEffect {

    @Override
    public void apply(Card.TargetMode targetMode, Entity caster, Entity target) {
        //TODO consider moving validation here from CombatAPI

        apply(caster, target);
    }

    public abstract void apply(Entity caster, Entity target);

}
