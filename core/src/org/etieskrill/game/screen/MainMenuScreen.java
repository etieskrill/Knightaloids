package org.etieskrill.game.screen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import org.etieskrill.game.App;
import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.CombatAPIImpl;
import org.etieskrill.game.core.InvalidCardTargetException;
import org.etieskrill.game.core.card.BlockAbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.card.CardCostTooHighException;
import org.etieskrill.game.core.card.SlashAbilityCard;
import org.etieskrill.game.core.entity.*;
import org.etieskrill.game.screen.actions.HitAction;
import org.etieskrill.game.util.AnimatedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen extends BaseScreen {

    public static final int CARD_WIDTH = 200, CARD_HEIGHT = (int) (1.5 * CARD_WIDTH);
    public static final int TOP_CARD_INDEX = 100, UI_ELEMENT_Z_INDEX = 50;
    public static final float DECK_Y = -CARD_HEIGHT / 2.5f, DECK_CARD_DISTANCE = CARD_WIDTH / 1.5f;
    public static final Vector2 DECK_POSITION = new Vector2();

    private static final Logger logger = Logger.getLogger(MainMenuScreen.class.getSimpleName());

    private CombatAPI mission;
    private List<HandCard> handCards;
    private boolean handCardsChanged = true;
    private HandCard prevFocus;
    private EntityContext.EntityContextFactory contextFactory;
    private List<EntityContext> allies;
    private List<EntityContext> enemies;
    private boolean entitiesChanged = true;
    private PlayerContext.PlayerContextFactory playerContextFactory;
    private PlayerContext playerContext;

    private Label drawPileCounterLabel;
    private Label discardPileCounterLabel;
    private Label exilePileCounterLabel;

    private Table alliesTable;
    private Table enemiesTable;

    private Image focusIndicator;
    private Entity previousFocusedEntity;

    private TextButton nextTurnButton;

    public MainMenuScreen(App app) {
        super(app);
    }

    @Override
    protected void init() {
        handCards = new ArrayList<>();
        allies = new ArrayList<>();
        enemies = new ArrayList<>();

        mission = new CombatAPIImpl();

        stage.addActor(new Image(manager.get("background_tundra.png", Texture.class)));

        mission.setPlayer(new Player(null, 4, 100, null, "hodekoboud"));

        List<Card> initialCards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            initialCards.add(new SlashAbilityCard());
            initialCards.add(new BlockAbilityCard());
        }

        Knight knight = new Knight();
        addEntity(knight);
        //TODO automate this, but beware; only do this once at beginning of combat
        initialCards.addAll(knight.getMoveSet());

        mission.setDrawPile(initialCards);

        /*knight.addAction(sequence(
                moveTo(500, 0, 0.5f, Interpolation.pow3In),
                delay(0.35f),
                moveTo(0, 0, 0.5f, Interpolation.pow3Out)
        ));*/

        //knight.addAction(hitAction);

        if (playerContextFactory == null) {
            playerContextFactory = new PlayerContext.PlayerContextFactory(manager);
        }
        playerContext = playerContextFactory.getFor(mission.getPlayer());
        playerContext.setPosition(playerContext.getWidth(), stage.getHeight() - playerContext.getHeight());
        stage.addActor(playerContext);

        TextureRegion _focusIndicator = new TextureRegion(manager.get("focus_indicator.png", Texture.class));
        _focusIndicator.flip(false, true);
        focusIndicator = new Image(_focusIndicator);
        focusIndicator.setScale(2.5f);
        focusIndicator.setVisible(false);
        stage.addActor(focusIndicator);

        mission.setEntities(new Goblin(), new Skeleton());

        super.addInputProcessor(0, new CardSelectorResetListener()); //TODO hack to make cards "loose focus"

        mission.drawCardsToHand(0);

        for (Card card : mission.getHandCards()) {
            addHandCard(card);
        }

        for (Entity entity : mission.getEntities()) {
            addEntity(entity);
        }

        Label.LabelStyle style = new Label.LabelStyle(manager.getLargeFont(), null);

        drawPileCounterLabel = new Label("G:0", style);
        drawPileCounterLabel.setFontScale(1f);
        discardPileCounterLabel = new Label("D:0", style);
        discardPileCounterLabel.setFontScale(1f);
        exilePileCounterLabel = new Label("E:0", style);
        exilePileCounterLabel.setFontScale(1f);

        Stack drawPileCounter = new Stack(new Image(), drawPileCounterLabel);
        Stack discardPileCounter = new Stack(new Image(), discardPileCounterLabel);
        Stack exilePileCounter = new Stack(new Image(), exilePileCounterLabel);

        Table bottom = new Table(skin);
        bottom.setSize(uiStage.getWidth(), uiStage.getHeight() / 5f);
        bottom.setPosition(0, 0);

        bottom.add(drawPileCounter).left().expand().pad(50f).padLeft(80f);
        bottom.add(discardPileCounter).right().bottom().pad(50f);
        bottom.add(exilePileCounter).right().top().pad(50f).padRight(80f);

        uiStage.addActor(bottom);

        alliesTable = new Table();
        alliesTable.setPosition(50, 150);
        alliesTable.setSize(600, 300);
        alliesTable.center();
        stage.addActor(alliesTable);

        enemiesTable = new Table();
        enemiesTable.setPosition(650, 150);
        enemiesTable.setSize(600, 300);
        enemiesTable.center();
        stage.addActor(enemiesTable);

        nextTurnButton = new TextButton("Next Turn", skin);
        nextTurnButton.setPosition(uiStage.getWidth() / 2f, uiStage.getHeight(), Align.top);
        //nextTurnButton.setSize(200f, 50f);
        nextTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mission.discardHandCards();
                mission.doEnemyTurn();
                mission.drawCardsToHand(0);
            }
        });
        uiStage.addActor(nextTurnButton);
    }

    private static class CardSelectorResetListener extends InputAdapter {
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            CardSelectorListener.setPreviousFocus(CardSelectorListener.getFocus());
            CardSelectorListener.setFocus(null);
            return false;
        }
    }

    private static class CardSelectorListener extends ClickListener {
        private static HandCard focus;
        private static HandCard previousFocus;

        private final HandCard handCard;
        private final CombatAPI mission;
        private final MainMenuScreen screen;

        public CardSelectorListener(HandCard handCard, CombatAPI mission, MainMenuScreen screen) {
            this.handCard = handCard;
            this.mission = mission;
            this.screen = screen;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (getPreviousFocus() == handCard) {
                try {
                    mission.playCard(handCard.getCard());
                    screen.removeHandCard(handCard.getCard());
                    setFocus(null);
                    return;
                } catch (InvalidCardTargetException | CardCostTooHighException e) {
                    logger.info("Attempt to play card failed: " + e.getMessage());
                    //TODO visual feedback
                }
            }

            setFocus(handCard);
        }

        public static synchronized HandCard getFocus() {
            return focus;
        }

        public static synchronized void setFocus(HandCard focus) {
            CardSelectorListener.focus = focus;
        }

        public static synchronized HandCard getPreviousFocus() {
            return previousFocus;
        }

        public static synchronized void setPreviousFocus(HandCard previousFocus) {
            CardSelectorListener.previousFocus = previousFocus;
        }
    }

    private static class EntitySelectorListener extends ClickListener {
        private final Entity entity;
        private final CombatAPI mission;

        public EntitySelectorListener(Entity entity, CombatAPI mission) {
            this.entity = entity;
            this.mission = mission;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            mission.selectEntity(entity);
        }
    }

    /**
     * A container class for a hand card and its associated image actor on the stage.
     */
    private static class HandCard {
        private final Card card;
        private final Image image;

        public HandCard(Card card, Image image) {
            this.card = card;
            this.image = image;
        }

        public Card getCard() {
            return card;
        }

        public Image getImage() {
            return image;
        }
    }

    private void addHandCards(Card... cards) {
        for (Card card : cards) addHandCard(card);
    }

    private void addHandCard(Card card) {
        TextureRegion region = new TextureRegion(new Texture(card.getPixmap()));
        region.flip(false, true);
        Image image = new Image(/*manager.get("parchment.png", Texture.class)*/ region);
        image.setSize(CARD_WIDTH, CARD_HEIGHT);
        HandCard handCard = new HandCard(card, image);
        image.addListener(new CardSelectorListener(handCard, mission, this));
        uiStage.addActor(image);
        handCards.add(handCard);

        handCardsChanged = true;
    }

    private void removeHandCards(Card... cards) {
        for (Card card : cards) removeHandCard(card);
    }

    private void removeHandCard(Card card) {
        for (HandCard handCard : handCards) {
            if (handCard.getCard() == card) {
                Image image = handCard.getImage();
                image.clear();
                image.remove();
                handCards.remove(handCard);
                break;
            }
        }

        handCardsChanged = true;
    }

    private static class EntityEffectListener extends Entity.EntityChangeAdapter {
        private final EntityContext context;

        public EntityEffectListener(EntityContext context) {
            this.context = context;
        }

        @Override
        public void receivedHealthDamage() {
            System.out.println("before: " + context.getStageActor().getActions());
            context.getStageActor().addAction(
                    sequence(
                            parallel(
                                    moveBy(1, 0, 0.2f, new Interpolation.ElasticOut(1.2f, 10, 3, 15)),
                                    color(Color.RED, 0.1f)
                            ),
                            parallel(
                                    moveBy(-1, 0),
                                    color(Color.WHITE, 0.1f)
                            )
                    )
            );
            System.out.println("after: " + context.getStageActor().getActions());
        }

        @Override
        public void receivedBlockDamage() {
        }
    }

    private void addEntities(Entity... entities) {
        for (Entity entity : entities) addEntity(entity);
    }

    private void addEntity(Entity entity) {
        if (contextFactory == null) contextFactory = new EntityContext.EntityContextFactory(manager);

        EntityContext entityContext = contextFactory.getFor(entity);
        if (entity instanceof AlliedEntity) allies.add(entityContext);
        else enemies.add(entityContext);

        entityContext.addListener(new EntitySelectorListener(entity, mission));
        entityContext.getEntity().addChangeListener(new EntityEffectListener(entityContext));
        stage.addActor(entityContext);

        entitiesChanged = true;
    }

    private void removeEntity(Entity entity) {
        EntityContext entityContext = findEntityContext(entity);
        if (entityContext != null) {
            AnimatedImage actor = entityContext.getStageActor();
            actor.clear();
            actor.remove();
            allies.remove(entityContext);
        }

        entitiesChanged = true;
    }

    @Override
    protected void update(float delta) {
        float scaleInitial = 1f, scale = 1.2f, duration = 0.15f;
        final int horizontalDiff = (int) ((CARD_WIDTH * scale) - (CARD_WIDTH * scaleInitial));
        final Interpolation interpolation = Interpolation.pow2;
        Action pickupAction = parallel(
                moveBy(-horizontalDiff, -DECK_Y, duration, interpolation),
                scaleTo(scale, scale, duration, interpolation));
        Action returnAction = parallel(
                moveBy(horizontalDiff, DECK_Y, duration, interpolation),
                scaleTo(scaleInitial, scaleInitial, duration, interpolation));

        if (CardSelectorListener.getFocus() == null) {
            if (prevFocus != null) {
                prevFocus.getImage().addAction(returnAction);
                for (int i = 0; i < handCards.size(); i++) handCards.get(i).getImage().setZIndex(i + UI_ELEMENT_Z_INDEX);
                prevFocus = null;
            }
        } else {
            for (int i = 0; i < handCards.size(); i++) {
                HandCard handCard = handCards.get(i);

                if (handCard != prevFocus && handCard == CardSelectorListener.getFocus()) {
                    handCard.getImage().addAction(pickupAction);
                    handCard.getImage().setZIndex(TOP_CARD_INDEX + UI_ELEMENT_Z_INDEX);
                    if (prevFocus != null) {
                        prevFocus.getImage().addAction(returnAction);
                    }
                    prevFocus = handCard;
                }

                if (handCard != CardSelectorListener.getFocus()) {
                    handCard.getImage().setZIndex(i + UI_ELEMENT_Z_INDEX);
                } else {
                    handCard.getImage().setZIndex(TOP_CARD_INDEX + UI_ELEMENT_Z_INDEX);
                }
            }
        }

        drawPileCounterLabel.setText("G:" + mission.getDrawPile().size());
        discardPileCounterLabel.setText("D:" + mission.getDiscardPile().size());
        exilePileCounterLabel.setText("E:" + mission.getExilePile().size());

        Entity focusedEntity = mission.getFocusedEntity();
        if (focusedEntity != null) {
            if (focusedEntity != previousFocusedEntity) {
                focusIndicator.setVisible(true);
                EntityContext context = findEntityContext(focusedEntity);
                if (context == null) throw new UnsupportedOperationException("entity has no stage context");
                Vector2 pos = new Vector2(context.getX() + (context.getWidth() / 2f) - (focusIndicator.getWidth()), 0);
                if (focusedEntity instanceof AlliedEntity) pos.add(alliesTable.getX(), alliesTable.getY(Align.top));
                else pos.add(enemiesTable.getX(Align.left), enemiesTable.getY(Align.top));
                focusIndicator.addAction(moveTo(pos.x, pos.y, 0.15f, Interpolation.pow2));
                previousFocusedEntity = focusedEntity;
            }
        } else focusIndicator.setVisible(false);

        if (handCardsChanged) {
            updateHandCardPositions();
            handCardsChanged = false;
        }

        if (entitiesChanged) {
            updateEntitiesDisplayed();
            entitiesChanged = false;
        }
    }
    
    private EntityContext findEntityContext(Entity entity) {
        for (EntityContext context : allies) if (context.getEntity() == entity) return context;
        for (EntityContext context : enemies) if (context.getEntity() == entity) return context;
        return null;
    }

    private void updateHandCardPositions() {
        DECK_POSITION.set(
                uiStage.getWidth() / 2f - DECK_CARD_DISTANCE * (handCards.size() / 2f) - (0.5f * CARD_WIDTH),
                DECK_Y);

        for (int i = 0; i < handCards.size(); i++) {
            handCards.get(i).getImage().setPosition(
                    i * DECK_CARD_DISTANCE + DECK_POSITION.x, DECK_POSITION.y);
        }
    }

    private void updateEntitiesDisplayed() {
        alliesTable.clearChildren();
        enemiesTable.clearChildren();

        for (EntityContext context : allies) alliesTable.add(context).center();
        for (EntityContext context : enemies) enemiesTable.add(context).left();
    }

    @Override
    protected void render() {
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}
