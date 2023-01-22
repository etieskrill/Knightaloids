package org.etieskrill.game.core.effect;

public class EffectNotApplicableException extends RuntimeException {

    public EffectNotApplicableException() {
        super("effect could not be applied");
    }

    public EffectNotApplicableException(String message) {
        super(message);
    }

}
