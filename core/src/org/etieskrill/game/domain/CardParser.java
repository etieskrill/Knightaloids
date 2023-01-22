package org.etieskrill.game.domain;

import com.badlogic.gdx.files.FileHandle;
import org.etieskrill.game.core.card.Card;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

public interface CardParser {

    StringWriter to(Collection<Card> cards);

    Collection<Card> from(InputStream input);

    Collection<Card> from(FileHandle file);

}
