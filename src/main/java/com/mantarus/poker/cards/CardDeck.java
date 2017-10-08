package com.mantarus.poker.cards;

import com.mantarus.poker.exceptions.PokerException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//TODO: Should be moved to Board (Should be?)
public class CardDeck {

    private List<Card> cards;

    public CardDeck() {
        this(true);
    }

    public CardDeck(boolean shuffle) {
        cards = new LinkedList<>();

        // Filling
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank value : CardRank.values()) {
                cards.add(new Card(suit, value));
            }
        }

        // Shuffling
        if (shuffle) {
            Collections.shuffle(cards);
        }
    }

    public Card pop() {
        if (cards.isEmpty()) {
            throw new PokerException("Can't pop a card from the empty deck");
        }
        return cards.remove(0);
    }

}
