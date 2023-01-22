package org.etieskrill.game.core;

import org.etieskrill.game.core.Entity.AlliedEntity;
import org.etieskrill.game.core.Entity.EnemyEntity;
import org.etieskrill.game.core.Entity.Entity;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.card.Card.TargetMode;
import org.etieskrill.game.core.effect.Effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class CombatAPIImpl implements CombatAPI {

    private final List<Card> deck;
    private final List<Entity> allies;
    private final List<Entity> enemies;

    private Entity focusedEntity;

    public CombatAPIImpl() {
        this.deck = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    @Override
    public void setDeck(List<Card> cards) {
        deck.clear();
        deck.addAll(cards);
    }

    @Override
    public void addCard(Card card) {
        deck.add(card);
    }

    @Override
    public boolean removeCard(Card card) {
        return deck.remove(card);
    }

    @Override
    public Card removeCard(int index) {
        return deck.remove(index);
    }

    @Override
    public Card getCard(int index) {
        return deck.get(index);
    }

    @Override
    public List<Card> getCards() {
        return Collections.unmodifiableList(deck);
    }

    @Override
    public boolean playCard(Card card) {
        for (int i = 0; i < deck.size(); i++) {
            if (deck.get(i) == card) { //== Warranted, since object can change at runtime
                //TODO still, equals for card must be implemented
                playCard(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public Card playCard(int index) {
        if (index < 0) throw new IllegalArgumentException("index cannot be smaller than zero");
        if (index >= deck.size()) throw new ArrayIndexOutOfBoundsException("index larger than collection size");

        TargetMode targetMode = deck.get(index).getTargetMode();
        EnumSet<TargetMode> enemyTargetModes = EnumSet.of(TargetMode.ENEMY_SINGLE, TargetMode.ENEMY_ALL,
                TargetMode.ENEMY_SUMMON_FRONT, TargetMode.ENEMY_SUMMON_BEHIND); //TODO seriously, java?!
        boolean targetModeIsEnemy = enemyTargetModes.contains(targetMode);
        if (focusedEntity instanceof AlliedEntity && targetModeIsEnemy)
            throw new InvalidCardTargetException("card with allied target mode cannot be played on enemies");
        else if (focusedEntity instanceof EnemyEntity && !targetModeIsEnemy)
            throw new InvalidCardTargetException("card with enemy target mode cannot be played on allies");
        else if (focusedEntity == null && TargetMode.getEntityTargetModes().contains(targetMode)) {
            throw new InvalidCardTargetException("cards with entity target mode must have non-null target");
        }
        //Enuff input validation, I think??

        Card cardPlayed = deck.remove(index);
        for (Effect effect : cardPlayed.getEffects()) effect.apply(cardPlayed.getTargetMode(), focusedEntity);

        return cardPlayed;
    }

    @Override
    public boolean selectEntity(Entity entity) {
        this.focusedEntity = entity;
        return true;
    }

    @Override
    public Entity selectEntity(int index) {
        if (index < -1) throw new IllegalArgumentException("index cannot be smaller than zero");
        int alliesSize = allies.size(), enemiesSize = allies.size();
        if (index > alliesSize + enemiesSize)
            throw new ArrayIndexOutOfBoundsException("index larger than total entity cound");

        if (index < alliesSize) {
            return allies.get(index);
        } else {
            return enemies.get(index);
        }
    }

    @Override
    public boolean useConsumable(Consumable cons) {
        return false;
    }

    @Override
    public Consumable useConsumable(int index) {
        return null;
    }

}
