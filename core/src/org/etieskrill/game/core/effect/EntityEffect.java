package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.Entity;

public abstract class EntityEffect extends BaseEffect {

    private final Entity target;

    public EntityEffect(float magnitude, Entity target) {
        super(magnitude);
        this.target = target;
    }

}
