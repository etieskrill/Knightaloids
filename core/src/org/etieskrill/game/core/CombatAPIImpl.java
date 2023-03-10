package org.etieskrill.game.core;

import org.etieskrill.game.core.card.*;
import org.etieskrill.game.core.effect.EffectEvent;
import org.etieskrill.game.core.effect.InstantEffect;
import org.etieskrill.game.core.effect.StatusEffect;
import org.etieskrill.game.core.entity.AlliedEntity;
import org.etieskrill.game.core.entity.EnemyEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.card.Card.TargetMode;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.entity.Player;

import java.util.*;
import java.util.logging.Logger;

public class CombatAPIImpl implements CombatAPI {

    public static final int DEFAULT_NUMBER_CARDS_DRAWN = 4;

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final List<Card> drawPile;
    private final List<Card> handCards;
    private final List<Card> discardPile;
    private final List<Card> exilePile;

    private Player player;

    private final List<AlliedEntity> allies;
    private final List<EnemyEntity> enemies;

    private Entity focusedEntity;

    public CombatAPIImpl() {
        this.drawPile = new ArrayList<>();
        this.handCards = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.exilePile = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    @Override
    public void setDrawPile(List<Card> cards) {
        drawPile.clear();
        drawPile.addAll(cards);
        Collections.shuffle(drawPile);
    }

    @Override
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }

    @Override
    public void addCardToHand(Card card) {
        handCards.add(card);
    }

    @Override
    public void addCardToDiscardPile(Card card) {
        discardPile.add(0, card);
    }

    @Override
    public List<Card> getDrawPile() {
        return Collections.unmodifiableList(drawPile);
    }

    @Override
    public void drawCardsToHand(int additionalCards) {
        int totalNumberCardsDrawn = DEFAULT_NUMBER_CARDS_DRAWN + additionalCards;
        if (totalNumberCardsDrawn <= 0)
            throw new IllegalArgumentException("total number of cards drawn cannot be negative");

        for (int i = 0; i < totalNumberCardsDrawn; i++) {
            if (drawPile.isEmpty()) refreshDrawPile();
            handCards.add(drawPile.remove(0));
        }
    }

    private void refreshDrawPile() {
        if ((drawPile.size() | discardPile.size()) == 0)
            throw new IllegalStateException("neither draw pile nor discard pile contains any cards to reshuffle");
        drawPile.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(drawPile);
    }

    @Override
    public List<Card> getHandCards() {
        return Collections.unmodifiableList(handCards);
    }

    @Override
    public void discardHandCards() {
        discardPile.addAll(handCards);
        handCards.clear();
    }

    @Override
    public List<Card> getDiscardPile() {
        return Collections.unmodifiableList(discardPile);
    }

    @Override
    public List<Card> getExilePile() {
        return Collections.unmodifiableList(exilePile);
    }

    @Override
    public EffectEvent getEffectEventFor(Card card, Entity entity) {
        EffectEvent event = new EffectEvent(card.getTargetMode(), entity, new ArrayList<>(), allies, enemies, player);

        switch (card.getTargetMode()) {
            case ALLY_FRONT -> {
                event.setTarget(List.of(getAllies().size() == 0 ? getPlayer() : getAllies().get(getAllies().size() - 1)));
            }
            case ALLY_BACK -> event.setTarget(List.of(getAllies().size() == 0 ? getPlayer() : getAllies().get(0)));
            case ALLY_RANDOM -> {
                if (event.getAllies().size() == 0) {
                    event.setTarget(List.of(event.getPlayer()));
                    break;
                } else if (event.getAllies().size() == 1) {
                    event.setTarget(List.of(event.getAllies().get(0)));
                    break;
                }

                int target = new Random().nextInt(0, event.getAllies().size() - 1);
                event.setTarget(List.of(event.getAllies().get(target)));
            }
            /*case default -> { //TODO what kind of degenerated hairless monkey configured the gradle build config for this sodding project
                logger.fine("Card " + card.getClass().getSimpleName() + " had no identifiable target mode.");
            }*/
        }

        return event;
    }

