package org.etieskrill.game.core;

import java.util.Arrays;
import java.util.List;

public class Card {

    private final String id;
    private final String backgroundNinePatch;
    private final String bannerTexture;
    private final String title;
    private final int cost;
    private final List<Effect> effects;
    private final CardDescription decription;
    private final boolean hasFlavourText;
    private final CardDescription flavourText;

    private Card(CardBuilder builder) {
        this.id = builder.id;
        this.backgroundNinePatch = builder.backgroundNinePatch;
        this.bannerTexture = builder.bannerTexture;
        this.title = builder.title;
        this.cost = builder.cost;
        this.effects = builder.effects;
        this.decription = builder.description;
        this.hasFlavourText = builder.hasFlavourText;
        this.flavourText = builder.flavourText;
    }

    public static class CardBuilder {
        private String id;
        private String backgroundNinePatch;
        private String bannerTexture;
        private String title;
        private int cost;
        private final List<Effect> effects;
        private CardDescription description;
        private boolean hasFlavourText;
        private CardDescription flavourText;

        public CardBuilder(String id, int cost, Effect... effects) {
            this.id = id;
            this.backgroundNinePatch = "assets/cards/background.9.png";
            this.bannerTexture = "assets/cards/banners/" + id;
            this.title = id;
            this.cost = cost;
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

}
