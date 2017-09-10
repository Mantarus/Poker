package com.mantarus.poker;


public class Action {

    private final ActionEnum action;
    private final int amount;

    public Action(ActionEnum action, int amount) {
        this.action = action;
        this.amount = amount;
    }

    public ActionEnum getAction() {
        return action;
    }

    public int getAmount() {
        return amount;
    }

    public enum ActionEnum {
        RAISE,
        CALL,
        BET,
        FOLD
    }
}
