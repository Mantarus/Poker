package com.mantarus.poker.player;

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
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
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
