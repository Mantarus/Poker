package com.mantarus.poker;

import java.util.Set;

import static com.mantarus.poker.Utils.reader;

public class ControllableStrategy implements Strategy {

    @Override
    public Action trade(Player.PlayerInfo playerInfo, TexasHoldemBoard.BoardInfo boardInfo, Set<Action.ActionEnum> possibleActions) {
        // Print information
        System.out.println(String.format("Cards:       %s", playerInfo.getHand()));
        System.out.println(String.format("Combination: %s", playerInfo.getCombination()));
        System.out.println(String.format("Balance:     %d", playerInfo.getBalance()));
        System.out.println("Possible actions:");
        for (Action.ActionEnum action : possibleActions) {
            System.out.println(action);
        }

        // Read and parse action
        Action action;
        boolean ok;
        try {
            do {
                ok = true;
                action = parseAction(reader.readLine());
                // TODO: Check possibility of negative balance more careful
                int safeStake = playerInfo.getBalance() - (boardInfo.getCurrentStake() - playerInfo.getCurrentStake());
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
