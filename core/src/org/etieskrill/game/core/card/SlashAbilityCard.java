package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.AttackEffect;

import java.util.List;

public class SlashAbilityCard extends AbilityCard {

    public SlashAbilityCard() {
        super("base_slash", 1, List.of(
                new AttackEffect(6)
        ), TargetMode.ENEMY_SINGLE);
        setTitle("Slash");
    }

}
