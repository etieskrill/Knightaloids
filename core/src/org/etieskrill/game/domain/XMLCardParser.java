package org.etieskrill.game.domain;

import com.badlogic.gdx.files.FileHandle;
import org.etieskrill.game.core.card.Card;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

public class XMLCardParser implements CardParser {

    protected XMLCardParser() {}

    @Override
    public StringWriter to(Collection<Card> cards) {
        return null;
    }

    @Override
    public Collection<Card> from(InputStream input) {
        return null;
    }

    @Override
    public Collection<Card> from(FileHandle file) {
        return null;
    }

}
