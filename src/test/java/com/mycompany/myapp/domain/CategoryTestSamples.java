package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Category getCategorySample1() {
        return new Category().id(1L).categoryCode("categoryCode1").categoryId(1).categoryName("categoryName1").createdBy(1).updatedBy(1);
    }

    public static Category getCategorySample2() {
        return new Category().id(2L).categoryCode("categoryCode2").categoryId(2).categoryName("categoryName2").createdBy(2).updatedBy(2);
    }

    public static Category getCategoryRandomSampleGenerator() {
        return new Category()
            .id(longCount.incrementAndGet())
            .categoryCode(UUID.randomUUID().toString())
            .categoryId(intCount.incrementAndGet())
            .categoryName(UUID.randomUUID().toString())
            .createdBy(intCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet());
    }
}
