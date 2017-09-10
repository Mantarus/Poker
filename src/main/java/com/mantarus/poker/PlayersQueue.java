package com.mantarus.poker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class PlayersQueue implements Iterable<Player>{

    private List<Player> players;
    private Player current;
    private Player dealer;

    public PlayersQueue() {
        players = new ArrayList<>();
    }

    public PlayersQueue(List<Player> players) {
        this.players = players;
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Argument can't be empty");
        }
        current = this.players.get(0);
        dealer = current;
    }

    public List<Player> getAsList() {
        return new ArrayList<>(players);
    }

    public int size() {
        return players.size();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void add(Player p) {
        if (players.add(p)) {
            if (players.isEmpty()) {
                current = p;
                dealer = current;
            }
        }
    }

    public void add(int idx, Player p) {
        players.add(idx, p);
        if (players.isEmpty()) {
            current = p;
            dealer = current;
        }
    }

    public boolean remove(Player p) {
        if (p == current || p == dealer) {
            Player next = next();
            if (p == current) {
                current = next;
            }
            if (p == dealer) {
                dealer = next;
            }
        }
        return players.remove(p);
    }

    public boolean remove(int idx) {
        return remove(players.get(idx));
    }

    public Player current() {
        return current;
    }

    public Player next() {
        if (players.isEmpty()) {
            return null;
        }
        int nextIdx = (players.indexOf(current) + 1) % players.size();
        current = players.get(nextIdx);
        return current;
    }

    public Player rotateDealer() {
        if (players.isEmpty()) {
            return null;
        }
        int nextIdx = (players.indexOf(dealer) + 1) % players.size();
        dealer = players.get(nextIdx);
        return dealer;
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    @Override
    public void forEach(Consumer<? super Player> action) {
        players.forEach(action);
    }

    @Override
    public Spliterator<Player> spliterator() {
        return players.spliterator();
    }
}
