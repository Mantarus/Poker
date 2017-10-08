package com.mantarus.poker.info;

/**
 * Information accessible to anyone
 */
public class PlayerPublicInfo {
    public final String name;
    public final int balance;
    public final int currentStake;
    public final boolean isFolded;

    public PlayerPublicInfo(String name,
                            int balance,
                            int currentStake,
                            boolean isFolded) {
        this.name = name;
        this.balance = balance;
        this.currentStake = currentStake;
        this.isFolded = isFolded;
    }
}
