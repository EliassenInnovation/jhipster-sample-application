package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductImageBeforeApproveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductImageBeforeApprove getProductImageBeforeApproveSample1() {
        return new ProductImageBeforeApprove().id(1L).createdBy(1).imageURL("imageURL1").productId(1L).productImageId(1);
    }

    public static ProductImageBeforeApprove getProductImageBeforeApproveSample2() {
        return new ProductImageBeforeApprove().id(2L).createdBy(2).imageURL("imageURL2").productId(2L).productImageId(2);
    }

    public static ProductImageBeforeApprove getProductImageBeforeApproveRandomSampleGenerator() {
        return new ProductImageBeforeApprove()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .imageURL(UUID.randomUUID().toString())
            .productId(longCount.incrementAndGet())
            .productImageId(intCount.incrementAndGet());
    }
}
