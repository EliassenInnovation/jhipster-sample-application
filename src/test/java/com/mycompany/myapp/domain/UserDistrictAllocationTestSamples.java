package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserDistrictAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserDistrictAllocation getUserDistrictAllocationSample1() {
        return new UserDistrictAllocation()
            .id(1L)
            .createdBy(1)
            .schoolDistrictId(1)
            .updatedBy(1)
            .userDistrictAllocationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(1);
    }

    public static UserDistrictAllocation getUserDistrictAllocationSample2() {
        return new UserDistrictAllocation()
            .id(2L)
            .createdBy(2)
            .schoolDistrictId(2)
            .updatedBy(2)
            .userDistrictAllocationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(2);
    }

    public static UserDistrictAllocation getUserDistrictAllocationRandomSampleGenerator() {
        return new UserDistrictAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet())
            .userDistrictAllocationId(UUID.randomUUID())
            .userId(intCount.incrementAndGet());
    }
}
