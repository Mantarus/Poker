package com.mantarus.poker;

import com.mantarus.poker.cards.Card;
import com.mantarus.poker.cards.CardDeck;
import com.mantarus.poker.exception.PokerException;
import com.mantarus.poker.info.BoardInfo;
import com.mantarus.poker.info.PlayerPublicInfo;
import com.mantarus.poker.player.Player;
import com.mantarus.poker.player.PlayersQueue;
import com.mantarus.poker.ranking.RankingUtil;
import com.mantarus.poker.strategy.ControllableStrategy;
import com.mantarus.poker.strategy.SimpleBotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    public final static int MIN_PLAYERS = 2;
    public final static int MAX_PLAYERS = 6;

    public final static int NUMBER_OF_PLAYER_CARDS = 2;
    public final static int NUMBER_OF_COMMUNITY_CARDS = 5;

    private CardDeck deck = new CardDeck();
    private PlayersQueue players;
    //TODO: Do smth to make the dealer useful
    private List<Card> communityCards = new ArrayList<>();
    private int bank;
    private int currentStake = 0;
    private boolean alive;

    public void initiateGame(int playersCount, int initialBalance) {
        List<Player> playerList = new ArrayList<>();

        Player controllablePlayer = new Player(initialBalance, new ControllableStrategy());
        playerList.add(controllablePlayer);

        for (int i = 1; i < playersCount; i++) {
            if (playerList.size() == MAX_PLAYERS) {
                throw new PokerException("Too many players");
            }
            Player player = new Player(initialBalance, new SimpleBotStrategy());
            playerList.add(player);
        }
        players = new PlayersQueue(playerList);
        updateIsAlive();
    }

    public void kickLosers() {
        for (Player player : players.asList()) {
            if (player.getBalance() <= 0) {
                leave(player);
                System.out.println(String.format("%s left the game", player.getName()));
            }
        }
    }

    public void leave(Player player) {
        players.remove(player);
        updateIsAlive();
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
            int comparingResult = RankingUtil.combinationComparator.compare(player, other);
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

    /**
     * Discard one card from the deck
     */
    public void burnCard() {
        deck.pop();
    }

    /**
     * Recalculate combinations for every player
     */
    public void recalculateCombinations() {
        players.asList().forEach(player -> RankingUtil.checkRanking(player, communityCards));
    }

    /**
     * Increase balance of chosen player in case of his victory
     * @param player chosen player
     * @param increment
     */
    public void incrementBalance(Player player, int increment) {
        player.setBalance(player.getBalance() + increment);
    }

    /**
     * Reset cards after the game
     */
    public void reset() {
        deck = new CardDeck();
        players.asList().forEach(player -> player.getHand().reset());
        communityCards.clear();
        bank = 0;
    }

    public PlayersQueue getPlayers() {
        return players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getBank() {
        return bank;
    }
    public void setBank(int bank) {
        this.bank = bank;
        System.out.println(String.format("Bank is %d now", bank));
    }
    public void clearBank() {
        bank = 0;
    }

    public int getCurrentStake() {
        return currentStake;
    }
    public void setCurrentStake(int currentStake) {
        this.currentStake = currentStake;
    }

    public boolean isAlive() {
        return alive;
    }
    private void updateIsAlive() {
        alive = players.size() >= MIN_PLAYERS;
    }

    /**
     * Get information about the current state of the game
     * @return object containing copy of board information
     */
    BoardInfo getBoardInfo() {
        List<PlayerPublicInfo> playerInfoList = players.asList()
                .stream()
                .map(Player::getPublicInfo)
                .collect(Collectors.toList());

        return new BoardInfo(
                bank,
                currentStake,
                communityCards,
                playerInfoList
        );
    }

}
