package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PrivacyTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PrivacyType getPrivacyTypeSample1() {
        return new PrivacyType().id(1L).privacyTypeId(1).privacyTypeName("privacyTypeName1");
    }

    public static PrivacyType getPrivacyTypeSample2() {
        return new PrivacyType().id(2L).privacyTypeId(2).privacyTypeName("privacyTypeName2");
    }

    public static PrivacyType getPrivacyTypeRandomSampleGenerator() {
        return new PrivacyType()
            .id(longCount.incrementAndGet())
            .privacyTypeId(intCount.incrementAndGet())
            .privacyTypeName(UUID.randomUUID().toString());
    }
}
