package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BlockReportPostTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BlockReportPost getBlockReportPostSample1() {
        return new BlockReportPost()
            .id(1L)
            .postBlockReportId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .postId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .postType("postType1")
            .requestedBy(1);
    }

    public static BlockReportPost getBlockReportPostSample2() {
        return new BlockReportPost()
            .id(2L)
            .postBlockReportId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .postId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .postType("postType2")
            .requestedBy(2);
    }

    public static BlockReportPost getBlockReportPostRandomSampleGenerator() {
        return new BlockReportPost()
            .id(longCount.incrementAndGet())
            .postBlockReportId(UUID.randomUUID())
            .postId(UUID.randomUUID())
            .postType(UUID.randomUUID().toString())
            .requestedBy(intCount.incrementAndGet());
    }
}
