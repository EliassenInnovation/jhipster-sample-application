package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SupportTicketTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SupportTicketTransaction getSupportTicketTransactionSample1() {
        return new SupportTicketTransaction()
            .id(1L)
            .createdBy(1)
            .fileExtension("fileExtension1")
            .fileName("fileName1")
            .filePath("filePath1")
            .fileSize(1)
            .lastUpdatedBy(1)
            .ticketId(1)
            .ticketTransactionId(1)
            .userId(1);
    }

    public static SupportTicketTransaction getSupportTicketTransactionSample2() {
        return new SupportTicketTransaction()
            .id(2L)
            .createdBy(2)
            .fileExtension("fileExtension2")
            .fileName("fileName2")
            .filePath("filePath2")
            .fileSize(2)
            .lastUpdatedBy(2)
            .ticketId(2)
            .ticketTransactionId(2)
            .userId(2);
    }

    public static SupportTicketTransaction getSupportTicketTransactionRandomSampleGenerator() {
        return new SupportTicketTransaction()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .fileExtension(UUID.randomUUID().toString())
            .fileName(UUID.randomUUID().toString())
            .filePath(UUID.randomUUID().toString())
            .fileSize(intCount.incrementAndGet())
            .lastUpdatedBy(intCount.incrementAndGet())
            .ticketId(intCount.incrementAndGet())
            .ticketTransactionId(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet());
    }
}
