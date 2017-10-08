package com.mantarus.poker;

public class Main {

    public static void main(String[] args) {
        GameRunner runner = new TexasHoldemRunner();
        runner.startGame(4, 20);
    }
}
