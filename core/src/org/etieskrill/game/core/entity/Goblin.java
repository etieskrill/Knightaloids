package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.card.AbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.effect.AttackEffect;
import org.etieskrill.game.core.effect.Effect;

import java.util.ArrayList;
import java.util.List;

public class Goblin extends EnemyEntity {

    private static final List<Card> goblinMoveset;

    static {
        goblinMoveset = new ArrayList<>();
        goblinMoveset.add(new AbilityCard("goblin_attack_weak", 0, List.of(new AttackEffect(5)), Card.TargetMode.ALLY_FRONT) {});
        goblinMoveset.add(new AbilityCard("goblin_attack_weak", 0, List.of(new AttackEffect(5)), Card.TargetMode.ALLY_FRONT) {});
        goblinMoveset.add(new AbilityCard("goblin_attack_strong", 0, List.of(new AttackEffect(15)), Card.TargetMode.ALLY_FRONT) {});
    }

    public Goblin() {
        super(goblinMoveset, 25, new ArrayList<>());
    }

    @Override
    public String getName() {
        return "goblin_melee_weak";
    }

    @Override
    public String getDisplayName() {
        return "Goblin";
    }

}
