package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DistributorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Distributor getDistributorSample1() {
        return new Distributor()
            .id(1L)
            .createdBy(1)
            .distributorCode("distributorCode1")
            .distributorID(1)
            .distributorName("distributorName1")
            .updatedBy(1);
    }

    public static Distributor getDistributorSample2() {
        return new Distributor()
            .id(2L)
            .createdBy(2)
            .distributorCode("distributorCode2")
            .distributorID(2)
            .distributorName("distributorName2")
            .updatedBy(2);
    }

    public static Distributor getDistributorRandomSampleGenerator() {
        return new Distributor()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .distributorCode(UUID.randomUUID().toString())
            .distributorID(intCount.incrementAndGet())
            .distributorName(UUID.randomUUID().toString())
            .updatedBy(intCount.incrementAndGet());
    }
}
