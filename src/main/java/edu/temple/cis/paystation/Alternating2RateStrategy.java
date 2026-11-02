package edu.temple.cis.paystation;

import java.util.Calendar;

public class Alternating2RateStrategy implements RateStrategy {
        @Override
        public int calculateTime(int amountInserted) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if(day == Calendar.SUNDAY || day == Calendar.SATURDAY){
                return 0;
            }else{
                RateStrategy strategy = new Linear1RateStrategy();
                return strategy.calculateTime(amountInserted);
            }

        }

    }


