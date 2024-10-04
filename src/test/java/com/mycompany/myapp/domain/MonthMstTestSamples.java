package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonthMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MonthMst getMonthMstSample1() {
        return new MonthMst().id(1L).monthID(1).monthName("monthName1").year("year1");
    }

    public static MonthMst getMonthMstSample2() {
        return new MonthMst().id(2L).monthID(2).monthName("monthName2").year("year2");
    }

    public static MonthMst getMonthMstRandomSampleGenerator() {
        return new MonthMst()
            .id(longCount.incrementAndGet())
            .monthID(intCount.incrementAndGet())
            .monthName(UUID.randomUUID().toString())
            .year(UUID.randomUUID().toString());
    }
}
