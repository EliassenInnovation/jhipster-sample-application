package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonthlyNumbersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MonthlyNumbers getMonthlyNumbersSample1() {
        return new MonthlyNumbers()
            .id(1L)
            .actualMonthId(1)
            .enrollment(1)
            .freeAndReducedPercent(1)
            .iD(1)
            .mealsServed(1)
            .monthId(1)
            .numberOfDistricts(1)
            .numberOfSites(1)
            .schoolDistrictId(1)
            .year("year1");
    }

    public static MonthlyNumbers getMonthlyNumbersSample2() {
        return new MonthlyNumbers()
            .id(2L)
            .actualMonthId(2)
            .enrollment(2)
            .freeAndReducedPercent(2)
            .iD(2)
            .mealsServed(2)
            .monthId(2)
            .numberOfDistricts(2)
            .numberOfSites(2)
            .schoolDistrictId(2)
            .year("year2");
    }

    public static MonthlyNumbers getMonthlyNumbersRandomSampleGenerator() {
        return new MonthlyNumbers()
            .id(longCount.incrementAndGet())
            .actualMonthId(intCount.incrementAndGet())
            .enrollment(intCount.incrementAndGet())
            .freeAndReducedPercent(intCount.incrementAndGet())
            .iD(intCount.incrementAndGet())
            .mealsServed(intCount.incrementAndGet())
            .monthId(intCount.incrementAndGet())
            .numberOfDistricts(intCount.incrementAndGet())
            .numberOfSites(intCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet())
            .year(UUID.randomUUID().toString());
    }
}
