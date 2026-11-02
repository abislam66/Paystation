package edu.temple.cis.paystation;

import java.time.Clock;

/**
 * Omegatown: parking is free on weekends, Linear1 on weekdays.
 */
public class Alternating2RateStrategy extends AlternatingRateStrategy {

    public Alternating2RateStrategy() {
        this(Clock.systemDefaultZone());
    }

    /** @param clock the clock used to decide weekday vs. weekend (inject a fixed clock in tests) */
    public Alternating2RateStrategy(Clock clock) {
        super(clock, amountInserted -> 0, new Linear1RateStrategy());
    }
}
