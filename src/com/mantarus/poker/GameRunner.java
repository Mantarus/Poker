package com.mantarus.poker;

import java.util.ArrayList;
import java.util.List;

public class GameRunner {
    private Board board;

    public void initiateGame(int playersCount, int initialBalance) {
        board = new Board();
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player(initialBalance);
            board.sit(player);
        }
        run();
    }

    public void run() {
        while (board.isAlive()) {
            board.reset();
            board.deal();
            preFlop();
            flop();
            trade();
            turn();
            trade();
            river();
            trade();
            showdown();
            List<Player> winners = getWinners();
            int increment = board.getBank() / winners.size();
            for (Player winner : winners) {
                board.incrementBalance(winner, increment);
            }
            for (Player player : board.getPlayers()) {
                if (player.getBalance() <= 0) {
                    board.leave(player);
                }
            }
        }
    }

    private void preFlop() {

    }

    private List<Player> getWinners() {
        List<Player> players = board.getPlayers();
        List<Player> winners = new ArrayList<>();

        for (Player player : players) {
            if (winners.isEmpty()) {
                winners.add(player);
                continue;
            }
            Player other = winners.get(0);
            Integer comparingResult = RankingUtil.combinationComparator.compare(player, other);
            if (comparingResult > 0) {
                winners.clear();
            } if (comparingResult >= 0) {
                winners.add(player);
            }
        }

        return winners;
    }
}


