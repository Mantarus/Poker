package com.courseWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RankingUtil {

    public static void checkRanking(Player player, List<Card> communityCards) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(player.getHand().getCards());
        cards.addAll(communityCards);

        checkHighCard(player, cards);
        checkPairs(player, cards);
        checkThreeOfAKind(player, cards);
        checkFourOfAKind(player, cards);
        checkStraight(player, cards);
        checkFlush(player, cards);
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
        List<Card> pairedCards = findHighestSameCards(cards, 2);

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
        pairedCards.addAll(findHighestSameCards(remainingCards, 2));
        // If found and added, set current player combination and ranking
        if (pairedCards.size() == 4) {
            player.setCurrentRanking(Ranking.TWO_PAIRS);
        }
    }

    private static void checkThreeOfAKind(Player player, List<Card> cards) {
        List<Card> threeOfAKind = findHighestSameCards(cards, 3);

        if (!threeOfAKind.isEmpty()) {
            player.setCombination(threeOfAKind);
            player.setCurrentRanking(Ranking.THREE_OF_A_KIND);
        }
    }

    private static void checkStraight(Player player, List<Card> cards) {
        List<Card> straightLow = findHighestSequence(cards, 5, false, Card.aceLowComparator);
        List<Card> straightHigh = findHighestSequence(cards, 5, false, Card.aceHighComparator);
        List<Card> resultStraight = !straightHigh.isEmpty() ? straightHigh : straightLow;

        if (!resultStraight.isEmpty()) {
            player.setCombination(resultStraight);
            player.setCurrentRanking(Ranking.STRAIGHT);
        }
    }

    private static void checkFourOfAKind(Player player, List<Card> cards) {
        List<Card> fourOfAKind = findHighestSameCards(cards, 4);

        if (!fourOfAKind.isEmpty()) {
            player.setCombination(fourOfAKind);
            player.setCurrentRanking(Ranking.FOUR_OF_A_KIND);
        }
    }

    private static List<Card> findHighestSameCards(List<Card> cards, Integer size) {
        List<Card> result = new ArrayList<>();

        for (Card first : cards) {
            List<Card> group = Collections.singletonList(first);
            for (Card card : cards) {
                if (card != first && card.isEqualTo(first)) {
                    group.add(card);
                }
                if (group.size() == size) {
                    if (result.isEmpty() || first.isGreaterThan(result.get(0))) {
                        result.clear();
                        result.addAll(group);
                    }
                }
            }
        }

        return result;
    }

    private static List<Card> findHighestSequence(List<Card> cards, Integer size, Boolean compareSuits, Comparator<Card> comparator) {
        List<Card> result = new ArrayList<>();
        List<Card> cardsOrdered = cards.stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());

        Card previousCard = cardsOrdered.get(0);
        for (int i = 1; i < cardsOrdered.size(); i++) {
            Card nextCard = cardsOrdered.get(i);

            if (nextCard.getRankToInt() - previousCard.getRankToInt() == 1 && (!compareSuits || nextCard.getSuit().equals(previousCard.getSuit()))) {
                if (result.size() == 0) {
                    result.add(previousCard);
                }
                result.add(nextCard);
                if (result.size() == size) {
                    return result;
                }
            } else {
                result.clear();
            }

            previousCard = nextCard;
        }
        return new ArrayList<>();
    }



}
