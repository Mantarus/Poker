package com.mantarus.poker.strategies;

import com.mantarus.poker.Action;
import com.mantarus.poker.info.BoardInfo;
import com.mantarus.poker.info.PlayerPrivateInfo;

import java.util.Set;

import static com.mantarus.poker.Utils.reader;

public class ControllableStrategy implements Strategy {

    @Override
    public Action trade(PlayerPrivateInfo playerInfo, BoardInfo boardInfo, Set<Action.ActionEnum> possibleActions) {
        //Print information
        printInfo(playerInfo, boardInfo, possibleActions);

        // Read and parse action
        Action action;
        boolean ok;
        try {
            do {
                ok = true;
                System.out.print("Your action: ");
                action = parseAction(reader.readLine());
                // TODO: Check possibility of negative balance more careful
                int safeStake = playerInfo.balance - (boardInfo.currentStake - playerInfo.currentStake);
                if (action == null || !possibleActions.contains(action.getAction()) || action.getAmount() > safeStake) {
                    ok = false;
                    System.out.println("Nope!");
                }
            } while (!ok);
        } catch (Exception e) {
            e.printStackTrace();
            return new Action(Action.ActionEnum.FOLD);
        }
        return action;
    }

    private void printInfo(PlayerPrivateInfo playerInfo, BoardInfo boardInfo, Set<Action.ActionEnum> possibleActions) {
        // Print board information
        System.out.println(String.format("Bank:          %d", boardInfo.bank));
        System.out.println(String.format("Current stake: %d", boardInfo.currentStake));
        System.out.println();
        //Print information about players
        boardInfo.playersInfo.forEach(player -> {
            System.out.println(String.format("%s - %sfolded, balance: %d, stake: %d",
                    player.name,
                    player.isFolded ? "" : "not ",
                    player.balance,
                    player.currentStake));
        });
        System.out.println();
        // Print player information
        System.out.println(String.format("%s:", playerInfo.name));
        System.out.println(String.format("Board cards: %s", boardInfo.communityCards));
        System.out.println(String.format("Cards:       %s", playerInfo.hand));
        System.out.println(String.format("Combination: %s - %s", playerInfo.currentRanking, playerInfo.combination));
        System.out.println(String.format("Balance:     %d", playerInfo.balance));
        System.out.println("Possible actions:");
        for (Action.ActionEnum action : possibleActions) {
            System.out.println(action);
        }
    }

    private Action parseAction(String string) {
        Action.ActionEnum actionEnum;
        int value = 0;

        String[] tokens = string.split(" ");
        switch (tokens[0].toUpperCase()) {
            case "BET": {
                actionEnum = Action.ActionEnum.BET;
                break;
            } case "RAISE": {
                actionEnum = Action.ActionEnum.RAISE;
                break;
            } case "CALL": {
                actionEnum = Action.ActionEnum.CALL;
                break;
            } case "CHECK": {
                actionEnum = Action.ActionEnum.CHECK;
                break;
            } case "FOLD": {
                actionEnum = Action.ActionEnum.FOLD;
                break;
            } default:
                return null;
        }
        if (actionEnum == Action.ActionEnum.BET || actionEnum == Action.ActionEnum.RAISE) {
            try {
                value = Integer.parseInt(tokens[1]);
            } catch (Exception e) {
                return null;
            }
        }
        return new Action(actionEnum, value);
    }
}
