package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductsToUpdateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductsToUpdate getProductsToUpdateSample1() {
        return new ProductsToUpdate().id(1L).maxGLNCode("maxGLNCode1").maxManufacturerID(1).productId(1L);
    }

    public static ProductsToUpdate getProductsToUpdateSample2() {
        return new ProductsToUpdate().id(2L).maxGLNCode("maxGLNCode2").maxManufacturerID(2).productId(2L);
    }

    public static ProductsToUpdate getProductsToUpdateRandomSampleGenerator() {
        return new ProductsToUpdate()
            .id(longCount.incrementAndGet())
            .maxGLNCode(UUID.randomUUID().toString())
            .maxManufacturerID(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet());
    }
}
