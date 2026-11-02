package edu.temple.cis.paystation;

public class Linear2RateStrategy implements RateStrategy {
    @Override
    public int calculateTime(int amountInserted) {
        return amountInserted * 1/5;
    }

}
