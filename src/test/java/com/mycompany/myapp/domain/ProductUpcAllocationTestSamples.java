package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductUpcAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductUpcAllocation getProductUpcAllocationSample1() {
        return new ProductUpcAllocation().id(1L).createdBy(1).productId(1L).productUpcId(1).uPC("uPC1").updatedBy(1);
    }

    public static ProductUpcAllocation getProductUpcAllocationSample2() {
        return new ProductUpcAllocation().id(2L).createdBy(2).productId(2L).productUpcId(2).uPC("uPC2").updatedBy(2);
    }

    public static ProductUpcAllocation getProductUpcAllocationRandomSampleGenerator() {
        return new ProductUpcAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .productUpcId(intCount.incrementAndGet())
            .uPC(UUID.randomUUID().toString())
            .updatedBy(intCount.incrementAndGet());
    }
}
