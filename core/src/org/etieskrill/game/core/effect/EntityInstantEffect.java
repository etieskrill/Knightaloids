package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.Entity.AlliedEntity;
import org.etieskrill.game.core.Entity.EnemyEntity;
import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.InvalidCardTargetException;
import org.etieskrill.game.core.card.Card;

import java.util.EnumSet;

public abstract class EntityInstantEffect extends InstantEffect {

    @Override
    public void apply(Card.TargetMode targetMode, Entity target) {
        //TODO consider moving validation here from CombatAPI

        apply(target);
    }

    public abstract void apply(Entity target);

}
