package org.etieskrill.game.domain;

public class CardParserFactory {

    public XMLCardParser getXMLParser() {
        return new XMLCardParser();
    }

}
