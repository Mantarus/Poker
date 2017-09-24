package com.mantarus.poker;

import com.mantarus.poker.Player.PlayerInfo;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;
import com.mantarus.poker.Action.ActionEnum;

import java.util.Set;

public interface Strategy {

    Action trade(int stake, PlayerInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions);

}
