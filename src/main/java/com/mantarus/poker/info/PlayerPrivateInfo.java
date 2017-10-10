package com.mantarus.poker.info;

import com.mantarus.poker.cards.Card;
import com.mantarus.poker.ranking.Ranking;

import java.util.ArrayList;
import java.util.List;

/**
 * Information accessible only to the player to which it applies
 */
public class PlayerPrivateInfo {
    public final String name;
    public final boolean isFolded;
    public final int balance;
    public final int currentStake;
    public final Ranking currentRanking;
    public final List<Card> hand;
    public final List<Card> combination;
    public final List<Card> kickers;

    public PlayerPrivateInfo(String name,
                             boolean isFolded,
                             int balance,
                             int currentStake,
                             Ranking currentRanking,
                             List<Card> hand,
                             List<Card> combination,
                             List<Card> kickers) {
        this.name = name;
        this.isFolded = isFolded;
        this.balance = balance;
        this.currentStake = currentStake;
        this.currentRanking = currentRanking;
        this.hand = new ArrayList<>(hand);
        this.combination = new ArrayList<>(combination);
        this.kickers = new ArrayList<>(kickers);
    }
}
