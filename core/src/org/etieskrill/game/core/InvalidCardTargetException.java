package org.etieskrill.game.core;

public class InvalidCardTargetException extends RuntimeException {

    public InvalidCardTargetException() {
        super("card target mode is not applicable to focused entity");
    }

    public InvalidCardTargetException(String message) {
        super(message);
    }

}
