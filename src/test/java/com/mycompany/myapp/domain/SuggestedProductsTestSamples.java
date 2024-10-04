package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SuggestedProductsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SuggestedProducts getSuggestedProductsSample1() {
        return new SuggestedProducts()
            .id(1L)
            .productId(1L)
            .suggestedByDistrict(1)
            .suggestedByUserId(1)
            .suggestedProductId(1L)
            .suggestionId(1);
    }

    public static SuggestedProducts getSuggestedProductsSample2() {
        return new SuggestedProducts()
            .id(2L)
            .productId(2L)
            .suggestedByDistrict(2)
            .suggestedByUserId(2)
            .suggestedProductId(2L)
            .suggestionId(2);
    }

    public static SuggestedProducts getSuggestedProductsRandomSampleGenerator() {
        return new SuggestedProducts()
            .id(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .suggestedByDistrict(intCount.incrementAndGet())
            .suggestedByUserId(intCount.incrementAndGet())
            .suggestedProductId(longCount.incrementAndGet())
            .suggestionId(intCount.incrementAndGet());
    }
}
