package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductChangeHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductChangeHistory getProductChangeHistorySample1() {
        return new ProductChangeHistory()
            .id(1L)
            .createdBy(1)
            .historyId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .iocCategoryId(1)
            .productId(1L)
            .schoolDistrictId(1)
            .selectionType("selectionType1");
    }

    public static ProductChangeHistory getProductChangeHistorySample2() {
        return new ProductChangeHistory()
            .id(2L)
            .createdBy(2)
            .historyId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .iocCategoryId(2)
            .productId(2L)
            .schoolDistrictId(2)
            .selectionType("selectionType2");
    }

    public static ProductChangeHistory getProductChangeHistoryRandomSampleGenerator() {
        return new ProductChangeHistory()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .historyId(UUID.randomUUID())
            .iocCategoryId(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet())
            .selectionType(UUID.randomUUID().toString());
    }
}
