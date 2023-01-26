package org.etieskrill.game.core.card;

import com.badlogic.gdx.graphics.Pixmap;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.util.CardTextureAssembler;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public abstract class Card {

    public enum TargetMode {
        ALLY_FRONT,
        ALLY_BACK,
        ALLY_RANDOM,
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
                EnumSet.of(ALLY_ALL, ENEMY_SINGLE,
                ENEMY_ALL, ALLY_SUMMON_FRONT, ALLY_SUMMON_BEHIND,
                ENEMY_SUMMON_FRONT, ENEMY_SUMMON_BEHIND);

        private static final EnumSet<TargetMode> allyTargetModes =
                EnumSet.of(ALLY_FRONT, ALLY_BACK, ALLY_RANDOM, ALLY_SUMMON_FRONT, ALLY_SUMMON_BEHIND);

        private static final EnumSet<TargetMode> enemyTargetModes =
                EnumSet.of(TargetMode.ENEMY_SINGLE, TargetMode.ENEMY_ALL,
                        TargetMode.ENEMY_SUMMON_FRONT, TargetMode.ENEMY_SUMMON_BEHIND);

        private static final EnumSet<TargetMode> casterTargetModes =
                EnumSet.of(ALLY_SUMMON_FRONT, ALLY_SUMMON_BEHIND, ENEMY_SUMMON_FRONT, ENEMY_SUMMON_BEHIND, CASTER);

        public static EnumSet<TargetMode> getEntityTargetModes() {
            return entityTargetModes.clone(); //TODO is there a way to make immutable?
        }

        public static EnumSet<TargetMode> getAllyTargetModes() {
            return allyTargetModes.clone();
        }

        public static EnumSet<TargetMode> getEnemyTargetModes() {
            return enemyTargetModes.clone();
        }

        public static EnumSet<TargetMode> getCasterTargetModes() {
            return casterTargetModes.clone();
        }
    }

    //TODO separate card data from visual context
    private final String id;
    private String backgroundNinePatch;
    private String bannerTexture;
    private String title;
    private final int cost;
    private final List<Effect> effects;
    private CardDescription description;
    private boolean hasFlavourText;
    private CardDescription flavourText;
    private final TargetMode targetMode;
    private Pixmap pixmap;

    protected Card(String id, String backgroundNinePatch, String bannerTexture, String title, int cost,
                   List<Effect> effects, CardDescription description, boolean hasFlavourText,
                   CardDescription flavourText, TargetMode targetMode) {
        this.id = id;
        this.backgroundNinePatch = backgroundNinePatch;
        this.bannerTexture = bannerTexture;
        this.title = title;
        this.cost = cost;
        this.effects = effects;
        this.description = description;
        this.hasFlavourText = hasFlavourText;
        this.flavourText = flavourText;
        this.targetMode = targetMode;
    }

    public Card(String id, int cost, List<Effect> effects, TargetMode targetMode) {
        this(id, "card/cardBaseBackground.9.png", "card/" + id + ".png", id, cost,
                effects, null, false, null, targetMode);
    }

    public String getId() {
        return id;
    }

    public String getBackgroundNinePatch() {
        return backgroundNinePatch;
    }

    public void setBackgroundNinePatch(String backgroundNinePatch) {
        this.backgroundNinePatch = backgroundNinePatch;
    }

    public String getBannerTexture() {
        return bannerTexture;
    }

    public void setBannerTexture(String bannerTexture) {
        this.bannerTexture = bannerTexture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCost() {
        return cost;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public CardDescription getDescription() {
        return description;
    }

    public void setDescription(CardDescription description) {
        this.description = description;
    }

    public boolean hasFlavourText() {
        return hasFlavourText;
    }

    public void setHasFlavourText(boolean hasFlavourText) {
        this.hasFlavourText = hasFlavourText;
    }

    public CardDescription getFlavourText() {
        if (!hasFlavourText) throw new UnsupportedOperationException("card has no flavour text");

        return flavourText;
    }

    public void setFlavourText(CardDescription flavourText) {
        this.flavourText = flavourText;
        this.hasFlavourText = true;
    }

    public Pixmap getPixmap() {
        if (pixmap == null) {
            CardTextureAssembler assembler = CardTextureAssembler.getInstance();
            this.pixmap = assembler.get(this);
        }

        return pixmap;
    }

    public TargetMode getTargetMode() {
        return targetMode;
    }

}
