package com.balamaci.rx.domain;

import java.util.Arrays;

/**
 * @author Serban Balamaci
 */
public enum UserCreditScoring {

    CREDIT_CARD_STORED,
    MOBILE_PHONE_STORED,
    NONE;

    public static UserCreditScoring fromString(String scoringString) {
        return Arrays.stream(values())
                .filter((scoring) -> scoring.toString().equalsIgnoreCase(scoringString))
                .findFirst().orElse(null);
    }

}
