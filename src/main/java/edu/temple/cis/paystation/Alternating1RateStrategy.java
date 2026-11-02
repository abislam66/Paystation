package edu.temple.cis.paystation;
import java.util.Calendar;
public class Alternating1RateStrategy implements RateStrategy{
    @Override
    public int calculateTime(int amountInserted) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY || day == Calendar.SATURDAY){
            RateStrategy strategy = new Linear1RateStrategy();
            return strategy.calculateTime(amountInserted);
        }else{
            RateStrategy strategy = new ProgressiveRateStrategy();
            return strategy.calculateTime(amountInserted);
        }

    }

}
