package org.etieskrill.game.core.effect;

import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.entity.AlliedEntity;
import org.etieskrill.game.core.entity.EnemyEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.entity.Player;

import java.util.List;

public final class EffectEvent {

    private final Card.TargetMode targetMode;
    private final Entity caster;
    private final List<AlliedEntity> allies;
    private final List<EnemyEntity> enemies;
    private final Player player;

    private List<Entity> target;

    public EffectEvent(Card.TargetMode targetMode, Entity caster, List<Entity> target, List<AlliedEntity> allies, List<EnemyEntity> enemies, Player player) {
        this.targetMode = targetMode;
        this.caster = caster;
        this.target = target;
        this.allies = allies;
        this.enemies = enemies;
        this.player = player;
    }

    public Card.TargetMode getTargetMode() {
        return targetMode;
    }

    public Entity getCaster() {
        return caster;
    }

    public List<AlliedEntity> getAllies() {
        return allies;
    }

    public List<EnemyEntity> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getTarget() {
        return target;
    }

    public void setTarget(List<Entity> target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "EffectEvent{" +
                "targetMode=" + targetMode +
                ", caster=" + caster +
                ", allies=" + allies +
                ", enemies=" + enemies +
                ", player=" + player +
                ", target=" + target +
                '}';
    }

}
