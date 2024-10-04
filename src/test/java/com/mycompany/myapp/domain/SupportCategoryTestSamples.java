package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SupportCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SupportCategory getSupportCategorySample1() {
        return new SupportCategory().id(1L).supportCategoryId(1).supportCategoryName("supportCategoryName1");
    }

    public static SupportCategory getSupportCategorySample2() {
        return new SupportCategory().id(2L).supportCategoryId(2).supportCategoryName("supportCategoryName2");
    }

    public static SupportCategory getSupportCategoryRandomSampleGenerator() {
        return new SupportCategory()
            .id(longCount.incrementAndGet())
            .supportCategoryId(intCount.incrementAndGet())
            .supportCategoryName(UUID.randomUUID().toString());
    }
}
