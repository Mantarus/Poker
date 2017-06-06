package com.courseWork;

import java.util.Comparator;

public class Card {
    private CardSuit suit;
    private CardRank rank;

    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public Integer getRankToInt() {
        return rank.getValue();
    }

    public boolean isGreaterThan(Card card) {
        return this.getRankToInt() > card.getRankToInt();
    }

    public boolean isEqualTo(Card card) {
        return this.getRankToInt().equals(card.getRankToInt());
    }

    @Override
    public String toString() {
        return suit.getRepresentation() + rank.getRepresentation();
    }

    public static Comparator<Card> aceLowComparator = (card1, card2) -> {
        if (card1.getRank().equals(card2.getRank())) {
            return 0;
        }
        if (card1.getRank().equals(CardRank.ACE)) {
            return -1;
        }
        if (card2.getRank().equals(CardRank.ACE)) {
            return 1;
        }
        return card1.getRankToInt().compareTo(card2.getRankToInt());
    };

    public static Comparator<Card> aceHighComparator = Comparator.comparing(Card::getRankToInt);
}
