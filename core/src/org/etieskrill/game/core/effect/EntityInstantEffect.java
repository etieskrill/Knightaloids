package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.EnemyEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

import java.util.List;
import java.util.Random;

import static org.etieskrill.game.core.card.Card.TargetMode.ALLY_FRONT;

public abstract class EntityInstantEffect implements InstantEffect {

    @Override
    public void apply(EffectEvent effect) {
        //TODO consider moving validation here from CombatAPI
        if (effect.getCaster() == null && !Card.TargetMode.getCasterTargetModes().contains(effect.getTargetMode()))
            throw new EffectNotApplicableException("effect with caster target mode must have a caster");

        apply(effect.getCaster(), effect.getTarget());
    }

    public abstract void apply(Entity caster, List<Entity> target);

}
