package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AllergenMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AllergenMst getAllergenMstSample1() {
        return new AllergenMst().id(1L).allergenGroup("allergenGroup1").allergenId(1).allergenName("allergenName1");
    }

    public static AllergenMst getAllergenMstSample2() {
        return new AllergenMst().id(2L).allergenGroup("allergenGroup2").allergenId(2).allergenName("allergenName2");
    }

    public static AllergenMst getAllergenMstRandomSampleGenerator() {
        return new AllergenMst()
            .id(longCount.incrementAndGet())
            .allergenGroup(UUID.randomUUID().toString())
            .allergenId(intCount.incrementAndGet())
            .allergenName(UUID.randomUUID().toString());
    }
}
