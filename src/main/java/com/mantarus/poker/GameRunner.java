package main.java.com.mantarus.poker;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.pokerlistings.com/poker-rules-texas-holdem - rules
 */
public class GameRunner {
    private TexasHoldemBoard board;
    //TODO: Do smth to make the dealer useful
    private Player dealer;

    public void initiateGame(int playersCount, int initialBalance) {
        board = new TexasHoldemBoard();
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player(initialBalance);
            board.sit(player);
        }
        run();
    }

    public void run() {
        while (board.isAlive()) {
            board.reset();
            playBlinds();
            //TODO: Initialize bet properly and fix initial bets for trade rounds
            int bet = 42;
            board.dealToPlayers();
            preFlop();
            flop();
            trade(bet);
            turn();
            trade(bet);
            river();
            trade(bet);
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

    /**
     * Put out the blinds
     */
    private void playBlinds() {

    }

    /**
     * The first trading round
     *
     * A betting round ends when two conditions are met:
     * All players have had a chance to act.
     * All players who haven't folded have bet the same amount of money for the round.
     */
    private void preFlop() {

    }

    /**
     * Once the preflop betting round ends, the flop is dealt.
     * This is done by dealing the top card in the deck facedown on the table (it becomes the burn card),
     * followed by three cards faceup.
     */
    private void flop() {
        board.burnCard();
        board.dealToCommunity(3);
        board.recalculateCombinations();
    }

    /**
     * Once the betting round on the flop completes, the dealer deals one card facedown followed by a single card faceup,
     * also known as the "burn and turn." Once the turn has been dealt, the third betting round starts.
     */
    private void turn() {
        board.burnCard();
        board.dealToCommunity(1);
        board.recalculateCombinations();
    }

    /**
     * Assuming more than one player is left, having not folded on one of the previous streets,
     * the river is now dealt. Dealing the river is identical as dealing the turn, with one card being dealt facedown,
     * followed by a single card faceup.
     * This is the final street, and no more cards will be dealt in this hand.
     * The betting round is identical to the betting round on the turn.
     */
    private void river() {
        board.burnCard();
        board.dealToCommunity(1);
        board.recalculateCombinations();
    }

    /**
     * The rules of a post-flop betting round are the same as a preflop, with two small exceptions:
     * The first player to act is the next player with a hand to the left of the dealer,
     * and the first player to act can check or bet; as there has been no bet made, calling is free.
     *
     * A bet on the flop is the amount of the big blind.
     *
     * The third betting round is identical to the flop betting round with one single exception:
     * The size of a bet for this round, and the final betting round, is doubled
     */
    private void trade(int bet) {

    }

    /**
     * Open all player's cards and calculate combinations
     */
    private void showdown() {

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


