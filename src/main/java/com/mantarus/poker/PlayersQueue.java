package com.mantarus.poker;

import java.util.ArrayList;
import java.util.List;

public class PlayersQueue {
    private List<Player> players;
    private Player dealer;
    private Player current;

    public PlayersQueue(List<Player> players) {
        if (players.isEmpty()) {
            throw new AssertionError("Can't instantiate class with empty collection!");
        } if (players.size() == 1) {
            throw new AssertionError("Can't instantiate class with collection containing one element");
        }
        this.players = new ArrayList<>(players);
        this.dealer = players.get(0);
        this.current = players.get(1);
    }

    /**
     * Pass a dealer button to next player
     */
    public void rotateDealer() {
        if (players.size() == 0) {
            throw new AssertionError("Can't rotate dealer in an empty collection!");
        }
        dealer = players.get((players.indexOf(dealer) + 1) % players.size());
        System.out.println(String.format("%s is dealer now", dealer));
    }

    /**
     * Check that passed player sits next to the current dealer
     * @param player
     * @return true if player sits next to dealer
     */
    public boolean isNextToDealer(Player player) {
        if (!players.contains(player)) {
            throw new AssertionError("Player doesn't present in collection!");
        }
        return (players.indexOf(player) == (players.indexOf(dealer) + 1) % players.size());
    }

    /**
     * Pass a move to the next player
     * @return new current player
     */
    public Player getNext() {
        if (players.isEmpty()) {
            throw new AssertionError("Can't get next player from empty collection!");
        }
        current = players.get((players.indexOf(current) + 1) % players.size());
        System.out.println(String.format("Next turn by %s", current));
        return current;
    }

    /**
     * Remove player from the queue
     * @param player
     */
    public void remove(Player player) {
        if (!players.contains(player)) {
            throw new AssertionError("Player doesn't present in collection!");
        } if (players.size() == 1) {
            throw new AssertionError("Collection mustn't be empty after removing!");
        }
        if (player == dealer) {
            int idx = players.indexOf(dealer) == 0 ? players.size() - 1 : (players.indexOf(dealer) + 1) % players.size();
            dealer = players.get(idx);
        } if (player == current) {
            int idx = players.indexOf(current) == 0 ? players.size() - 1 : (players.indexOf(current) + 1) % players.size();
            current = players.get(idx);
        }
        players.remove(player);
        System.out.println(String.format("%s removed", player));
    }

    /**
     * Make player, sitting next to the dealer, current
     */
    public void reset() {
        current = players.get((players.indexOf(dealer) + 1) % players.size());
    }

    /**
     * @return copy of players list
     */
    public List<Player> asList() {
        return new ArrayList<>(players);
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getCurrent() {
        return current;
    }

    public int size() {
        return players.size();
    }
}
