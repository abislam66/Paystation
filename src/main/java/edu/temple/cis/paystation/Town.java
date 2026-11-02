package edu.temple.cis.paystation;

import java.util.function.Supplier;

// Part 5: the towns
public enum Town {

    // Each town: display name, rate name, and how to build its rate strategy.
    // "Linear1RateStrategy::new" == "() -> new Linear1RateStrategy()"
    ALPHATOWN("Alphatown", "Linear1", Linear1RateStrategy::new),
    BETATOWN("Betatown", "Progressive", ProgressiveRateStrategy::new),
    GAMMATOWN("Gammatown", "Alternating1", Alternating1RateStrategy::new),
    DELTATOWN("Deltatown", "Linear2", Linear2RateStrategy::new),
    OMEGATOWN("Omegatown", "Alternating2", Alternating2RateStrategy::new);

    public static final Town DEFAULT = ALPHATOWN;

    private final String townName;
    private final String rateName;

    // Builds a new rate strategy each time get() is called
    private final Supplier<RateStrategy> rateFactory;

    Town(String townName, String rateName, Supplier<RateStrategy> rateFactory) {
        this.townName = townName;
        this.rateName = rateName;
        this.rateFactory = rateFactory;
    }

    // Runs the recipe, e.g. returns new Linear1RateStrategy() for Alphatown
    public RateStrategy createRateStrategy() {
        return rateFactory.get();
    }

    // Shown in the menu, e.g. "Alphatown (Linear1)"
    @Override
    public String toString() {
        return townName + " (" + rateName + ")";
    }
}
