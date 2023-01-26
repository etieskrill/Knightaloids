package org.etieskrill.game.core.card;

import org.etieskrill.game.core.effect.BlockEffect;

import java.util.List;

public class BlockAbilityCard extends AbilityCard {

    public BlockAbilityCard() {
        super("base_block", 1, List.of(
                new BlockEffect(6)), TargetMode.CASTER);
        setTitle("Block");
    }

}
