package org.etieskrill.game.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.etieskrill.game.core.effect.StatusEffect;
import org.etieskrill.game.core.entity.*;
import org.etieskrill.game.util.AnimatedImage;
import org.etieskrill.game.util.ResourceManager;

import java.util.logging.Logger;

public class EntityContext extends HorizontalGroup {

    public static final int ENTITY_WIDTH = 150, ENTITY_HEIGHT = (int) (ENTITY_WIDTH * 1.5f);
    public static final int CONTEXT_WIDTH = 150, CONTEXT_HEIGHT = (int) (CONTEXT_WIDTH * 1.5f);
    public static final float DEFAULT_SCALE = 3f;

    private static final Logger logger = Logger.getLogger(EntityContext.class.getSimpleName());

    private final Skin skin;
    private final Label.LabelStyle smallStyle;

    private final String id;
    private final String name;
    private final Entity entity;
    private final AnimatedImage stageActor;
    private final WidgetGroup stageActorContainer;

    private final Label nameLabel;
    private final ProgressBar healthBar;
    private final Label healthLabel;
    private final Label blockLabel;
    private final Label manaLabel;
    private final HorizontalGroup statusEffects;

    private EntityContext(String id, String name, Entity entity, AnimatedImage stageActor, ResourceManager manager) {
        this.skin = manager.get("skins/default/skin/uiskin.json", Skin.class);
        smallStyle = new Label.LabelStyle(manager.getSmallFont(), null);
        Label.LabelStyle mediumStyle = new Label.LabelStyle(manager.getFont(), null);

        this.id = id;
        this.name = name;
        this.entity = entity;
        this.stageActor = stageActor;
        this.stageActor.setScale(DEFAULT_SCALE);
        this.stageActorContainer = new WidgetGroup(stageActor);
        this.stageActor.setPosition(-stageActor.getWidth(), -stageActor.getHeight());
        this.nameLabel = new Label(name, smallStyle);

        Pixmap pixmap = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        pixmap = new Pixmap(0, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        pixmap = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        this.healthBar = new ProgressBar(0, entity.getBaseHealth(), 1f, false, progressBarStyle);
        this.healthLabel = new Label("0/0", smallStyle);
        this.blockLabel = new Label("B:0", smallStyle);

        Table table = new Table(skin);

        table.add(nameLabel).center();
        table.row();
        table.add(stageActorContainer)
                .center()
                .size(ENTITY_WIDTH, ENTITY_HEIGHT);
        table.row();
        HorizontalGroup mainStats = new HorizontalGroup();
        if (entity instanceof AlliedEntity) {
            this.manaLabel = new Label("M:0", smallStyle);
            mainStats.addActor(this.manaLabel);
        } else this.manaLabel = null;
        Stack stack = new Stack();
        stack.add(healthBar);
        stack.add(healthLabel);
        healthLabel.setPosition(0, stack.getWidth()); //TODO whatever, somehow center this on the health bar
        mainStats.addActor(stack);
        mainStats.addActor(blockLabel);
        mainStats.space(5f);
        table.add(mainStats).center();

        statusEffects = new HorizontalGroup();
        statusEffects.setHeight(30);
        table.row();
        table.add(statusEffects).center();

        setSize(CONTEXT_WIDTH, CONTEXT_HEIGHT);
        setTouchable(Touchable.enabled);

        pad(10f);
        addActor(table);
    }

    @Override
    public void act(float delta) {
        update();
        super.act(delta);
    }

    private void update() {
        nameLabel.setText(name.length() != 0 ? name : id);
        healthBar.setValue(entity.getHealth());
        healthLabel.setText(entity.getHealth() <= 0 ? "dead" :
                String.format("%d / %d", (int) entity.getHealth(), (int) entity.getBaseHealth()));
        if (manaLabel != null) manaLabel.setText("M:" + ((AlliedEntity) entity).getMana());
        blockLabel.setText("B:" + (int) entity.getBlock());
        statusEffects.clearChildren();
        for (StatusEffect effect : entity.getStatusEffects()) {
            statusEffects.addActor(new Label(effect.getAbbreviation() + ":" + effect.getStacks(), smallStyle));
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }

    public AnimatedImage getStageActor() {
        return stageActor;
    }

    public static class EntityContextFactory {

        private static int idCounter;

        private final ResourceManager manager;

        public EntityContextFactory(ResourceManager manager) {
            this.manager = manager;
        }

        public EntityContext getFor(Entity entity) {
            AnimatedImage actor;
            try {
                actor = getResourceForEntity(entity);
            } catch (GdxRuntimeException | UnsupportedOperationException e) {
                logger.warning(e.getMessage() + " Error loading texture: using default");
                actor = AnimatedImage.fromPackedTexture(manager.get("entity/entity_sprite_error.png", Texture.class), 1, 1);
            }
            actor.setTouchable(Touchable.childrenOnly);

            return new EntityContext(entity.getName() + idCounter++, entity.getDisplayName(), entity, actor,
                    manager);
        }

        private AnimatedImage getResourceForEntity(Entity entity) {
            if (entity instanceof Goblin) {
                return AnimatedImage.fromPackedTexture(manager.get("entity/enemy/goblin/idle.png", Texture.class), 4, 1);
            } else if (entity instanceof Skeleton) {
                return AnimatedImage.fromPackedTexture(manager.get("entity/enemy/skeleton/idle.png", Texture.class), 4, 1);
            } else if (entity instanceof Knight) {
                return AnimatedImage.fromPackedTexture(manager.get("entity/ally/knight/idle.png", Texture.class), 2, 1);
            } else {
                throw new UnsupportedOperationException("unknown entity type");
            }
        }

    }

}
