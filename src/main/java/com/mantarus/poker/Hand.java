package com.mantarus.poker;

import com.mantarus.poker.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains player cards
 */
public class Hand {

    private List<Card> cards;

    public Hand() {
        this(new ArrayList<>());
    }

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public Hand(Hand hand) {
        this(new ArrayList<>(hand.getCards()));
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

    @Override
    public String toString() {
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" "));
    }
}
