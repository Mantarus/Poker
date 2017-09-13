package com.mantarus.poker;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Utils {

    public static void printCards(List<Card> cards) {
        System.out.println(cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" "))
        );
    }

}
