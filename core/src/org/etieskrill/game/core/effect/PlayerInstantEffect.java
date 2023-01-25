package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

public abstract class PlayerInstantEffect implements InstantEffect {

    @Override
    public void apply(Card.TargetMode targetMode, Entity caster, Entity target) {
        if (targetMode != Card.TargetMode.PLAYER)
            throw new EffectNotApplicableException("player instant effect must be applied to player");

        apply();
    }

    public abstract void apply();

}
