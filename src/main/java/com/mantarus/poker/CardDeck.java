package main.java.com.mantarus.poker;

import main.java.com.mantarus.poker.exceptions.PokerException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CardDeck {

    //TODO: Should be moved to Board


    private List<Card> cards;

    public CardDeck() {
        this(true);
    }

    public CardDeck(boolean shuffle) {
        cards = new LinkedList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank value : CardRank.values()) {
                cards.add(new Card(suit, value));
            }
        }

        if (shuffle) {
            shuffle();
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card pop() {
        if (isEmpty()) {
            throw new PokerException("Can't pop a card from the empty deck");
        }
        return cards.remove(0);
    }

    public CardDeck push(Card card) {
        cards.add(card);
        return this;
    }

    public Boolean isEmpty() {
        return cards.isEmpty();
    }
}
