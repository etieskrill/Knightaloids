package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.AbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.AttackEffect;
import org.etieskrill.game.core.effect.BlockEffect;

import java.util.ArrayList;
import java.util.List;

public class Skeleton extends EnemyEntity {

    private static final List<Card> skeletonMoveset;

    static {
        skeletonMoveset = new ArrayList<>();
        skeletonMoveset.add(new AbilityCard("skeleton_wild_thrust_attack", 0, List.of(new AttackEffect(5)), Card.TargetMode.ALLY_RANDOM) {});
        skeletonMoveset.add(new AbilityCard("skeleton_block", 0, List.of(new BlockEffect(10)), Card.TargetMode.CASTER) {});
        skeletonMoveset.add(new AbilityCard("skeleton_reach_attack", 0, List.of(new AttackEffect(8)), Card.TargetMode.ALLY_BACK) {});
    }

    public Skeleton() {
        super(skeletonMoveset, 40, new ArrayList<>());
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
