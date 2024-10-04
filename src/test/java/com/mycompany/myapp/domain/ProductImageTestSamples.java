package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductImage getProductImageSample1() {
        return new ProductImage().id(1L).createdBy(1).imageURL("imageURL1").productId(1L).productImageId(1);
    }

    public static ProductImage getProductImageSample2() {
        return new ProductImage().id(2L).createdBy(2).imageURL("imageURL2").productId(2L).productImageId(2);
    }

    public static ProductImage getProductImageRandomSampleGenerator() {
        return new ProductImage()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .imageURL(UUID.randomUUID().toString())
            .productId(longCount.incrementAndGet())
            .productImageId(intCount.incrementAndGet());
    }
}
