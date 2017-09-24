package com.mantarus.poker;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GameRunner runner = new GameRunner();
        runner.startGame(4, 1000);

//        trySequence();
//        tryCombination(Ranking.HIGH_CARD);
//        tryCombination(Ranking.ONE_PAIR);
//        tryCombination(Ranking.TWO_PAIRS);
//        tryCombination(Ranking.THREE_OF_A_KIND);
//        tryCombination(Ranking.STRAIGHT);
//        tryCombination(Ranking.FLUSH);
//        tryCombination(Ranking.FOUR_OF_A_KIND);
//        tryCombination(Ranking.STRAIGHT_FLUSH);
//        tryCombination(Ranking.ROYAL_FLUSH);
    }

    private static void printCards(String title, List<Card> cards) {
        System.out.println(title);
        for (Card card : cards) {
            System.out.println(card);
        }
        System.out.println();
    }

    private static void tryCombination(Ranking ranking) {
        for (int count = 0; count < 1000000; count++) {
            CardDeck deck = new CardDeck();
            Player player = new Player();

            player.setHand(new Hand(new ArrayList<>()));
            for (int i = 0; i < 2; i++) {
                player.getHand().getCards().add(deck.pop());
            }

            List<Card> communityCards = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                communityCards.add(deck.pop());
            }

            RankingUtil.checkRanking(player, communityCards);
            if (player.getCurrentRanking().equals(ranking)) {
                printCards("Hand:", player.getHand().getCards());
                printCards("Community cards:", communityCards);
                printCards("Combination: " + player.getCurrentRanking(), player.getCombination());
                printCards("Kickers:", player.getKickers());
                System.out.println("---------------------------");
                System.out.println();
                return;
            }
        }
        System.out.println("NOPE");
    }

    private static void trySequence() {
        Player player = new Player();
        player.setHand(new Hand(new ArrayList<>()));
        player.getHand().getCards().add(new Card(CardSuit.HEARTS, CardRank.TWO));
        player.getHand().getCards().add(new Card(CardSuit.HEARTS, CardRank.THREE));
        player.getHand().getCards().add(new Card(CardSuit.CLUBS, CardRank.FOUR));
        player.getHand().getCards().add(new Card(CardSuit.CLUBS, CardRank.FIVE));
        player.getHand().getCards().add(new Card(CardSuit.DIAMONDS, CardRank.FIVE));
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(new Card(CardSuit.HEARTS, CardRank.KING));
        communityCards.add(new Card(CardSuit.HEARTS, CardRank.SIX));
        RankingUtil.checkRanking(player, communityCards);

        printCards("Hand:", player.getHand().getCards());
        printCards("Community cards:", communityCards);
        printCards("Combination: " + player.getCurrentRanking(), player.getCombination());
        printCards("Kickers:", player.getKickers());
        System.out.println("---------------------------");
        System.out.println();
    }
}
