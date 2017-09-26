package com.mantarus.poker;

import com.mantarus.poker.exceptions.UnallowedActionException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.min;

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
            if (preFlop(bet)) {
                closeGame();
                continue;
            }
            flop();
            if (trade(bet)) {
                closeGame();
                continue;
            }
            turn();
            if (trade(bet * 2)) {
                closeGame();
                continue;
            }
            river();
            if (trade(bet * 2)) {
                closeGame();
                continue;
            }
            showdown();
            closeGame();
        }
        for (Player player : board.getPlayers()) {
            System.out.println(String.format("WINNER: %s", player.getName()));
        }
    }

    private void closeGame() {
        board.recalculateCombinations();

        board.getPlayers().forEach(player -> {
            if (!player.isFolded()) {
                System.out.println(String.format("%s's cards:", player.getName()));
                System.out.println(player.getHand());
                System.out.println(player.getCurrentRanking());
            }
        });

        List<Player> winners = board.getWinners();
        int increment = board.getBank() / winners.size();
        for (Player winner : winners) {
            board.incrementBalance(winner, increment);
            System.out.println(String.format("%s won %d", winner.getName(), increment));
        }
        board.clearBank();
        board.getBoardInfo().setCurrentStake(0);
        board.kickLosers();
        board.getPlayers().rotateDealer();
        board.getPlayers().forEach(player ->  {
            player.setFolded(false);
            player.setCurrentStake(0);
        });
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
        board.getBoardInfo().setCurrentStake(blind * 2);
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
    private boolean preFlop(int bet) {
        System.out.println("PREFLOP");
        return trade(bet);
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
     *
     * @return 'true' if someone wins, 'false' otherwise
     */
    private boolean trade(int bet) {
        System.out.println("TRADING ROUND");

        boolean betDone = false;
        int raiseCount = 0;

        List<Player> players = board.getPlayers().getAsList();
        do {
            if (checkZeroBalance(players) || checkOneNotFolded(players)) {
                return true;
            }

            Player player = board.getPlayers().next();
            if (player.isFolded() || player.getBalance() == 0) {
                System.out.println(String.format("%s skips because of %s", player.getName(), player.isFolded() ? "FOLD" : "zero balance"));
                continue;
            }

            Set<Action.ActionEnum> allowedActions = getPossibleActions(player, betDone, raiseCount);

            Action action = player.trade(board.getBoardInfo(), allowedActions);
            checkAction(action, player, allowedActions);
            executeAction(action, player);

            if (action.getAction() == Action.ActionEnum.BET) {
                betDone = true;
            }
            if (action.getAction() == Action.ActionEnum.RAISE) {
                raiseCount++;
            }
        } while (!checkBetEquality(players));
        return false;
    }

    //Done
    /**
     * Analyze information about game state and player to provide set of possible actions
     * @param player acting player
     * @param betDone true if someone acted BET before, false otherwise
     * @param raiseCount how many times RAISE was committed
     * @return set of possible actions
     */
    private Set<Action.ActionEnum> getPossibleActions(Player player, boolean betDone, int raiseCount) {
        Set<Action.ActionEnum> allowedActions = new HashSet<>();

        // Difference between board current stake and player current stake
        int difference = board.getBoardInfo().getCurrentStake() - player.getCurrentStake();

        // FOLD is allowed by default
        allowedActions.add(Action.ActionEnum.FOLD);

        // If player current stake equals to board current stake, allow CHECK, else allow CALL
        if (difference == 0) {
            allowedActions.add(Action.ActionEnum.CHECK);
        } else {
            allowedActions.add(Action.ActionEnum.CALL);
        }

        // If player has balance bigger than difference, he can BET or RAISE
        if (player.getBalance() > difference) {
            // If nobody did BET before, BET is allowed
            if (!betDone) {
                allowedActions.add(Action.ActionEnum.BET);
            // Else if RAISE was committed less than 3 times, RAISE is allowed
            } else if (raiseCount < 3) {
                allowedActions.add(Action.ActionEnum.RAISE);
            }
        }
        return allowedActions;
    }

    //Done
    /**
     * Check that suggested action is possible, throws exception if it is not
     * @param action suggested action
     * @param player acting player
     * @param allowedActions set of possible actions
     */
    private void checkAction(Action action, Player player, Set<Action.ActionEnum> allowedActions) {
        // Difference between board current stake and player's current stake
        int difference = board.getBoardInfo().getCurrentStake() - player.getCurrentStake();
        // Additional bet chosen by player
        int bet = action.getAmount();

        // Check that action is contained in set of possible actions
        if (!allowedActions.contains(action.getAction()))
            throw new UnallowedActionException("Action was not allowed!");

        // Check that bet is not negative
        if (bet < 0)
            throw new UnallowedActionException("Amount can't be negative!");

        // Check that bet is positive in case of BET and RAISE
        if ((action.getAction() == Action.ActionEnum.BET || action.getAction() == Action.ActionEnum.RAISE) && bet <= 0) {
            throw new UnallowedActionException(String.format("Amount of %s must be positive!", action.getAction()));
        }

        // Check that bet isn't too large in case of BET and RAISE
        if (action.getAction() == Action.ActionEnum.BET || action.getAction() == Action.ActionEnum.RAISE) {
            int total = difference + bet;
            if (total > player.getBalance())
                throw new UnallowedActionException("Action can't lead to negative balance!");
        // Check that bet is 0 otherwise
        } else if (bet != 0) {
            throw new UnallowedActionException(String.format("Amount of %s must be zero!", action.getAction()));
        }
    }

    //Done
    /**
     * Take previously chosen and checked action
     * @param action chosen action
     * @param player acting player
     */
    private void executeAction(Action action, Player player) {
        // Difference between board current stake and player's current stake
        int difference = board.getBoardInfo().getCurrentStake() - player.getCurrentStake();
        // Additional bet chosen by player
        int bet = action.getAmount();

        // Log information about the action
        System.out.println(String.format("%s does %s (%d)", player.getName(), action.getAction(), action.getAmount()));

        switch (action.getAction()) {
            // Set player folded if FOLD is committed
            case FOLD: {
                player.setFolded(true);

                break;
            }
            // Equalize player stake to board stake in case of CALL
            case CALL: {
                difference = min(player.getBalance(), difference);
                //Change player state
                player.setCurrentStake(player.getCurrentStake() + difference);
                player.setBalance(player.getBalance() - difference);
                //Change board state
                board.setBank(board.getBank() + difference);

                break;
            }
            // Equalize player stake to board stake and do additional bet in case of BET or RAISE
            case BET:
            case RAISE: {
                // Change player state
                player.setCurrentStake(board.getBoardInfo().getCurrentStake() + bet);
                player.setBalance(player.getBalance() - difference - bet);
                // Change board state
                board.getBoardInfo().setCurrentStake(board.getBoardInfo().getCurrentStake() + bet);
                board.setBank(board.getBank() + difference + bet);

                break;
            }
            // Do nothing in case of CHECK
            case CHECK: {
            }
        }
    }

    private boolean checkBetEquality(List<Player> players) {
        int bet = 0;
        for (Player player : players) {
            if (!player.isFolded() && player.getBalance() > 0) {
                bet = player.getCurrentStake();
                break;
            }
        }
        for (Player player : players) {
            if (player.getCurrentStake() != bet)
                return false;
        }
        return true;
    }

    private boolean checkZeroBalance(List<Player> players) {
        return players.stream().filter(player -> !player.isFolded() && player.getBalance() > 0).collect(Collectors.toList()).size() == 0;
    }

    private boolean checkOneNotFolded(List<Player> players) {
        int folded = players.stream().filter(Player::isFolded).collect(Collectors.toList()).size();
        return folded == 1;
    }

    /**
     * Open all player's cards and calculate combinations
     */
    private void showdown() {
        System.out.println("SHOWDOWN");
    }

}


