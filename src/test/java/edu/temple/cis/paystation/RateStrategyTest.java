package edu.temple.cis.paystation;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

public class RateStrategyTest {

    private static final ZoneId ZONE = ZoneId.of("America/New_York");
    // Wednesday, Nov 4 2026 and Saturday, Nov 7 2026, at noon
    private static final Clock WEEKDAY = Clock.fixed(Instant.parse("2026-11-04T17:00:00Z"), ZONE);
    private static final Clock WEEKEND = Clock.fixed(Instant.parse("2026-11-07T17:00:00Z"), ZONE);

    // ---- Linear1 (Alphatown): 2 min per 5 cents ----

    @Test
    public void linear1Gives2MinutesPer5Cents() {
        RateStrategy rs = new Linear1RateStrategy();
        assertEquals(2, rs.calculateTime(5));
        assertEquals(10, rs.calculateTime(25));
        assertEquals(60, rs.calculateTime(150));
    }

    // ---- Linear2 (Deltatown): 1 min per 5 cents ----

    @Test
    public void linear2Gives1MinutePer5Cents() {
        RateStrategy rs = new Linear2RateStrategy();
        assertEquals(1, rs.calculateTime(5));
        assertEquals(5, rs.calculateTime(25));
        assertEquals(30, rs.calculateTime(150));
    }

    // ---- Progressive (Betatown) ----

    @Test
    public void progressiveFirstHourMatchesLinear1() {
        RateStrategy rs = new ProgressiveRateStrategy();
        assertEquals(40, rs.calculateTime(100));
    }

    @Test
    public void progressiveExactlyOneHourAt150Cents() {
        assertEquals(60, new ProgressiveRateStrategy().calculateTime(150));
    }

    @Test
    public void progressiveSecondHourIs1Point5MinPer5Cents() {
        assertEquals(75, new ProgressiveRateStrategy().calculateTime(200));
    }

    @Test
    public void progressiveExactlyTwoHoursAt350Cents() {
        assertEquals(120, new ProgressiveRateStrategy().calculateTime(350));
    }

    @Test
    public void progressiveAfterTwoHoursIs1MinPer5Cents() {
        assertEquals(130, new ProgressiveRateStrategy().calculateTime(400));
    }

    // ---- Alternating1 (Gammatown): Linear1 weekends, Progressive weekdays ----

    @Test
    public void alternating1UsesLinear1OnWeekends() {
        assertEquals(80, new Alternating1RateStrategy(WEEKEND).calculateTime(200));
    }

    @Test
    public void alternating1UsesProgressiveOnWeekdays() {
        assertEquals(75, new Alternating1RateStrategy(WEEKDAY).calculateTime(200));
    }

    // ---- Alternating2 (Omegatown): free weekends, Linear1 weekdays ----

    @Test
    public void alternating2IsFreeOnWeekends() {
        assertEquals(0, new Alternating2RateStrategy(WEEKEND).calculateTime(200));
    }

    @Test
    public void alternating2UsesLinear1OnWeekdays() {
        assertEquals(10, new Alternating2RateStrategy(WEEKDAY).calculateTime(25));
    }

    // ---- Town wiring ----

    @Test
    public void eachTownBuildsItsStrategy() {
        assertEquals(Linear1RateStrategy.class, Town.ALPHATOWN.createRateStrategy().getClass());
        assertEquals(ProgressiveRateStrategy.class, Town.BETATOWN.createRateStrategy().getClass());
        assertEquals(Alternating1RateStrategy.class, Town.GAMMATOWN.createRateStrategy().getClass());
        assertEquals(Linear2RateStrategy.class, Town.DELTATOWN.createRateStrategy().getClass());
        assertEquals(Alternating2RateStrategy.class, Town.OMEGATOWN.createRateStrategy().getClass());
    }
}
