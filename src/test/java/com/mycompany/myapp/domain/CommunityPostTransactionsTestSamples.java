package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommunityPostTransactionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CommunityPostTransactions getCommunityPostTransactionsSample1() {
        return new CommunityPostTransactions()
            .id(1L)
            .communityPostId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .communityPostTransactionId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy(1)
            .lastUpdatedBy(1);
    }

    public static CommunityPostTransactions getCommunityPostTransactionsSample2() {
        return new CommunityPostTransactions()
            .id(2L)
            .communityPostId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .communityPostTransactionId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy(2)
            .lastUpdatedBy(2);
    }

    public static CommunityPostTransactions getCommunityPostTransactionsRandomSampleGenerator() {
        return new CommunityPostTransactions()
            .id(longCount.incrementAndGet())
            .communityPostId(UUID.randomUUID())
            .communityPostTransactionId(UUID.randomUUID())
            .createdBy(intCount.incrementAndGet())
            .lastUpdatedBy(intCount.incrementAndGet());
    }
}
