package com.courseWork;

public class Main {

    public static void main(String[] args) {

        CardDeck deck = new CardDeck();

        Integer count = 0;
        while (!deck.isEmpty()) {
            System.out.println((++count).toString() + " " + deck.pop());
        }
    }
}
