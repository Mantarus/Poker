package com.mantarus.poker;

import java.util.Set;
import com.mantarus.poker.Player.PlayerInfo;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;
import com.mantarus.poker.Action.ActionEnum;

public class SimpleBotStrategy implements Strategy {

    //Done
    @Override
    public Action trade(PlayerInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            System.out.println("¯\\_(ツ)_/¯");
//        }
        int randResult = Utils.getRandomInt();

        if (randResult < 5 && possibleActions.contains(ActionEnum.FOLD))
            return new Action(ActionEnum.FOLD);

        if (randResult >= 80 && (possibleActions.contains(ActionEnum.BET) || possibleActions.contains(ActionEnum.RAISE))) {
            int bet = 42;
            int difference = boardInfo.getCurrentStake() - playerInfo.getCurrentStake();
            ActionEnum action = ActionEnum.RAISE;
            if (playerInfo.getBalance() < bet + difference)
                bet = playerInfo.getBalance() - difference;
            if (possibleActions.contains(ActionEnum.BET))
                action = ActionEnum.BET;
            return new Action(action, bet);
        }

        if (possibleActions.contains(ActionEnum.CHECK) && boardInfo.getCurrentStake().equals(playerInfo.getCurrentStake())) {
            return new Action(ActionEnum.CHECK);
        }

        if (possibleActions.contains(ActionEnum.CALL)) {
            return new Action(ActionEnum.CALL);
        }

        return new Action(ActionEnum.FOLD);
    }
}
