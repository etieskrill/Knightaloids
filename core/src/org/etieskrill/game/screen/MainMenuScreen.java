package org.etieskrill.game.screen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sun.tools.javac.Main;
import org.etieskrill.game.App;
import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.CombatAPIImpl;
import org.etieskrill.game.core.InvalidCardTargetException;
import org.etieskrill.game.core.card.BlockAbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.card.SlashAbilityCard;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.entity.Goblin;
import org.etieskrill.game.core.entity.Skeleton;
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
    private boolean handCardsChanged;
    private HandCard prevFocus;

    private Label drawPileCounterLabel;
    private Label discardPileCounterLabel;
    private Label exilePileCounterLabel;

    public MainMenuScreen(App app) {
        super(app);
    }

    @Override
    protected void init() {
        mission = new CombatAPIImpl();
        stage.addActor(new Image(manager.get("background_tundra.png", Texture.class)));
        Animation<TextureRegion> animation = new Animation<>(0.6f,
                new TextureRegion(manager.get("knight_idle_1.png", Texture.class)),
                new TextureRegion(manager.get("knight_idle_2.png", Texture.class)));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        AnimatedImage knight = new AnimatedImage(animation, true);
        knight.setSize(200, 200);
        stage.addActor(knight);

        /*knight.addAction(sequence(
                moveTo(500, 0, 0.5f, Interpolation.pow3In),
                delay(0.35f),
                moveTo(0, 0, 0.5f, Interpolation.pow3Out)
        ));*/

        EntityContext.EntityContextFactory contextFactory = new EntityContext.EntityContextFactory(manager);

        AnimatedImage goblinActor = AnimatedImage.fromPackedTexture(
                manager.get("entity/enemy/goblin/idle.png", Texture.class), 4, 1);
        goblinActor.setTouchable(Touchable.disabled);
        EntityContext goblin = contextFactory.getFor(new Goblin(), goblinActor);

        goblin.setPosition(700, 200);
        goblin.addListener(new EntitySelectorListener(goblin.getEntity(), mission));
        stage.addActor(goblin);

        AnimatedImage skeletonActor = AnimatedImage.fromPackedTexture(
                manager.get("entity/enemy/skeleton/idle.png", Texture.class), 4, 1);
        skeletonActor.setTouchable(Touchable.disabled);
        EntityContext skeleton = contextFactory.getFor(new Skeleton(), skeletonActor);

        skeleton.setPosition(900, 200);
        skeleton.addListener(new EntitySelectorListener(skeleton.getEntity(), mission));
        stage.addActor(skeleton);

        //Card handCard1 = new Card.CardBuilder("attack", 1, TargetMode.ENEMY_SINGLE, new AttackEffect()).build();
        Card handCard1 = new SlashAbilityCard();
        Card handCard2 = new SlashAbilityCard();
        Card handCard3 = new SlashAbilityCard();
        Card handCard4 = new SlashAbilityCard();
        //Card handCard2 = new Card.CardBuilder("defend", 1, TargetMode.ALLY_SINGLE, new AttackEffect()).build();
        Card handCard5 = new BlockAbilityCard();
        Card handCard6 = new BlockAbilityCard();
        Card handCard7 = new BlockAbilityCard();
        Card handCard8 = new BlockAbilityCard();
        /*Card handCard3 = new Card.CardBuilder("summon", 1, TargetMode.ALLY_SUMMON_FRONT, new AttackEffect()).build();
        Card handCard4 = new Card.CardBuilder("heal", 1, TargetMode.ALLY_SINGLE, new AttackEffect()).build();
        Card handCard5 = new Card.CardBuilder("recuperate", 1, TargetMode.PLAYER, new AttackEffect()).build();*/

        super.addInputProcessor(0, new CardSelectorResetListener()); //TODO hack to make cards "loose focus"

        mission.setDrawPile(List.of(handCard1, handCard2, handCard3, handCard4, handCard5, handCard6, handCard7, handCard8));
        mission.drawCardsToHand(0);

        handCards = new ArrayList<>();
        for (Card card : mission.getHandCards()) {
            addHandCard(card);
        }

        drawPileCounterLabel = new Label("G:0", skin);
        discardPileCounterLabel = new Label("D:0", skin);
        exilePileCounterLabel = new Label("E:0", skin);

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
                } catch (InvalidCardTargetException e) {
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
            System.out.println(mission.getFocusedEntity());
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
        handCardsChanged = true;
    }

    private void addHandCard(Card card) {
        TextureRegion region = new TextureRegion(new Texture(card.getPixmap()));
        region.flip(false, true);
        Image image = new Image(manager.get("parchment.png", Texture.class) /*region*/);
        image.setSize(CARD_WIDTH, CARD_HEIGHT);
        HandCard handCard = new HandCard(card, image);
        image.addListener(new CardSelectorListener(handCard, mission, this));
        uiStage.addActor(image);
        handCards.add(handCard);

        handCardsChanged = true;
    }

    private void removeHandCards(Card... cards) {
        for (Card card : cards) removeHandCard(card);
        handCardsChanged = true;
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

        if (mission.getFocusedEntity() != null); //TODO add focus indicator

        if (handCardsChanged) {
            updateHandCardPositions();
        }
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
