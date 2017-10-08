package com.mantarus.poker.info;

import com.mantarus.poker.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about the game, accessible to every player
 */
public class BoardInfo {
    public final int bank;
    public final int currentStake;
    public final List<Card> communityCards;
    public final List<PlayerPublicInfo> playersInfo;

    public BoardInfo(int bank,
                     int currentStake,
                     List<Card> communityCards,
                     List<PlayerPublicInfo> playersInfo) {
        this.bank = bank;
        this.currentStake = currentStake;
        this.communityCards = new ArrayList<>(communityCards);
        this.playersInfo = new ArrayList<>(playersInfo);
    }
}
