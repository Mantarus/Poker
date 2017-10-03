package com.mantarus.poker;

import com.mantarus.poker.exceptions.PokerException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TexasHoldemBoard {

    public final static int MIN_PLAYERS = 2;
    public final static int MAX_PLAYERS = 6;

    public final static int NUMBER_OF_PLAYER_CARDS = 2;
    public final static int NUMBER_OF_COMMUNITY_CARDS = 5;

    private CardDeck deck = new CardDeck();
    private PlayersQueue players;
    //TODO: Do smth to make the dealer useful
    private List<Card> communityCards;
    private int bank;
    private BoardInfo boardInfo = new BoardInfo();
    private boolean alive;

    public void initiateGame(int playersCount, int initialBalance) {
        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < playersCount; i++) {
            if (playerList.size() == MAX_PLAYERS) {
                throw new PokerException("Too many players");
            }
            Player player = new Player(initialBalance);
            playerList.add(player);
        }
        players = new PlayersQueue(playerList);
        updateIsAlive();
    }

    public TexasHoldemBoard leave(Player player) {
        players.remove(player);
        updateIsAlive();
        return this;
    }

    public void kickLosers() {
        for (Player player : players.asList()) {
            if (player.getBalance() <= 0) {
                leave(player);
                System.out.println(String.format("%s left the game", player.getName()));
            }
        }
    }

    public List<Player> getWinners() {
        List<Player> winners = new ArrayList<>();

        for (Player player : players.asList()) {
            if (player.isFolded()) {
                continue;
            }
            if (winners.isEmpty()) {
                winners.add(player);
                continue;
            }
            Player other = winners.get(0);
            Integer comparingResult = RankingUtil.combinationComparator.compare(player, other);
            if (comparingResult > 0) {
                winners.clear();
            } if (comparingResult >= 0) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     * Deal cards to players before the game
     */
    public void dealToPlayers() {
        IntStream.range(0, NUMBER_OF_PLAYER_CARDS)
                .forEach(i -> players.asList().forEach(p -> p.getHand().addCard(deck.pop())));
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
        for (Player player : players.asList()) {
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
        deck = new CardDeck();
        players.asList().forEach(player -> player.getHand().reset());
        communityCards = new ArrayList<>();
        bank = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    private void updateIsAlive() {
        alive = players.size() >= MIN_PLAYERS;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public PlayersQueue getPlayers() {
        return players;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
        System.out.println(String.format("Bank is %d now", bank));
    }

    public void clearBank() {
        bank = 0;
    }

    BoardInfo getBoardInfo() {
        return boardInfo;
    }

    public class BoardInfo {
        private Integer currentStake = 0;

        public Integer getCurrentStake() {
            return currentStake;
        }

        public void setCurrentStake(Integer currentStake) {
            this.currentStake = currentStake;
        }
    }
}
