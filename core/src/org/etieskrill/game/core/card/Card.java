package org.etieskrill.game.core.card;

import com.badlogic.gdx.graphics.Pixmap;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.util.CardTextureAssembler;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class Card {

    /*private enum CardType { //have inheritance solve this distinction
        ABILITY,
        POWER
    }*/

    public enum TargetMode {
        ALLY_SINGLE,
        ALLY_ALL,
        ENEMY_SINGLE,
        ENEMY_ALL,
        ALLY_SUMMON_FRONT,
        ALLY_SUMMON_BEHIND,
        ENEMY_SUMMON_FRONT,
        ENEMY_SUMMON_BEHIND,
        PLAYER,
        CASTER;

        private static final EnumSet<TargetMode> entityTargetModes =
                EnumSet.of(ALLY_SINGLE, ALLY_ALL, ENEMY_SINGLE,
                ENEMY_ALL, ALLY_SUMMON_FRONT, ALLY_SUMMON_BEHIND,
                ENEMY_SUMMON_FRONT, ENEMY_SUMMON_BEHIND);

        public static EnumSet<TargetMode> getEntityTargetModes() {
            return entityTargetModes.clone(); //TODO is there a way to make immutable?
        }
    }

    //TODO separate card data from visual context
    private final String id;
    private final String backgroundNinePatch;
    private final String bannerTexture;
    private final String title;
    private final int cost;
    private final int power;
    private final int defense;
    private final List<Effect> effects;
    private final CardDescription description;
    private final boolean hasFlavourText;
    private final CardDescription flavourText;
    private final Pixmap pixmap;
    private final TargetMode targetMode;

    private Card(CardBuilder builder) {
        this.id = builder.id;
        this.backgroundNinePatch = builder.backgroundNinePatch;
        this.bannerTexture = builder.bannerTexture;
        this.title = builder.title;
        this.cost = builder.cost;
        this.power = builder.power;
        this.defense = builder.defense;
        this.effects = builder.effects;
        this.description = builder.description;
        this.hasFlavourText = builder.hasFlavourText;
        this.flavourText = builder.flavourText;

        CardTextureAssembler assembler = CardTextureAssembler.getInstance();
        this.pixmap = assembler.get(this);

        this.targetMode = builder.targetMode;
    }

    public static class CardBuilder {
        private String id;
        private String backgroundNinePatch;
        private String bannerTexture;
        private String title;
        private int cost;
        private int power;
        private int defense;
        private final List<Effect> effects;
        private CardDescription description;
        private boolean hasFlavourText;
        private CardDescription flavourText;
        private final TargetMode targetMode;

        public CardBuilder(String id, int cost, TargetMode targetMode, Effect... effects) {
            this.id = id;
            this.backgroundNinePatch = "assets/cards/background.9.png";
            this.bannerTexture = "assets/cards/banners/" + id;
            this.title = id;
            this.cost = cost;
            this.targetMode = targetMode;
            this.effects = Arrays.asList(effects);
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setBackgroundNinePatch(String backgroundNinePatch) {
            this.backgroundNinePatch = backgroundNinePatch;
        }

        public void setBannerTexture(String bannerTexture) {
            this.bannerTexture = bannerTexture;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public void setDefense(int defense) {
            this.defense = defense;
        }

        public void setDescription(CardDescription description) {
            this.description = description;
        }

        public void setFlavourText(CardDescription flavourText) {
            this.hasFlavourText = true;
            this.flavourText = flavourText;
        }

        public Card build() {
            return new Card(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getBackgroundNinePatch() {
        return backgroundNinePatch;
    }

    public String getBannerTexture() {
        return bannerTexture;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public int getPower() {
        return power;
    }

    public int getDefense() {
        return defense;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public CardDescription getDescription() {
        return description;
    }

    public boolean hasFlavourText() {
        return hasFlavourText;
    }

    public CardDescription getFlavourText() {
        return flavourText;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public TargetMode getTargetMode() {
        return targetMode;
    }

}
