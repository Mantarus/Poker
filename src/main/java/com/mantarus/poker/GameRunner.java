package com.mantarus.poker;

import com.mantarus.poker.exceptions.UnallowedActionException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            System.out.println(String.format("Player %s won %d", winner.getName(), increment));
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
        List<Player> players = board.getPlayers().getAsList();
        do {
            while (board.getPlayers().next().isFolded()) {
            }
            Player player = board.getPlayers().current();

            Set<Action.ActionEnum> allowedActions = new HashSet<>();
            allowedActions.add(Action.ActionEnum.BET);
            allowedActions.add(Action.ActionEnum.RAISE);
            allowedActions.add(Action.ActionEnum.CALL);
            allowedActions.add(Action.ActionEnum.CHECK);
            allowedActions.add(Action.ActionEnum.FOLD);

            Action action = player.trade(bet, board.getBoardInfo(), allowedActions);
            if (!checkAction(action, player))
                throw new UnallowedActionException("Action is not allowed!");

            executeAction(action, player);
            if (action.getAction() == Action.ActionEnum.FOLD && checkOneNotFolded(players)) {
                return true;
            }

            if (!player.isFolded()) {
                bet = player.getCurrentStake();
            }
        } while (!checkBetEquality(players));
        return false;
    }

    private boolean checkAction(Action action, Player player) {
        return true;
    }

    private void executeAction(Action action, Player player) {
        System.out.println(String.format("%s does %s (%d)", player.getName(), action.getAction(), action.getAmount()));
        if (action.getAction().equals(Action.ActionEnum.FOLD)) {
            player.setFolded(true);
        } else {
            player.setCurrentStake(player.getCurrentStake() + action.getAmount());
            player.setBalance(player.getBalance() - action.getAmount());
            board.setBank(board.getBank() + action.getAmount());
            if (player.getCurrentStake() > board.getBoardInfo().getCurrentStake()) {
                board.getBoardInfo().setCurrentStake(player.getCurrentStake());
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


