package com.mantarus.poker;

import com.mantarus.poker.exceptions.PokerException;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public final static int MIN_PLAYERS = 2;
    public final static int MAX_PLAYERS = 22;

    private List<Player> players;
    private List<Card> communityCards;
    private Integer bank;
    private boolean alive;

    public Board() {
        players = new ArrayList<>();
    }

    public Board sit(Player player) {
        if (players.size() == MAX_PLAYERS) {
            throw new PokerException("Too many players");
        }

        players.add(player);
        updateIsAlive();
        return this;
    }

    public Board leave(Player player) {
        players.remove(player);
        updateIsAlive();
        return this;
    }

    /**
     * Deal cards before the game
     */
    public void deal() {
        CardDeck deck = new CardDeck();
        deck.dealToPlayers(players);
        deck.dealToCommunity(communityCards);
    }

    public void incrementBalance(Player player, int increment) {
        player.setBalance(player.getBalance() + increment);
    }

    /**
     * Reset cards after the game
     */
    public void reset() {
        players.forEach(player -> player.getHand().reset());
        communityCards = new ArrayList<>();
        bank = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    private void updateIsAlive() {
        alive = players.size() >= MIN_PLAYERS;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Integer getBank() {
        return bank;
    }
}
