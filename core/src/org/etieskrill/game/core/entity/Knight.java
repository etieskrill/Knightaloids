package org.etieskrill.game.core.entity;

import org.etieskrill.game.core.CombatAPI;
import org.etieskrill.game.core.card.OverheadSwingSkillCard;

import java.util.ArrayList;

public class Knight extends AlliedEntity {

    public Knight() {
        super(new ArrayList<>(), 3, 100, new ArrayList<>());
        moveSet.add(new OverheadSwingSkillCard(this));
        moveSet.add(new OverheadSwingSkillCard(this));
    }

    @Override
    public String getName() {
        return "knight";
    }

    @Override
    public String getDisplayName() {
        return "Knight";
    }

}
