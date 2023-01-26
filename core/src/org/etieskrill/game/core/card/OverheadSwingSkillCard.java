package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.AttackEffect;
import org.etieskrill.game.core.effect.BleedingStatusEffect;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public class OverheadSwingSkillCard extends SkillCard {

    public OverheadSwingSkillCard(Entity owner) {
        super("base_overhead_swing", 2,
                List.of(new AttackEffect(8), new BleedingStatusEffect(4)),
                TargetMode.ENEMY_SINGLE, owner);
        setTitle("Overhead Swing");
    }

}
