package com.mantarus.poker;

import com.mantarus.poker.exceptions.PokerException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CardDeck {

    public final static int NUMBER_OF_PLAYER_CARDS = 2;
    public final static int NUMBER_OF_COMMUNITY_CARDS = 3;

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

    public void dealToPlayers(List<Player> players) {
        IntStream.range(0, NUMBER_OF_PLAYER_CARDS)
                .forEach(i -> players.forEach(p -> p.getHand().addCard(pop())));
    }

    public void dealToCommunity(List<Card> community) {
        IntStream.range(0, NUMBER_OF_COMMUNITY_CARDS)
                .forEach(i -> community.add(pop()));
    }

    public CardDeck push(Card card) {
        cards.add(card);
        return this;
    }

    public Boolean isEmpty() {
        return cards.isEmpty();
    }
}
