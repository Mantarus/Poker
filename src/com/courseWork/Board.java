package com.courseWork;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Board {

    private List<Player> players;
    private CardDeck deck;
    private List<Card> communityCards;
    private Integer bank;

    public Board(Integer playerCount, Integer balance) {
        if (playerCount < 2 || playerCount > 22) {
            throw new IllegalArgumentException("Invalid number of players!");
        }

        deck = new CardDeck();

        players = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.setBalance(balance);
            players.add(player);
        }
    }

    //Deal cards before the game
    public void deal() {
        players.forEach(player -> IntStream.range(0, 2).forEach(i -> player.getHand().getCards().add(deck.pop())));
        IntStream.range(0, 3).forEach(i -> communityCards.add(deck.pop()));
    }

    //Reset cards after the game
    public void reset() {
        players.forEach(player -> player.setHand(new Hand(new ArrayList<>())));
        communityCards = new ArrayList<>();
        deck = new CardDeck();
    }


}
