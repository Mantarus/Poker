package com.mantarus.poker;

import java.util.Set;
import com.mantarus.poker.Player.PlayerInfo;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;
import com.mantarus.poker.Action.ActionEnum;

public class SimpleBotStrategy implements Strategy {

    @Override
    public Action trade(int stake, PlayerInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions) {
        int randResult = Utils.getRandomInt();

        if (randResult < 5 && possibleActions.contains(ActionEnum.FOLD))
            return new Action(ActionEnum.FOLD);

        if (randResult >= 80 && (possibleActions.contains(ActionEnum.BET) || possibleActions.contains(ActionEnum.RAISE))) {
            int bet = 50;
            ActionEnum action = ActionEnum.RAISE;
            if (playerInfo.getBalance() < 50)
                bet = playerInfo.getBalance();
            if (possibleActions.contains(ActionEnum.BET))
                action = ActionEnum.BET;
            return new Action(action, bet);
        }

        if (boardInfo.getCurrentStake().equals(playerInfo.getCurrentStake()) && possibleActions.contains(ActionEnum.CHECK)) {
            return new Action(ActionEnum.CHECK);
        }

        if (possibleActions.contains(ActionEnum.CALL)) {
            int bet = boardInfo.getCurrentStake() - playerInfo.getCurrentStake();
            bet = bet > playerInfo.getBalance() ? playerInfo.getBalance() : bet;
            return new Action(ActionEnum.CALL, bet);
        }

        return new Action(ActionEnum.FOLD);
    }
}
