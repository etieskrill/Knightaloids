package org.etieskrill.game.core;

import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;

import java.util.List;

public interface CombatAPI {

    void setDrawPile(List<Card> cards);

    void addCardToDrawPile(Card card);

    void addCardToHand(Card card);

    void addCardToDiscardPile(Card card);

    List<Card> getDrawPile();

    void drawCardsToHand(int additionalCards);

    List<Card> getHandCards();

    void discardHandCards();

    List<Card> getDiscardPile();

    List<Card> getExilePile();

    boolean playCard(Card card) throws InvalidCardTargetException;

    Card playCard(int index) throws InvalidCardTargetException;

    boolean selectEntity(Entity entity);

    Entity selectEntity(int index);

    Entity getFocusedEntity();

    List<Entity> getEntities();

    List<Entity> getAllies();

    List<Entity> getEnemies();

    boolean useConsumable(Consumable consumable);

    Consumable useConsumable(int index);

}
