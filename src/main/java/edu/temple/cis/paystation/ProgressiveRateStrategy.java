package edu.temple.cis.paystation;

public class ProgressiveRateStrategy implements RateStrategy
{
    @Override
    public int calculateTime(int amountInserted)
    {
        //Less than one hour
        //5 cents gives 2 minutes
        if (amountInserted < 150)
        {
            return (amountInserted * 2) / 5;
        }
        //Between one and two hours
        //5 cents gives 1.5 minutes
        else if (amountInserted < 350)
        {
            return 60 + (amountInserted - 150) * 3 / 10;
        }
        //Greater than two hours
        //5 cents gives 1 minute
        else
        {
            return 120 + (amountInserted - 350) / 5;
        }
    }
}