    @Override
    public boolean playCard(Card card) {
        for (int i = 0; i < handCards.size(); i++) {
            if (handCards.get(i) == card) { //== Warranted, since object can change at runtime
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
        if (index >= handCards.size()) throw new ArrayIndexOutOfBoundsException("index larger than collection size");

        Entity caster = null;
        Card card = handCards.get(index);
        if (card instanceof AbilityCard) caster = focusedEntity;
        else if (card instanceof SkillCard || card instanceof PowerCard) caster = ((EntityCard)card).getOwner();
        if (caster != null) {
            if (caster instanceof Player player) {
                if (!player.hasMana(card.getCost())) {
                    logger.info("card could not be played because card cost was " + card.getCost() +
                            ", but player only had " + player.getMana() + " mana");
                    throw new CardCostTooHighException();
                }
            } else if (caster instanceof AlliedEntity ally) {
                if (!ally.hasMana(card.getCost())) {
                    logger.info("card could not be played because card cost was " + card.getCost() +
                            ", but ally only had " + ally.getMana() + " mana");
                    throw new CardCostTooHighException();
                }
            }
        } else {
            caster = player;
        }

        TargetMode targetMode = handCards.get(index).getTargetMode();
        boolean targetModeIsEnemy = TargetMode.getEnemyTargetModes().contains(targetMode);
        if (focusedEntity instanceof AlliedEntity && targetModeIsEnemy)
            throw new InvalidCardTargetException("card with allied target mode cannot be played on enemies");
        else if (focusedEntity instanceof EnemyEntity && !targetModeIsEnemy)
            throw new InvalidCardTargetException("card with enemy target mode cannot be played on allies");
        else if (focusedEntity == null && TargetMode.getEntityTargetModes().contains(targetMode)) {
            throw new InvalidCardTargetException("cards with entity target mode must have non-null target");
        }
        //Enuff input validation, I think??

        Card cardPlayed = handCards.remove(index);
        discardPile.add(cardPlayed);
        for (Effect effect : cardPlayed.getEffects()) {
            if (effect instanceof InstantEffect)
                effect.apply(new EffectEvent(card.getTargetMode(), caster,
                        focusedEntity == null ? null : List.of(focusedEntity), allies, enemies, player));
            else if (effect instanceof StatusEffect statusEffect)
                focusedEntity.addStatusEffect(statusEffect);
        }

        if (caster instanceof AlliedEntity entity) entity.useMana(card.getCost());
        else /*if (caster instanceof Player player)*/ player.useMana(card.getCost());

        return cardPlayed;
    }

    @Override
    public void nextTurn() {
        discardHandCards();
        List<EnemyEntity> _enemies = new ArrayList<>(enemies);
        for (EnemyEntity enemy : _enemies) {
            if (!enemy.act(this)) {
                enemies.remove(enemy);
                logger.info(enemy.getClass().getName() + " died");
            }
        }
        player.resetMana();
        List<AlliedEntity> _allies = new ArrayList<>(allies);
        for (AlliedEntity ally : _allies) {
            if (!ally.act(this)) {
                allies.remove(ally);
                logger.info(ally.getClass().getName() + " died");
            }
        }
        drawCardsToHand(0);
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
            throw new ArrayIndexOutOfBoundsException("index larger than total entity count");

        if (index < alliesSize) {
            return allies.get(index);
        } else {
            return enemies.get(index - alliesSize);
        }
    }

    @Override
    public Entity getFocusedEntity() {
        return focusedEntity;
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>(allies);
        entities.addAll(enemies);
        return Collections.unmodifiableList(entities);
    }

    @Override
    public void addEntities(Entity... entities) {
        for (Entity entity : entities) {
            if (entity instanceof AlliedEntity ally) {
                allies.add(ally);
                drawPile.addAll(ally.getMoveSet());
            }
            else if (entity instanceof EnemyEntity enemy) enemies.add(enemy);
            else throw new UnsupportedOperationException(
                    "entity type " + entity.getClass().getSimpleName() + " could not be classified");
        }
    }

    @Override
    public void updateEntities() {
        for (AlliedEntity ally : allies) {
            if (ally.isDead()) {
                allies.remove(ally);
                return;
            }
        }
        for (EnemyEntity enemy : enemies) {
            if (enemy.isDead()) {
                enemies.remove(enemy);
                return;
            }
        }
    }

    @Override
    public List<AlliedEntity> getAllies() {
        return Collections.unmodifiableList(allies);
    }

    @Override
    public List<EnemyEntity> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
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
