package org.etieskrill.game.core;

import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.card.Card;

import java.util.List;

public interface CombatAPI {

    void setDeck(List<Card> cards);

    void addCard(Card card);

    boolean removeCard(Card card);

    Card removeCard(int index);

    Card getCard(int index);

    List<Card> getCards();

    boolean playCard(Card card) throws InvalidCardTargetException;

    Card playCard(int index) throws InvalidCardTargetException;

    boolean selectEntity(Entity entity);

    Entity selectEntity(int index);

    boolean useConsumable(Consumable cons);

    Consumable useConsumable(int index);

}
