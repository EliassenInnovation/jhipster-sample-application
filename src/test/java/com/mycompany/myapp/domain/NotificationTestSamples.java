package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification().id(1L).createdBy(1).notificationId(1).referenceId(1).type("type1").userId(1);
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(2L).createdBy(2).notificationId(2).referenceId(2).type("type2").userId(2);
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .notificationId(intCount.incrementAndGet())
            .referenceId(intCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .userId(intCount.incrementAndGet());
    }
}
