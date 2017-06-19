package com.mantarus.poker;

import java.util.List;

public class Player {

    private Strategy strategy;
    private Hand hand;
    private Integer balance;
    private Integer currentStake;
    private Ranking currentRanking;
    private List<Card> combination;
    private List<Card> kickers;

    public Player() {
        this.hand = new Hand();
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

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getCurrentStake() {
        return currentStake;
    }

    public void setCurrentStake(Integer currentStake) {
        this.currentStake = currentStake;
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
}
