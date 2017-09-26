package com.mantarus.poker;

import com.mantarus.poker.Player.PlayerInfo;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;
import com.mantarus.poker.Action.ActionEnum;

import java.util.Set;

public interface Strategy {

    /**
     * Give the player the opportunity to choose action
     * @param playerInfo information about player taking action
     * @param boardInfo information about the game
     * @param possibleActions set of allowed actions
     * @return
     * FOLD, 0 - player discards his cards <br>
     * CHECK, 0 - player does nothing <br>
     * CALL, 0 - player equalizes bet <br>
     * BET/RAISE, amount - player raises the bet
     */
    Action trade(PlayerInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions);

}
