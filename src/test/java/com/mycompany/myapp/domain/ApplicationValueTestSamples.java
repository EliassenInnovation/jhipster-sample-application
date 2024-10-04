package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationValueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApplicationValue getApplicationValueSample1() {
        return new ApplicationValue().id(1L).applicationValueId(1).key("key1").valueInt(1).valueLong(1L).valueString("valueString1");
    }

    public static ApplicationValue getApplicationValueSample2() {
        return new ApplicationValue().id(2L).applicationValueId(2).key("key2").valueInt(2).valueLong(2L).valueString("valueString2");
    }

    public static ApplicationValue getApplicationValueRandomSampleGenerator() {
        return new ApplicationValue()
            .id(longCount.incrementAndGet())
            .applicationValueId(intCount.incrementAndGet())
            .key(UUID.randomUUID().toString())
            .valueInt(intCount.incrementAndGet())
            .valueLong(longCount.incrementAndGet())
            .valueString(UUID.randomUUID().toString());
    }
}
