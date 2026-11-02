package edu.temple.cis.paystation;

public class Linear1RateStrategy implements RateStrategy {
    @Override
    public int calculateTime(int amountInserted) {
        return amountInserted * 2/5;
    }

}
