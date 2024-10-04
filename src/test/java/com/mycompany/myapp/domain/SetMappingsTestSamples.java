package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SetMappingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SetMappings getSetMappingsSample1() {
        return new SetMappings().id(1L).iD(1).oneWorldValue("oneWorldValue1").productValue("productValue1").setName("setName1");
    }

    public static SetMappings getSetMappingsSample2() {
        return new SetMappings().id(2L).iD(2).oneWorldValue("oneWorldValue2").productValue("productValue2").setName("setName2");
    }

    public static SetMappings getSetMappingsRandomSampleGenerator() {
        return new SetMappings()
            .id(longCount.incrementAndGet())
            .iD(intCount.incrementAndGet())
            .oneWorldValue(UUID.randomUUID().toString())
            .productValue(UUID.randomUUID().toString())
            .setName(UUID.randomUUID().toString());
    }
}
