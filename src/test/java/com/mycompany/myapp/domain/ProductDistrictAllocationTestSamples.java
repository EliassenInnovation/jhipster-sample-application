package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductDistrictAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductDistrictAllocation getProductDistrictAllocationSample1() {
        return new ProductDistrictAllocation()
            .id(1L)
            .createdBy(1)
            .productDistrictAllocationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .productId(1L)
            .schoolDistrictId(1)
            .updatedBy(1);
    }

    public static ProductDistrictAllocation getProductDistrictAllocationSample2() {
        return new ProductDistrictAllocation()
            .id(2L)
            .createdBy(2)
            .productDistrictAllocationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .productId(2L)
            .schoolDistrictId(2)
            .updatedBy(2);
    }

    public static ProductDistrictAllocation getProductDistrictAllocationRandomSampleGenerator() {
        return new ProductDistrictAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .productDistrictAllocationId(UUID.randomUUID())
            .productId(longCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet());
    }
}
