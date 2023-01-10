package org.etieskrill.game.core.effect;

public abstract class BaseEffect {

    private final float magnitude;

    public BaseEffect(float magnitude) {
        this.magnitude = magnitude;
    }

    protected abstract void apply();

}
