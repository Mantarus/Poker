package com.mantarus.poker;

import com.mantarus.poker.Player.PlayerInfo;
import com.mantarus.poker.TexasHoldemBoard.BoardInfo;

public interface Strategy {

    Action trade(int stake, PlayerInfo playerInfo, BoardInfo boardInfo);

}
