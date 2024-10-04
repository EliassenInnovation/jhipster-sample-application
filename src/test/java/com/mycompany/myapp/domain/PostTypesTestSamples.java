package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PostTypesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PostTypes getPostTypesSample1() {
        return new PostTypes()
            .id(1L)
            .createdBy(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .lastUpdatedBy(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .postType("postType1")
            .postTypeId(1);
    }

    public static PostTypes getPostTypesSample2() {
        return new PostTypes()
            .id(2L)
            .createdBy(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .lastUpdatedBy(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .postType("postType2")
            .postTypeId(2);
    }

    public static PostTypes getPostTypesRandomSampleGenerator() {
        return new PostTypes()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID())
            .lastUpdatedBy(UUID.randomUUID())
            .postType(UUID.randomUUID().toString())
            .postTypeId(intCount.incrementAndGet());
    }
}
