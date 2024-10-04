package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IocCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IocCategory getIocCategorySample1() {
        return new IocCategory().id(1L).iocCategoryColor("iocCategoryColor1").iocCategoryId(1).iocCategoryName("iocCategoryName1");
    }

    public static IocCategory getIocCategorySample2() {
        return new IocCategory().id(2L).iocCategoryColor("iocCategoryColor2").iocCategoryId(2).iocCategoryName("iocCategoryName2");
    }

    public static IocCategory getIocCategoryRandomSampleGenerator() {
        return new IocCategory()
            .id(longCount.incrementAndGet())
            .iocCategoryColor(UUID.randomUUID().toString())
            .iocCategoryId(intCount.incrementAndGet())
            .iocCategoryName(UUID.randomUUID().toString());
    }
}
