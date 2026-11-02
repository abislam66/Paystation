package edu.temple.cis.paystation;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * A rate strategy that delegates to one strategy on weekends and another on weekdays.
 *
 * The current day comes from an injected {@link Clock}, so tests can pin the
 * date instead of depending on what day they happen to run.
 */
public abstract class AlternatingRateStrategy implements RateStrategy {

    private final Clock clock;
    private final RateStrategy weekendStrategy;
    private final RateStrategy weekdayStrategy;

    protected AlternatingRateStrategy(Clock clock, RateStrategy weekendStrategy, RateStrategy weekdayStrategy) {
        this.clock = clock;
        this.weekendStrategy = weekendStrategy;
        this.weekdayStrategy = weekdayStrategy;
    }

    @Override
    public int calculateTime(int amountInserted) {
        return isWeekend() ? weekendStrategy.calculateTime(amountInserted)
                           : weekdayStrategy.calculateTime(amountInserted);
    }

    private boolean isWeekend() {
        DayOfWeek day = LocalDate.now(clock).getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
