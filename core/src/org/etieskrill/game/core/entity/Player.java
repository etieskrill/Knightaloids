package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.StatusEffect;

import java.util.List;

public class Player extends Entity {

    private int baseMana;
    private int mana;
    private String userName;

    public Player(List<Card> moveSet, int baseMana, int baseHealth, List<StatusEffect> statusEffects) {
        super(moveSet, baseHealth, statusEffects);
        this.baseMana = baseMana;
        this.mana = baseMana;
    }

    @Override
    public void damage(float damage) {
        this.health -= damage;
    }

    public int getMana() {
        return mana;
    }

    public boolean useMana(int amount) {
        if (mana - amount <= 0) return false;

        this.mana -= amount;
        return true;
    }

    @Override
    protected void doMove(int move) {
        throw new UnsupportedOperationException("player has no moveset");
    }

    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDisplayName() {
        return userName;
    }

    public void setDisplayName(String userName) {
        this.userName = userName;
    }

}
