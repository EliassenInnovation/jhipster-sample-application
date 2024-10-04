package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductAllergenBakTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductAllergenBak getProductAllergenBakSample1() {
        return new ProductAllergenBak()
            .id(1L)
            .allergenId(1)
            .allergenGroup("allergenGroup1")
            .allergenName("allergenName1")
            .gTIN("gTIN1")
            .gTINUPC("gTINUPC1")
            .productAllergenId(1)
            .productId(1L)
            .uPC("uPC1");
    }

    public static ProductAllergenBak getProductAllergenBakSample2() {
        return new ProductAllergenBak()
            .id(2L)
            .allergenId(2)
            .allergenGroup("allergenGroup2")
            .allergenName("allergenName2")
            .gTIN("gTIN2")
            .gTINUPC("gTINUPC2")
            .productAllergenId(2)
            .productId(2L)
            .uPC("uPC2");
    }

    public static ProductAllergenBak getProductAllergenBakRandomSampleGenerator() {
        return new ProductAllergenBak()
            .id(longCount.incrementAndGet())
            .allergenId(intCount.incrementAndGet())
            .allergenGroup(UUID.randomUUID().toString())
            .allergenName(UUID.randomUUID().toString())
            .gTIN(UUID.randomUUID().toString())
            .gTINUPC(UUID.randomUUID().toString())
            .productAllergenId(intCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .uPC(UUID.randomUUID().toString());
    }
}
