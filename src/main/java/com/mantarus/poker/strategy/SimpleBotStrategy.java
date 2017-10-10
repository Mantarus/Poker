package com.mantarus.poker.strategy;

import java.util.Set;

import com.mantarus.poker.strategy.Action.ActionEnum;
import com.mantarus.poker.Utils;
import com.mantarus.poker.info.PlayerPrivateInfo;
import com.mantarus.poker.info.BoardInfo;

public class SimpleBotStrategy implements Strategy {

    //Done
    @Override
    public Action trade(PlayerPrivateInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            System.out.println("¯\\_(ツ)_/¯");
//        }
        int randResult = Utils.getRandomInt();

        if (randResult < 5 && possibleActions.contains(ActionEnum.FOLD))
            return new Action(ActionEnum.FOLD);

        if (randResult >= 70 && (possibleActions.contains(ActionEnum.BET) || possibleActions.contains(ActionEnum.RAISE))) {
            int bet = 1;
            int difference = boardInfo.currentStake - playerInfo.currentStake;
            ActionEnum action = ActionEnum.RAISE;
            if (playerInfo.balance < bet + difference)
                bet = playerInfo.balance - difference;
            if (possibleActions.contains(ActionEnum.BET))
                action = ActionEnum.BET;
            return new Action(action, bet);
        }

        if (possibleActions.contains(ActionEnum.CHECK) && boardInfo.currentStake == playerInfo.currentStake) {
            return new Action(ActionEnum.CHECK);
        }

        if (possibleActions.contains(ActionEnum.CALL)) {
            return new Action(ActionEnum.CALL);
        }

        return new Action(ActionEnum.FOLD);
    }
}
