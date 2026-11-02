package edu.temple.cis.paystation;

import java.time.Clock;

/**
 * Gammatown: Linear1 on weekends, Progressive on weekdays.
 */
public class Alternating1RateStrategy extends AlternatingRateStrategy {

    public Alternating1RateStrategy() {
        this(Clock.systemDefaultZone());
    }

    /** @param clock the clock used to decide weekday vs. weekend (inject a fixed clock in tests) */
    public Alternating1RateStrategy(Clock clock) {
        super(clock, new Linear1RateStrategy(), new ProgressiveRateStrategy());
    }
}
