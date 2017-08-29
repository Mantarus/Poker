package main.java.com.mantarus.poker.exceptions;

public class PokerException extends RuntimeException {

    public PokerException(String message) {
        super(message);
    }

    public PokerException(String message, Throwable cause) {
        super(message, cause);
    }
}
