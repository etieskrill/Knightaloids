package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.effect.EffectEvent;
import org.etieskrill.game.core.effect.InstantEffect;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public abstract class EnemyEntity extends NonPlayerEntity {

    public EnemyEntity(List<Card> moveSet, float baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
    }

    @Override
    protected void entityAct(CombatAPI combat) {
        if (moveSet.size() == 0) throw new IllegalStateException("entity move set has no elements");

        Card move = moveSet.get(moveSetIndex++);
        if (moveSet.size() <= moveSetIndex) moveSetIndex = 0;

        for (Effect effect : move.getEffects()) {
            EffectEvent event = combat.getEffectEventFor(move, this);
            System.out.println(event);
            effect.apply(event);
        }
    }

}
