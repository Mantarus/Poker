package com.mantarus.poker.cards;

public enum CardSuit {
    HEARTS("♥"),
    SPADES("♠"),
    DIAMONDS("♦"),
    CLUBS("♣");

    private final String representation;

    CardSuit(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}
