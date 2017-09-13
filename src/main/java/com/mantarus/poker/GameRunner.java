package com.mantarus.poker;

import java.util.List;

/**
 * http://www.pokerlistings.com/poker-rules-texas-holdem - rules
 */
public class GameRunner {
    private TexasHoldemBoard board;

    public void startGame(int playersCount, int initialBalance) {
        board = new TexasHoldemBoard();
        board.initiateGame(playersCount, initialBalance);
        run();
    }

    public void run() {
        while (board.isAlive()) {
            System.out.println("NEW GAME STARTED");
            board.reset();
            //TODO: Initialize bet properly and fix initial bets for trade rounds
            int bet = 42;
            playBlinds(bet);
            board.dealToPlayers();
            preFlop(bet);
            flop();
            trade(bet);
            turn();
            trade(bet * 2);
            river();
            trade(bet * 2);
            showdown();
            List<Player> winners = board.getWinners();
            int increment = board.getBank() / winners.size();
            for (Player winner : winners) {
                board.incrementBalance(winner, increment);
            }
            board.clearBank();
            board.kickLosers();
            board.getPlayers().rotateDealer();
        }
    }

    /**
     * Put out the blinds
     *
     * There are two blinds in Holdem - a small blind and a big blind.
     * The player directly to the left of the dealer puts out the small blind.
     *
     * The big blind (exactly, or conveniently close to, double that of the small blind)
     * is placed by the player to the left of the small blind.
     */
    private void playBlinds(int blind) {
        System.out.println("PLAY BLINDS");
        board.setBank(board.getBank() + board.getPlayers().next().playSmallBlind(blind));
        board.setBank(board.getBank() + board.getPlayers().next().playBigBlind(blind));
    }

    /**
     * The first trading round
     *
     * When all players receive their hole cards, you are now in the preflop betting round.
     * Each player must look at their cards and decide what action they would like to take.
     * In Hold'em, only one player can act at a time.
     *
     * The preflop betting round starts with the player to the left of the big blind. This player has three options:
     * 1. Fold: They pay nothing to the pot and throw away their hand, waiting for the next deal to play again.
     * 2. Call: They match the amount of the big blind.
     * 3. Raise: They raise the bet by doubling the amount of the big blind. A player may raise more depending on the betting style being played.
     *
     * Once a player has made their action, the player to the left of them gets their turn to act.
     * Each player is given the same options: fold, call the bet of the player to their right
     * (if the previous player raised, that is the amount you must call) or raise.
     *
     * A raise is always the amount of one bet in addition to the amount of the previous bet,
     * for example: if the big blind is 25¢, and the first player to act would like to raise, they put in a total of 50¢
     * (the big blind + one additional bet).
     *
     * If the next player would like to reraise, they would put in a total of 75¢ (the previous bet + one additional bet).
     *
     * A betting round ends when two conditions are met:
     * 1. All players have had a chance to act.
     * 2. All players who haven't folded have bet the same amount of money for the round.
     */
    private void preFlop(int bet) {
        System.out.println("PREFLOP");
        trade(bet);
    }

    /**
     * Once the preflop betting round ends, the flop is dealt.
     * This is done by dealing the top card in the deck facedown on the table (it becomes the burn card),
     * followed by three cards faceup.
     */
    private void flop() {
        System.out.println("FLOP");
        board.burnCard();
        board.dealToCommunity(3);
        board.recalculateCombinations();
        Utils.printCards(board.getCommunityCards());
    }

    /**
     * Once the betting round on the flop completes, the dealer deals one card facedown followed by a single card faceup,
     * also known as the "burn and turn." Once the turn has been dealt, the third betting round starts.
     */
    private void turn() {
        System.out.println("TURN");
        board.burnCard();
        board.dealToCommunity(1);
        board.recalculateCombinations();
        Utils.printCards(board.getCommunityCards());
    }

    /**
     * Assuming more than one player is left, having not folded on one of the previous streets,
     * the river is now dealt. Dealing the river is identical as dealing the turn, with one card being dealt facedown,
     * followed by a single card faceup.
     * This is the final street, and no more cards will be dealt in this hand.
     * The betting round is identical to the betting round on the turn.
     */
    private void river() {
        System.out.println("RIVER");
        board.burnCard();
        board.dealToCommunity(1);
        board.recalculateCombinations();
        Utils.printCards(board.getCommunityCards());
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
        System.out.println("TRADING ROUND");
        List<Player> players = board.getPlayers().getAsList();
        while (!checkBetEquality(players)) {
            while (board.getPlayers().next().isFolded()) {
            }
            Player player = board.getPlayers().current();
            player.trade(bet, board.getInfo());
            if (!player.isFolded()) {
                bet = player.getCurrentStake();
            }
        }

        for (Player player : players) {
            player.setCurrentStake(0);
        }
    }

    private boolean checkBetEquality(List<Player> players) {
        if (players.isEmpty()) {
            return true;
        }
        int bet = players.get(0).getCurrentStake();
        for (Player player : players) {
            if (player.getCurrentStake() != bet)
                return false;
        }
        return true;
    }

    /**
     * Open all player's cards and calculate combinations
     */
    private void showdown() {
        System.out.println("SHOWDOWN");
        board.getPlayers().forEach(player -> {
            System.out.println(String.format("%s's cards:", player.getName()));
            System.out.println(player.getHand());
            System.out.println(player.getCurrentRanking());
        });
    }

}


