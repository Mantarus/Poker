package com.mantarus.poker.ranking;

public enum Ranking {

    HIGH_CARD(1, "HIGH CARD"),
    ONE_PAIR(2, "ONE PAIR"),
    TWO_PAIRS(3, "TWO PAIRS"),
    THREE_OF_A_KIND(4, "THREE OF A KIND"),
    STRAIGHT(5, "STRAIGHT"),
    FLUSH(6, "FLUSH"),
    FULL_HOUSE(7, "FULL HOUSE"),
    FOUR_OF_A_KIND(8, "FOUR OF A KIND"),
    STRAIGHT_FLUSH(9, "STRAIGHT FLUSH"),
    ROYAL_FLUSH(10, "ROYAL FLUSH");

    private Integer rank;
    private String representation;

    Ranking(Integer rank, String representation) {
        this.rank = rank;
        this.representation = representation;
    }

    public Integer getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return representation;
    }
}
