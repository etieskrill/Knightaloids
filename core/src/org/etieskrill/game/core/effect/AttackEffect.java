package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.card.Card;

public class AttackEffect extends EntityInstantEffect {

    @Override
    public void apply(Entity target) {
        target.damage(8);
    }

}
