package com.mantarus.poker;

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
        checkStraight(player, cards);
        checkFlush(player, cards);
        checkFullHouse(player, cards);
        checkFourOfAKind(player, cards);
        checkStraightFlush(player, cards);
        checkRoyalFlush(player, cards);
        checkKickers(player, cards);
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
        List<Card> pairedCards = findHighestSameRanked(cards, 2);

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
        pairedCards.addAll(findHighestSameRanked(remainingCards, 2));
        // If found and added, set current player combination and ranking
        if (pairedCards.size() == 4) {
            player.setCurrentRanking(Ranking.TWO_PAIRS);
        }
    }

    private static void checkThreeOfAKind(Player player, List<Card> cards) {
        List<Card> threeOfAKind = findHighestSameRanked(cards, 3);

        setCombination(threeOfAKind, player, Ranking.THREE_OF_A_KIND);
    }

    private static void checkStraight(Player player, List<Card> cards) {
        List<Card> straight = findHighestSequence(cards, 5, false, Card.aceHighComparator);
        if (straight.isEmpty()) {
            straight = findHighestSequence(cards, 5, false, Card.aceLowComparator);
        }

        setCombination(straight, player, Ranking.STRAIGHT);
    }

    private static void checkFlush(Player player, List<Card> cards) {
        List<Card> flush = findHighestSameSuited(cards, 5);

        setCombination(flush, player, Ranking.FLUSH);
    }

    private static void checkFullHouse(Player player, List<Card> cards) {
        List<Card> fullHouse = new ArrayList<>();
        List<Card> remainingCards = new ArrayList<>(cards);
        List<Card> threeOfAKind = findHighestSameRanked(remainingCards, 3);
        remainingCards.removeAll(threeOfAKind);
        List<Card> pair = findHighestSameRanked(remainingCards, 2);

        if (!threeOfAKind.isEmpty() && !pair.isEmpty()) {
            fullHouse.addAll(threeOfAKind);
            fullHouse.addAll(pair);
        }

        setCombination(fullHouse, player, Ranking.FULL_HOUSE);
    }

    private static void checkFourOfAKind(Player player, List<Card> cards) {
        List<Card> fourOfAKind = findHighestSameRanked(cards, 4);

        setCombination(fourOfAKind, player, Ranking.FOUR_OF_A_KIND);
    }

    private static void checkStraightFlush(Player player, List<Card> cards) {
        List<Card> straight = findHighestSequence(cards, 5, true, Card.aceHighComparator);
        if (straight.isEmpty()) {
            straight = findHighestSequence(cards, 5, true, Card.aceLowComparator);
        }

        setCombination(straight, player, Ranking.STRAIGHT_FLUSH);
    }

    private static void checkRoyalFlush(Player player, List<Card> cards) {
        List<Card> straight = findHighestSequence(cards, 5, true, Card.aceHighComparator);

        straight.forEach(card -> {
            if (card.getRank().equals(CardRank.ACE)) {
                setCombination(straight, player, Ranking.ROYAL_FLUSH);
            }
        });
    }

    private static void checkKickers(Player player, List<Card> cards) {
        List<Card> remainingCards = new ArrayList<>(cards);
        remainingCards.removeAll(player.getCombination());
        remainingCards = getOrderedList(remainingCards, Card.aceHighComparator, true);

        List<Card> kickers = new ArrayList<>();
        for (int i = 0; i < 5 - player.getCombination().size(); i++) {
            kickers.add(remainingCards.get(i));
        }

        player.setKickers(kickers);
    }

    private static void setCombination(List<Card> combination, Player player, Ranking ranking) {
        if (!combination.isEmpty()) {
            player.setCombination(combination);
            player.setCurrentRanking(ranking);
        }
    }

    private static List<Card> findHighestSameRanked(List<Card> cards, Integer size) {
        List<Card> result = new ArrayList<>();

        for (Card first : cards) {
            List<Card> group = new ArrayList<>();
            group.add(first);
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

    private static List<Card> findHighestSameSuited(List<Card> cards, Integer size) {
        List<Card> result = new ArrayList<>();
        List<Card> cardsOrdered = getOrderedList(cards, Card.aceHighComparator, true);

        for (Card first : cards) {
            for (Card card : cards) {
                if (card.getSuit().equals(first.getSuit())) {
                    result.add(card);
                    if (result.size() == size) {
                        return result;
                    }
                }
            }
            result.clear();
        }
        return new ArrayList<>();
    }

    private static List<Card> findHighestSequence(List<Card> cards, Integer size, Boolean compareSuits, Comparator<Card> comparator) {
        List<Card> result = new ArrayList<>();
        List<Card> cardsOrdered = getOrderedList(cards, comparator, true);

        Card previousCard = cardsOrdered.get(0);
        for (int i = 1; i < cardsOrdered.size(); i++) {
            Card nextCard = cardsOrdered.get(i);

            if (previousCard.isNextTo(nextCard) && (!compareSuits || nextCard.getSuit().equals(previousCard.getSuit()))) {
                if (result.size() == 0) {
                    result.add(previousCard);
                }
                result.add(nextCard);
                if (result.size() == size) {
                    return result;
                }
            } else if (previousCard.getRankToInt() != nextCard.getRankToInt()) {
                result.clear();
            }

            previousCard = nextCard;
        }
        return new ArrayList<>();
    }

    private static List<Card> getOrderedList(List<Card> cards, Comparator<Card> comparator, Boolean reversed) {
        return cards.stream()
                .sorted(reversed ? comparator.reversed() : comparator)
                .collect(Collectors.toList());
    }

}
