package com.mantarus.poker.cards;

import com.mantarus.poker.exception.PokerException;

import java.util.Comparator;

public class Card {
    private final CardSuit suit;
    private final CardRank rank;

    public Card(CardSuit suit, CardRank rank) {
        if (suit == null) {
            throw new PokerException("Suit cannot be null");
        }
        if (rank == null) {
            throw new PokerException("Rank cannot be null");
        }

        this.suit = suit;
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public int getRankToInt() {
        return rank.getValue();
    }

    public boolean isGreaterThan(Card card) {
        return this.getRankToInt() > card.getRankToInt();
    }

    public boolean isEqualTo(Card card) {
        return this.getRankToInt() == card.getRankToInt();
    }

    public boolean isNextTo(Card card) {
        if (card.getRank() == CardRank.ACE && this.getRank() == CardRank.TWO) {
            return true;
        }
        return this.getRankToInt() - card.getRankToInt() == 1;
    }

    @Override
    public String toString() {
        return suit.getRepresentation() + rank.getRepresentation();
    }

    public static Comparator<Card> aceLowComparator() {
        return (card1, card2) -> {
            if (card1.getRank() == card2.getRank()) {
                return 0;
            }
            if (card1.getRank() == CardRank.ACE) {
                return -1;
            }
            if (card2.getRank() == CardRank.ACE) {
                return 1;
            }
            return card1.getRankToInt() - card2.getRankToInt();
        };
    }

    public static Comparator<Card> aceHighComparator() {
        return Comparator.comparing(Card::getRankToInt);
    }
}
