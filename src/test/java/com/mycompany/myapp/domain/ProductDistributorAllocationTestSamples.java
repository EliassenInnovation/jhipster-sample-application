package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductDistributorAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductDistributorAllocation getProductDistributorAllocationSample1() {
        return new ProductDistributorAllocation()
            .id(1L)
            .createdBy(1)
            .distributorId(1)
            .productDistributorAllocationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .productId(1L)
            .updatedBy(1);
    }

    public static ProductDistributorAllocation getProductDistributorAllocationSample2() {
        return new ProductDistributorAllocation()
            .id(2L)
            .createdBy(2)
            .distributorId(2)
            .productDistributorAllocationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .productId(2L)
            .updatedBy(2);
    }

    public static ProductDistributorAllocation getProductDistributorAllocationRandomSampleGenerator() {
        return new ProductDistributorAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .distributorId(intCount.incrementAndGet())
            .productDistributorAllocationId(UUID.randomUUID())
            .productId(longCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet());
    }
}
