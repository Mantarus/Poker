package com.mantarus.poker;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() {
        this(new ArrayList<>());
    }

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Hand addCard(Card card) {
        cards.add(card);
        return this;
    }

    public void reset() {
        cards = new ArrayList<>();
    }
}
