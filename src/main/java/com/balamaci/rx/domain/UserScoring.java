package com.balamaci.rx.domain;

import java.util.Arrays;

/**
 * @author Serban Balamaci
 */
public enum UserScoring {

    HIGHEST,
    HIGH,
    MEDIUM,
    LOW,
    LOWEST;

    public static UserScoring fromString(String scoringString) {
        return Arrays.stream(values())
                .filter((scoring) -> scoring.toString().equalsIgnoreCase(scoringString))
                .findFirst().orElse(null);
    }

}
