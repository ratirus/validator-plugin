package com.validator.common.model;

import org.springframework.stereotype.Component;

@Component
public class CommonConfig {
    private static int INTERVAL = 10;//sec
    private static int ATTACK_COUNT = 5;//회

    public static void setInterval(int interval) {
        if (interval > 0) {
            INTERVAL = interval;
        } else {
            throw new IllegalArgumentException("Interval must be greater than 0");
        }
    }

    public static int getInterval() {
        return INTERVAL;
    }

    public static int getAttackCount() {
        return ATTACK_COUNT;
    }

    public static void setAttackCount(int attackCount) {
        if (attackCount > 0) {
            ATTACK_COUNT = attackCount;
        } else {
            throw new IllegalArgumentException("Attack count must be greater than 0");
        }
    }
}