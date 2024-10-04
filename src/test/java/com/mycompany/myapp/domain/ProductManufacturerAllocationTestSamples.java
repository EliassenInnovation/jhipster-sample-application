package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductManufacturerAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductManufacturerAllocation getProductManufacturerAllocationSample1() {
        return new ProductManufacturerAllocation()
            .id(1L)
            .createdBy(1)
            .manufactureId(1)
            .productId(1L)
            .productManufacturerAllocationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .updatedBy(1);
    }

    public static ProductManufacturerAllocation getProductManufacturerAllocationSample2() {
        return new ProductManufacturerAllocation()
            .id(2L)
            .createdBy(2)
            .manufactureId(2)
            .productId(2L)
            .productManufacturerAllocationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .updatedBy(2);
    }

    public static ProductManufacturerAllocation getProductManufacturerAllocationRandomSampleGenerator() {
        return new ProductManufacturerAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .manufactureId(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .productManufacturerAllocationId(UUID.randomUUID())
            .updatedBy(intCount.incrementAndGet());
    }
}
