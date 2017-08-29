package main.java.com.mantarus.poker;

import main.java.com.mantarus.poker.exceptions.PokerException;

import java.util.Comparator;

public class Card {
    private CardSuit suit;
    private CardRank rank;

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

    public Integer getRankToInt() {
        return rank.getValue();
    }

    public boolean isGreaterThan(Card card) {
        return this.getRankToInt() > card.getRankToInt();
    }

    public boolean isEqualTo(Card card) {
        return this.getRankToInt().equals(card.getRankToInt());
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

    public static final Comparator<Card> aceLowComparator = (card1, card2) -> {
        if (card1.getRank() == card2.getRank()) {
            return 0;
        }
        if (card1.getRank() == CardRank.ACE) {
            return -1;
        }
        if (card2.getRank() == CardRank.ACE) {
            return 1;
        }
        return card1.getRankToInt().compareTo(card2.getRankToInt());
    };

    public static final Comparator<Card> aceHighComparator = Comparator.comparing(Card::getRankToInt);
}
