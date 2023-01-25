package org.etieskrill.game.screen.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class HitAction {

    public static final Action hitAction = parallel(
            moveBy(100, 10, 1.5f, Interpolation.elastic)
    );

}
