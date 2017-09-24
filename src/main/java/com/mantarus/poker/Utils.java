package com.mantarus.poker;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Utils {
    private static Random random = new Random();

    public static int getRandomInt() {
        return random.nextInt(100);
    }

    public static void printCards(List<Card> cards) {
        System.out.println(cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" "))
        );
    }

}
