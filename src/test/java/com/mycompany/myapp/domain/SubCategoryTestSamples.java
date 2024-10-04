package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubCategory getSubCategorySample1() {
        return new SubCategory()
            .id(1L)
            .categoryId(1)
            .createdBy(1)
            .subCategoryCode("subCategoryCode1")
            .subCategoryId(1)
            .subCategoryName("subCategoryName1")
            .updatedBy(1);
    }

    public static SubCategory getSubCategorySample2() {
        return new SubCategory()
            .id(2L)
            .categoryId(2)
            .createdBy(2)
            .subCategoryCode("subCategoryCode2")
            .subCategoryId(2)
            .subCategoryName("subCategoryName2")
            .updatedBy(2);
    }

    public static SubCategory getSubCategoryRandomSampleGenerator() {
        return new SubCategory()
            .id(longCount.incrementAndGet())
            .categoryId(intCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .subCategoryCode(UUID.randomUUID().toString())
            .subCategoryId(intCount.incrementAndGet())
            .subCategoryName(UUID.randomUUID().toString())
            .updatedBy(intCount.incrementAndGet());
    }
}
