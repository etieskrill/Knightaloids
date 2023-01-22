package org.etieskrill.game.screen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.etieskrill.game.App;
import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.CombatAPIImpl;
import org.etieskrill.game.core.GameAPI;
import org.etieskrill.game.core.InvalidCardTargetException;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.card.Card.TargetMode;
import org.etieskrill.game.core.effect.AttackEffect;
import org.etieskrill.game.util.AnimatedImage;

import java.util.*;
import java.util.logging.Logger;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen extends BaseScreen {

    public static final int CARD_WIDTH = 200, CARD_HEIGHT = (int) (1.5 * CARD_WIDTH), TOP_CARD_INDEX = 100;
    public static final float DECK_Y = -CARD_HEIGHT / 3f, DECK_CARD_DISTANCE = CARD_WIDTH / 1.5f;
    public static final Vector2 DECK_POSITION = new Vector2();

    private static final Logger logger = Logger.getLogger(MainMenuScreen.class.getSimpleName());

    private CombatAPI mission;
    private List<HandCard> handCards;
    private HandCard prevFocus;

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
        AnimatedImage knight = new AnimatedImage(animation);
        knight.setSize(200, 200);
        stage.addActor(knight);

        /*knight.addAction(sequence(
                moveTo(500, 0, 0.5f, Interpolation.pow3In),
                delay(0.35f),
                moveTo(0, 0, 0.5f, Interpolation.pow3Out)
        ));*/

        handCards = new ArrayList<>();

        Card.CardBuilder cardContext1 = new Card.CardBuilder("attack", 1, TargetMode.ENEMY_SINGLE, new AttackEffect());
        Card handCard1 = cardContext1.build();
        Card handCard2 = new Card.CardBuilder("defend", 1, TargetMode.ALLY_SINGLE, new AttackEffect()).build();
        Card handCard3 = new Card.CardBuilder("summon", 1, TargetMode.ALLY_SUMMON_FRONT, new AttackEffect()).build();
        Card handCard4 = new Card.CardBuilder("heal", 1, TargetMode.ALLY_SINGLE, new AttackEffect()).build();
        Card handCard5 = new Card.CardBuilder("recuperate", 1, TargetMode.PLAYER, new AttackEffect()).build();
        addHandCards(handCard1, handCard2, handCard3, handCard4, handCard5);

        super.addInputProcessor(0, new CardSelectorResetListener());

        mission.setDeck(handCards.stream().map(HandCard::getCard).toList());
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

        public CardSelectorListener(HandCard handCard, CombatAPI mission) {
            this.handCard = handCard;
            this.mission = mission;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (getPreviousFocus() == handCard) {
                try {
                    mission.playCard(handCard.getCard());
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
        updateHandCardPositions();
    }

    private void addHandCard(Card card) {
        TextureRegion region = new TextureRegion(new Texture(card.getPixmap()));
        region.flip(false, true);
        Image image = new Image(manager.get("parchment.png", Texture.class) /*region*/);
        image.setSize(CARD_WIDTH, CARD_HEIGHT);
        HandCard handCard = new HandCard(card, image);
        image.addListener(new CardSelectorListener(handCard, mission));
        uiStage.addActor(image);
        handCards.add(handCard);

        updateHandCardPositions();
    }

    @Override
    protected void update(float delta) {
        float scaleInitial = 1f, scale = 1.2f, duration = 0.15f;
        final int horizontalDiff = (int) ((CARD_WIDTH * scale) - (CARD_WIDTH * scaleInitial));
        final Interpolation interpolation = Interpolation.pow2;

        if (CardSelectorListener.getFocus() == null) {
            if (prevFocus != null) {
                prevFocus.getImage().addAction(parallel(
                        moveBy(horizontalDiff, DECK_Y, duration, interpolation),
                        scaleTo(scaleInitial, scaleInitial, duration, interpolation)));
                System.out.println("added return effect");
                for (int i = 0; i < handCards.size(); i++) handCards.get(i).getImage().setZIndex(i);
                prevFocus = null;
            }
        } else {
            for (int i = 0; i < handCards.size(); i++) {
                HandCard handCard = handCards.get(i);

                if (handCard != prevFocus && handCard == CardSelectorListener.getFocus()) {
                    handCard.getImage().addAction(parallel(
                            moveBy(-horizontalDiff, -DECK_Y, duration, interpolation),
                            scaleTo(scale, scale, duration, interpolation)));
                    handCard.getImage().setZIndex(TOP_CARD_INDEX);
                    if (prevFocus != null) {
                        prevFocus.getImage().addAction(parallel(
                                moveBy(horizontalDiff, DECK_Y, duration, interpolation),
                                scaleTo(scaleInitial, scaleInitial, duration, interpolation)));
                    }
                    prevFocus = handCard;
                }

                if (handCard != CardSelectorListener.getFocus()) {
                    handCard.getImage().setZIndex(i);
                } else {
                    handCard.getImage().setZIndex(TOP_CARD_INDEX);
                }
            }
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
