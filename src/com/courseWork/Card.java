package com.courseWork;

public class Card {
    private CardSuit suit;
    private CardValue value;

    public Card(CardSuit suit, CardValue value) {
        this.suit = suit;
        this.value = value;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardValue getValue() {
        return value;
    }

    public boolean isGreaterThan(Card card) {
        return this.getValue().getValue() > card.getValue().getValue();
    }

    public boolean isEqualTo(Card card) {
        return this.getValue().getValue() == card.getValue().getValue();
    }

    @Override
    public String toString() {
        return suit.getRepresentation() + value.getRepresentation();
    }
}
