package com.mantarus.poker.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mantarus.poker.strategy.Action;
import com.mantarus.poker.info.BoardInfo;
import com.mantarus.poker.strategy.Action.ActionEnum;
import com.mantarus.poker.cards.Card;
import com.mantarus.poker.info.PlayerPrivateInfo;
import com.mantarus.poker.info.PlayerPublicInfo;
import com.mantarus.poker.ranking.Ranking;
import com.mantarus.poker.strategy.SimpleBotStrategy;
import com.mantarus.poker.strategy.Strategy;

import static java.lang.Math.min;

public class Player {
    private static int count = 0;

    private String name;
    private Strategy strategy;
    private Hand hand = new Hand();
    private boolean folded;
    private int balance;
    private int currentStake;
    private Ranking currentRanking;
    private List<Card> combination = new ArrayList<>();
    private List<Card> kickers = new ArrayList<>();

    public Player(int balance, Strategy strategy) {
        this.name = String.format("Player %d", ++count);
        this.strategy = strategy;
        this.balance = balance;
    }

    public int playSmallBlind(int bet) {
        bet = min(bet, balance);
        System.out.println(String.format("%s plays SMALL BLIND (%d)", name, bet));
        setCurrentStake(bet);
        setBalance(balance - bet);
        return bet;
    }

    public int playBigBlind(int bet) {
        bet = min(bet * 2, balance);
        System.out.println(String.format("%s plays BIG BLIND (%d)", name, bet));
        setCurrentStake(bet);
        setBalance(balance - bet);
        return bet;
    }

    public Action trade(BoardInfo boardInfo, Set<ActionEnum> possibleActions) {
        return strategy.trade(getPrivateInfo(), boardInfo, possibleActions);
    }

    public PlayerPublicInfo getPublicInfo() {
        return new PlayerPublicInfo(
                name,
                balance,
                currentStake,
                folded
        );
    }

    private PlayerPrivateInfo getPrivateInfo() {
        return new PlayerPrivateInfo(
                name,
                folded,
                balance,
                currentStake,
                currentRanking,
                hand.getCards(),
                combination,
                kickers
        );
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
        return hand;
    }
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public boolean isFolded() {
        return folded;
    }
    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
        System.out.println(String.format("%s balance is %d", name, balance));
    }

    public int getCurrentStake() {
        return currentStake;
    }
    public void setCurrentStake(int currentStake) {
        this.currentStake = currentStake;
        System.out.println(String.format("%s stake is %d", name, currentStake));
    }

    public Ranking getCurrentRanking() {
        return currentRanking;
    }
    public void setCurrentRanking(Ranking currentRanking) {
        this.currentRanking = currentRanking;
    }

    public List<Card> getCombination() {
        return combination;
    }
    public void setCombination(List<Card> combination) {
        this.combination = combination;
    }

    public List<Card> getKickers() {
        return kickers;
    }
    public void setKickers(List<Card> kickers) {
        this.kickers = kickers;
    }

    @Override
    public String toString() {
        return name;
    }

}
