package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductH7NewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductH7New getProductH7NewSample1() {
        return new ProductH7New()
            .id(1L)
            .gtinUpc("gtinUpc1")
            .h7KeywordId(1)
            .iOCGroup("iOCGroup1")
            .productH7Id(1)
            .productId(1L)
            .productName("productName1");
    }

    public static ProductH7New getProductH7NewSample2() {
        return new ProductH7New()
            .id(2L)
            .gtinUpc("gtinUpc2")
            .h7KeywordId(2)
            .iOCGroup("iOCGroup2")
            .productH7Id(2)
            .productId(2L)
            .productName("productName2");
    }

    public static ProductH7New getProductH7NewRandomSampleGenerator() {
        return new ProductH7New()
            .id(longCount.incrementAndGet())
            .gtinUpc(UUID.randomUUID().toString())
            .h7KeywordId(intCount.incrementAndGet())
            .iOCGroup(UUID.randomUUID().toString())
            .productH7Id(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .productName(UUID.randomUUID().toString());
    }
}
