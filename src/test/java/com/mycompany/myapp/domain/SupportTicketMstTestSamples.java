package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SupportTicketMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SupportTicketMst getSupportTicketMstSample1() {
        return new SupportTicketMst()
            .id(1L)
            .createdBy(1)
            .email("email1")
            .lastUpdatedBy(1)
            .priority("priority1")
            .schoolDistrictId(1)
            .status("status1")
            .subject("subject1")
            .supportCategoryId(1)
            .ticketId(1)
            .ticketReferenceNumber(1)
            .userId(1)
            .userName("userName1");
    }

    public static SupportTicketMst getSupportTicketMstSample2() {
        return new SupportTicketMst()
            .id(2L)
            .createdBy(2)
            .email("email2")
            .lastUpdatedBy(2)
            .priority("priority2")
            .schoolDistrictId(2)
            .status("status2")
            .subject("subject2")
            .supportCategoryId(2)
            .ticketId(2)
            .ticketReferenceNumber(2)
            .userId(2)
            .userName("userName2");
    }

    public static SupportTicketMst getSupportTicketMstRandomSampleGenerator() {
        return new SupportTicketMst()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .lastUpdatedBy(intCount.incrementAndGet())
            .priority(UUID.randomUUID().toString())
            .schoolDistrictId(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .supportCategoryId(intCount.incrementAndGet())
            .ticketId(intCount.incrementAndGet())
            .ticketReferenceNumber(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .userName(UUID.randomUUID().toString());
    }
}
