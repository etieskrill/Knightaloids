package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.AbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.AttackEffect;

import java.util.ArrayList;
import java.util.List;

public class Skeleton extends EnemyEntity {

    private static final List<Card> skeletonMoveset;

    static {
        skeletonMoveset = new ArrayList<>();
        skeletonMoveset.add(new AbilityCard("goblin_attack_weak", 0, List.of(new AttackEffect(5)), Card.TargetMode.ALLY_SINGLE) {});
    }

    public Skeleton() {
        super(skeletonMoveset, 55, new ArrayList<>());
    }

    @Override
    protected void doMove(int move) {

    }

    @Override
    public String getName() {
        return "skeleton_melee_weak";
    }

    @Override
    public String getDisplayName() {
        return "Skeleton";
    }

}
