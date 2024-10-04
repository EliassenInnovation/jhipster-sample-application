package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommunityLikeMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CommunityLikeMst getCommunityLikeMstSample1() {
        return new CommunityLikeMst()
            .id(1L)
            .communityLikeId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .communityPostId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .likedByUserId(1);
    }

    public static CommunityLikeMst getCommunityLikeMstSample2() {
        return new CommunityLikeMst()
            .id(2L)
            .communityLikeId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .communityPostId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .likedByUserId(2);
    }

    public static CommunityLikeMst getCommunityLikeMstRandomSampleGenerator() {
        return new CommunityLikeMst()
            .id(longCount.incrementAndGet())
            .communityLikeId(UUID.randomUUID())
            .communityPostId(UUID.randomUUID())
            .likedByUserId(intCount.incrementAndGet());
    }
}
