package org.etieskrill.game.screen;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.etieskrill.game.core.entity.Entity;
import org.etieskrill.game.util.AnimatedImage;
import org.etieskrill.game.util.ResourceManager;

public class EntityContext extends WidgetGroup {

    public static final int ENTITY_WIDTH = 200, ENTITY_HEIGHT = (int) (ENTITY_WIDTH * 1.75f);

    private final String id;
    private final String name;
    private final Entity entity;
    private final AnimatedImage stageActor;

    private final Label labelName;
    private final ProgressBar healthBar;
    private final Label healthLabel;

    private EntityContext(String id, String name, Entity entity, AnimatedImage stageActor, Skin skin) {
        this.id = id;
        this.name = name;
        this.entity = entity;
        this.stageActor = stageActor;

        this.labelName = new Label(name, skin);
        this.healthBar = new ProgressBar(0, entity.getBaseHealth(), 1f, false, skin);
        this.healthLabel = new Label("0/0", skin);

        Table table = new Table(skin);

        table.add(labelName).center();
        table.row();
        table.add(stageActor)
                .size(stageActor.getWidth() * 3.5f, stageActor.getHeight() * 3.5f)
                .center()
                .pad(-150f);
        table.row();
        Stack stack = new Stack();
        stack.add(healthBar);
        stack.add(healthLabel);
        healthLabel.setPosition(0, stack.getWidth()); //TODO whatever, somehow center this on the health bar
        table.add(stack).center();

        table.setPosition(ENTITY_WIDTH / 2f, ENTITY_HEIGHT / 2f);

        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);

        addActor(table);
    }

    @Override
    public void act(float delta) {
        update();
        super.act(delta);
    }

    private void update() {
        labelName.setText(name.length() != 0 ? name : id);
        healthBar.setValue(entity.getHealth());
        healthLabel.setText(entity.getHealth() <= 0 ? "dead" :
                String.format("%d / %d", (int) entity.getHealth(), (int) entity.getBaseHealth()));
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

        public EntityContext getFor(Entity entity, AnimatedImage stageActor) {
            return new EntityContext(entity.getName() + idCounter++, entity.getDisplayName(), entity, stageActor,
                    manager.get("skins/default/skin/uiskin.json", Skin.class));
        }

    }

}
