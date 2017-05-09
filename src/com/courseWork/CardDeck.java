package com.courseWork;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {

    private List<Card> cards;

    public CardDeck() {
        cards = new LinkedList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    public Card pop() {
        return cards.remove(0);
    }

    public Boolean isEmpty() {
        return cards.isEmpty();
    }
}
