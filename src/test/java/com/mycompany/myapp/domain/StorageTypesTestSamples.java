package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StorageTypesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StorageTypes getStorageTypesSample1() {
        return new StorageTypes().id(1L).storageTypeId(1).storageTypeName("storageTypeName1");
    }

    public static StorageTypes getStorageTypesSample2() {
        return new StorageTypes().id(2L).storageTypeId(2).storageTypeName("storageTypeName2");
    }

    public static StorageTypes getStorageTypesRandomSampleGenerator() {
        return new StorageTypes()
            .id(longCount.incrementAndGet())
            .storageTypeId(intCount.incrementAndGet())
            .storageTypeName(UUID.randomUUID().toString());
    }
}
