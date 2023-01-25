package org.etieskrill.game.core.card;

public class CardCostTooHighException extends RuntimeException {

    public CardCostTooHighException() {
        super("card could not be played because of insufficient mana");
    }

}
