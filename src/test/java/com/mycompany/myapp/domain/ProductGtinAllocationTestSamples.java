package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductGtinAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductGtinAllocation getProductGtinAllocationSample1() {
        return new ProductGtinAllocation().id(1L).createdBy(1).gTIN("gTIN1").productGtinId(1).productId(1L).updatedBy(1);
    }

    public static ProductGtinAllocation getProductGtinAllocationSample2() {
        return new ProductGtinAllocation().id(2L).createdBy(2).gTIN("gTIN2").productGtinId(2).productId(2L).updatedBy(2);
    }

    public static ProductGtinAllocation getProductGtinAllocationRandomSampleGenerator() {
        return new ProductGtinAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .gTIN(UUID.randomUUID().toString())
            .productGtinId(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet());
    }
}
