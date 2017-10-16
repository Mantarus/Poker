package com.mantarus.poker.strategy;

import com.mantarus.poker.strategy.Action.ActionEnum;
import com.mantarus.poker.info.PlayerPrivateInfo;
import com.mantarus.poker.info.BoardInfo;

import java.util.Set;

public interface Strategy {

    /**
     * Интерфейс, позволяющий игроку совершать какое-либо действие в ходе раунда торговли
     * @param playerInfo информация о игроке, совершающем действие
     * @param boardInfo информация о текущей ситуации на игровом столе
     * @param possibleActions набор разрешенных действий
     * @return
     * FOLD, 0 - игрок сбрасывает карты <br>
     * CHECK, 0 - игрок ничего не делает <br>
     * CALL, 0 - игрок уравнивает ставку <br>
     * BET/RAISE, amount - игрок поднимает ставку
     */
    Action trade(PlayerPrivateInfo playerInfo, BoardInfo boardInfo, Set<ActionEnum> possibleActions);

}
