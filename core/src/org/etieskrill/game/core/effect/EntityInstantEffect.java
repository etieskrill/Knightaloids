package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

public abstract class EntityInstantEffect implements InstantEffect {

    @Override
    public void apply(Card.TargetMode targetMode, Entity caster, Entity target) {
        //TODO consider moving validation here from CombatAPI
        if (caster == null && !Card.TargetMode.getCasterTargetModes().contains(targetMode))
            throw new EffectNotApplicableException("effect with caster target mode must have a caster");

        apply(caster, target);
    }

    public abstract void apply(Entity caster, Entity target);

}
