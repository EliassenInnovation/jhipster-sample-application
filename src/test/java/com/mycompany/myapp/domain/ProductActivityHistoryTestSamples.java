package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductActivityHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductActivityHistory getProductActivityHistorySample1() {
        return new ProductActivityHistory()
            .id(1L)
            .activityId(1)
            .activityType("activityType1")
            .createdBy(1)
            .productId(1L)
            .suggestedProductId(1)
            .updatedBy(1);
    }

    public static ProductActivityHistory getProductActivityHistorySample2() {
        return new ProductActivityHistory()
            .id(2L)
            .activityId(2)
            .activityType("activityType2")
            .createdBy(2)
            .productId(2L)
            .suggestedProductId(2)
            .updatedBy(2);
    }

    public static ProductActivityHistory getProductActivityHistoryRandomSampleGenerator() {
        return new ProductActivityHistory()
            .id(longCount.incrementAndGet())
            .activityId(intCount.incrementAndGet())
            .activityType(UUID.randomUUID().toString())
            .createdBy(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .suggestedProductId(intCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet());
    }
}
