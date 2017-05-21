package com.courseWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RankingUtil {

    public static void checkRanking(Player player, List<Card> communityCards) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(player.getHand().getCards());
        cards.addAll(communityCards);

        checkHighCard(player, cards);
        checkPairs(player, cards);
    }

    private static void checkHighCard(Player player, List<Card> cards) {
        Card highCard = cards.get(0);

        for (Card card : cards) {
            highCard = card.isGreaterThan(highCard) ? card : highCard;
        }

        player.setCombination(Collections.singletonList(highCard));
        player.setCurrentRanking(Ranking.HIGH_CARD);
    }

    private static void checkPairs(Player player, List<Card> cards) {
        // Try to find the first pair of cards
        List<Card> pairedCards = findHighestPair(cards);

        // If not found, do nothing
        if (pairedCards.isEmpty()) {
            return;
        }

        // If found, set current player combination and ranking
        player.setCombination(pairedCards);
        player.setCurrentRanking(Ranking.ONE_PAIR);

        // Then build the list of remaining cards, excluding the pair found
        List<Card> remainingCards = new ArrayList<>();
        remainingCards.addAll(cards);
        remainingCards.removeAll(pairedCards);

        // Try to find the second pair of cards and add it to resulting combination
        pairedCards.addAll(findHighestPair(remainingCards));
        // If found and added, set current player combination and ranking
        if (pairedCards.size() == 4) {
            player.setCurrentRanking(Ranking.TWO_PAIRS);
        }
    }

    private static List<Card> findHighestPair(List<Card> cards) {
        List<Card> pair = new ArrayList<>();

        // Look through all possible pairs of cards
        for (int i = 0; i < cards.size() - 1; i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                Card first = cards.get(i);
                Card second = cards.get(j);

                // If we found a pair
                if (first.isEqualTo(second)) {
                    // Check that this is the first pair found, or it is greater than the previous one
                    if (pair.isEmpty() || first.isGreaterThan(pair.get(0))) {
                        // Replace the previous one with this one
                        pair = new ArrayList<>();
                        Collections.addAll(pair, first, second);
                    }
                }
            }
        }

        return pair;
    }

}
