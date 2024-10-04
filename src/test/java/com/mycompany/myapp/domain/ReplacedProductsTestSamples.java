package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReplacedProductsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReplacedProducts getReplacedProductsSample1() {
        return new ReplacedProducts().id(1L).productId(1L).replacedByUserId(1).replacedId(1).replacedProductId(1L).schoolDistrictId(1);
    }

    public static ReplacedProducts getReplacedProductsSample2() {
        return new ReplacedProducts().id(2L).productId(2L).replacedByUserId(2).replacedId(2).replacedProductId(2L).schoolDistrictId(2);
    }

    public static ReplacedProducts getReplacedProductsRandomSampleGenerator() {
        return new ReplacedProducts()
            .id(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .replacedByUserId(intCount.incrementAndGet())
            .replacedId(intCount.incrementAndGet())
            .replacedProductId(longCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet());
    }
}
