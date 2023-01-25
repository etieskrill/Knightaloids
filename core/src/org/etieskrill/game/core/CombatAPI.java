package org.etieskrill.game.core;

import org.etieskrill.game.core.card.CardCostTooHighException;
import org.etieskrill.game.core.entity.AlliedEntity;
import org.etieskrill.game.core.entity.EnemyEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.entity.Player;

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

    boolean playCard(Card card) throws InvalidCardTargetException, CardCostTooHighException;

    Card playCard(int index) throws InvalidCardTargetException;

    void doEnemyTurn();

    boolean selectEntity(Entity entity);

    Entity selectEntity(int index);

    Entity getFocusedEntity();

    List<Entity> getEntities();

    void setEntities(Entity... entities);

    List<AlliedEntity> getAllies();

    List<EnemyEntity> getEnemies();

    void setPlayer(Player player);

    Player getPlayer();

    boolean useConsumable(Consumable consumable);

    Consumable useConsumable(int index);

}
