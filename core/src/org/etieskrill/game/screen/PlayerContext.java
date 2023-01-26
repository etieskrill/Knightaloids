package org.etieskrill.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.etieskrill.game.core.effect.StatusEffect;
import org.etieskrill.game.core.entity.AlliedEntity;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.core.entity.Player;
import org.etieskrill.game.util.AnimatedImage;
import org.etieskrill.game.util.ResourceManager;

public class PlayerContext extends VerticalGroup {

    private final Player player;
    private final String id;
    private final String name;

    private final Label nameLabel;
    private final Label healthLabel;
    private final Label manaLabel;

    private PlayerContext(Player player, String id, String name, ResourceManager manager) {
        this.player = player;
        this.id = id;
        this.name = name;

        Label.LabelStyle style = new Label.LabelStyle(manager.getFont(), null);

        nameLabel = new Label(name, style);
        healthLabel = new Label("0/0", style);
        manaLabel = new Label("0/0", style);

        addActor(nameLabel);
        addActor(healthLabel);
        addActor(manaLabel);
    }

    @Override
    public void act(float delta) {
        update();
        super.act(delta);
    }

    private void update() {
        nameLabel.setText(name.length() != 0 ? player.getDisplayName() : player.getName());
        healthLabel.setText(player.getHealth() <= 0 ? "dead" :
                String.format("Health: %d / %d", (int) player.getHealth(), (int) player.getBaseHealth()));
        manaLabel.setText("Mana:" + player.getMana() + "/" + player.getBaseMana());
    }

    public static class PlayerContextFactory {

        private static int idCounter;

        private final ResourceManager manager;

        public PlayerContextFactory(ResourceManager manager) {
            this.manager = manager;
        }

        public PlayerContext getFor(Player player) {
            return new PlayerContext(player, player.getName() + idCounter++, player.getDisplayName(), manager);
        }
    }

}
