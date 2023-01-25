package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

public interface Effect {

    void apply(Card.TargetMode targetMode, Entity caster, Entity target);

}
