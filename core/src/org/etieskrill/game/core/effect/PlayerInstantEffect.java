package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.AlliedEntity;
import org.etieskrill.game.core.entity.EnemyEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.entity.Player;

import java.util.List;

public abstract class PlayerInstantEffect implements InstantEffect {

    @Override
    public void apply(EffectEvent effect) {
        if (effect.getTargetMode() != Card.TargetMode.PLAYER)
            throw new EffectNotApplicableException("player instant effect must be applied to player");

        apply(effect.getPlayer());
    }

    public abstract void apply(Player player);

}
