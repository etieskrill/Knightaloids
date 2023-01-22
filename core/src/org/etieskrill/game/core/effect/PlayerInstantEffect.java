package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.Player;
import org.etieskrill.game.core.card.Card;

public abstract class PlayerInstantEffect extends InstantEffect {

    @Override
    public void apply(Card.TargetMode targetMode, Entity target) {
        if (targetMode != Card.TargetMode.PLAYER)
            throw new EffectNotApplicableException("player instant effect must be applied to player");

        apply();
    }

    public abstract void apply();

}
