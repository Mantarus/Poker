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

    private CardDeck deck;
    private PlayersQueue players;
    private List<Card> communityCards = new ArrayList<>();
    private int bank = 0;
    private int currentStake = 0;
    private boolean alive;

    public void initiateGame(int playersCount, int initialBalance) {
        List<Player> playerList = new ArrayList<>();

        Player controllablePlayer = new Player(initialBalance, new ControllableStrategy());
        playerList.add(controllablePlayer);

        for (int i = 1; i < playersCount; i++) {
            if (playerList.size() == MAX_PLAYERS) {
                throw new PokerException("Слишком много игроков");
            }
            Player player = new Player(initialBalance, new SimpleBotStrategy());
            playerList.add(player);
        }
        players = new PlayersQueue(playerList);
        updateIsAlive();
        deck = new CardDeck();
    }

    public void kickLosers() {
        for (Player player : players.asList()) {
            if (player.getBalance() <= 0) {
                leave(player);
                System.out.println(String.format("%s покинул игру", player.getName()));
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
            int comparingResult = RankingUtil.comparePlayerCombinations(player, other);
            if (comparingResult > 0) {
                winners.clear();
            } if (comparingResult >= 0) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     * Раздает карты перед игрой
     */
    public void dealToPlayers() {
        IntStream.range(0, NUMBER_OF_PLAYER_CARDS)
                .forEach(i -> players.asList().forEach(p -> p.getHand().addCard(deck.pop())));
    }

    /**
     * Открывает карты на столе
     * @param amount количество открываемых карт
     */
    public void dealToCommunity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Количество карт должно быть строго положительным");
        }
        if (amount + communityCards.size() > NUMBER_OF_COMMUNITY_CARDS) {
            throw new IllegalArgumentException("Количество карт на столе не должно превысить максимального количества");
        }
        IntStream.range(0, amount)
                .forEach(i -> communityCards.add(deck.pop()));
    }

    /**
     * Сбрасывает одну карту из колоды
     */
    public void burnCard() {
        deck.pop();
    }

    /**
     * Пересчитать карточные комбинации для всех игроков
     */
    public void recalculateCombinations() {
        players.asList().forEach(player -> RankingUtil.checkRanking(player, communityCards));
    }

    /**
     * Увеличить баланс игрока в случае его победы
     * @param player выбранный игрок
     * @param increment количество фишек, на которое требуется увеличить баланс
     */
    public void incrementBalance(Player player, int increment) {
        player.setBalance(player.getBalance() + increment);
    }

    /**
     * Сбрасывает карты в конце игры
     */
    public void reset() {
        deck = new CardDeck();
        communityCards.clear();
        bank = 0;
    }

    /**
     * @return обьект, содержащий копию информации о текущем состоянии стола
     */
    public BoardInfo getBoardInfo() {
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
        System.out.println(String.format("Размер банка: %d", bank));
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

}
