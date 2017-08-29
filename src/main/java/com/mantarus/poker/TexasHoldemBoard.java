package main.java.com.mantarus.poker;

import main.java.com.mantarus.poker.exceptions.PokerException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TexasHoldemBoard {

    public final static int MIN_PLAYERS = 2;
    public final static int MAX_PLAYERS = 6;

    public final static int NUMBER_OF_PLAYER_CARDS = 2;
    public final static int NUMBER_OF_COMMUNITY_CARDS = 5;

    private CardDeck deck = new CardDeck();
    private List<Player> players = new ArrayList<>();
    private List<Card> communityCards;
    private Integer bank;
    private boolean alive;

    public void sit(Player player) {
        if (players.size() == MAX_PLAYERS) {
            throw new PokerException("Too many players");
        }

        players.add(player);
        updateIsAlive();
    }

    public TexasHoldemBoard leave(Player player) {
        players.remove(player);
        updateIsAlive();
        return this;
    }

    /**
     * Deal cards to players before the game
     */
    public void dealToPlayers() {
        IntStream.range(0, NUMBER_OF_PLAYER_CARDS)
                .forEach(i -> players.forEach(p -> p.getHand().addCard(deck.pop())));
    }

    /**
     * Deal cards to community cards
     * @param amount amount of dealing cards
     */
    public void dealToCommunity(int amount) {
        //TODO: Exception checking should be moved to Board
        if (amount < 0) {
            throw new IllegalArgumentException("Amount of cards can be only a positive value");
        }
        if (amount + communityCards.size() > NUMBER_OF_COMMUNITY_CARDS) {
            throw new IllegalArgumentException("Amount of community cards after dealing can't be more than maximum value");
        }
        IntStream.range(0, amount)
                .forEach(i -> communityCards.add(deck.pop()));
    }

    public void burnCard() {
        deck.pop();
    }

    public void recalculateCombinations() {
        for (Player player : players) {
            RankingUtil.checkRanking(player, communityCards);
        }
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
