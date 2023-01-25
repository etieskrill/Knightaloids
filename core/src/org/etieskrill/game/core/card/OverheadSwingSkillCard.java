package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.AttackEffect;
import org.etieskrill.game.core.effect.BleedingStatusEffect;
import org.etieskrill.game.core.effect.Effect;
import org.etieskrill.game.core.entity.Entity;

import java.util.List;

public class OverheadSwingSkillCard extends SkillCard {

    public OverheadSwingSkillCard(Entity owner) {
        super("base_overhead_swing", 2,
                List.of(new AttackEffect(12), new BleedingStatusEffect()),
                TargetMode.ENEMY_SINGLE, owner);
    }

}
