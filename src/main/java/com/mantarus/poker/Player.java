package com.mantarus.poker;

import java.util.List;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;

public class Player {

    private String name;
    private Strategy strategy;
    private PlayerInfo playerInfo = new PlayerInfo();

    public Player() {
        this.playerInfo.hand = new Hand();
    }

    public Player(int balance) {
        this();
        this.playerInfo.balance = balance;
    }

    public Player(String name, int balance) {
        this(balance);
        this.name = name;
    }

    public Integer playSmallBlind(int bet) {
        setCurrentStake(bet);
        playerInfo.balance -= bet;
        return bet;
    }

    public Integer playBigBlind(int bet) {
        setCurrentStake(bet * 2);
        playerInfo.balance -= bet * 2;
        return bet * 2;
    }

    public void trade(int stake, BoardInfo boardInfo) {
        strategy.trade(stake, playerInfo, boardInfo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Hand getHand() {
        return playerInfo.hand;
    }

    public void setHand(Hand hand) {
        this.playerInfo.hand = hand;
    }

    public boolean isFolded() {
        return playerInfo.folded;
    }

    public void setFolded(boolean folded) {
        this.playerInfo.folded = folded;
    }

    public int getBalance() {
        return playerInfo.balance;
    }

    public void setBalance(Integer balance) {
        this.playerInfo.balance = balance;
    }

    public int getCurrentStake() {
        return playerInfo.currentStake;
    }

    public void setCurrentStake(Integer currentStake) {
        this.playerInfo.currentStake = currentStake;
    }

    public Ranking getCurrentRanking() {
        return playerInfo.currentRanking;
    }

    public void setCurrentRanking(Ranking currentRanking) {
        this.playerInfo.currentRanking = currentRanking;
    }

    public List<Card> getCombination() {
        return playerInfo.combination;
    }

    public void setCombination(List<Card> combination) {
        this.playerInfo.combination = combination;
    }

    public List<Card> getKickers() {
        return playerInfo.kickers;
    }

    public void setKickers(List<Card> kickers) {
        this.playerInfo.kickers = kickers;
    }

    public class PlayerInfo {
        private Hand hand;
        private boolean folded;
        private int balance;
        private int currentStake;
        private Ranking currentRanking;
        private List<Card> combination;
        private List<Card> kickers;

        public Hand getHand() {
            return hand;
        }

        public boolean isFolded() {
            return folded;
        }

        public int getBalance() {
            return balance;
        }

        public int getCurrentStake() {
            return currentStake;
        }

        public Ranking getCurrentRanking() {
            return currentRanking;
        }

        public List<Card> getCombination() {
            return combination;
        }

        public List<Card> getKickers() {
            return kickers;
        }
    }
}
