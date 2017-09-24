package com.mantarus.poker.exceptions;

public class UnallowedActionException extends PokerException {

    public UnallowedActionException(String message) {
        super(message);
    }

    public UnallowedActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
