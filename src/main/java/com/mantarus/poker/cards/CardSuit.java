package com.mantarus.poker.cards;

public enum CardSuit {
    HEARTS("♥"),
    SPADES("♠"),
    DIAMONDS("♦"),
    CLUBS("♣");

    private String representation;

    public String getRepresentation() {
        return representation;
    }

    CardSuit(String representation) {
        this.representation = representation;
    }
}
