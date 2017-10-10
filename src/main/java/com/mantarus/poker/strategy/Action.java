package com.mantarus.poker.strategy;

public class Action {

    private final ActionEnum action;
    private final int amount;

    public Action(ActionEnum action, int amount) {
        this.action = action;
        this.amount = amount;
    }

    public Action(ActionEnum action) {
        this.action = action;
        this.amount = 0;
    }

    public ActionEnum getAction() {
        return action;
    }

    public int getAmount() {
        return amount;
    }

    public enum ActionEnum {
        BET,
        RAISE,
        CALL,
        CHECK,
        FOLD
    }
}
