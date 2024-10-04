package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ManufacturerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Manufacturer getManufacturerSample1() {
        return new Manufacturer()
            .id(1L)
            .createdBy(1)
            .email("email1")
            .firstName("firstName1")
            .glnNumber("glnNumber1")
            .lastName("lastName1")
            .manufacturer("manufacturer1")
            .manufacturerId(1)
            .password("password1")
            .phoneNumber("phoneNumber1");
    }

    public static Manufacturer getManufacturerSample2() {
        return new Manufacturer()
            .id(2L)
            .createdBy(2)
            .email("email2")
            .firstName("firstName2")
            .glnNumber("glnNumber2")
            .lastName("lastName2")
            .manufacturer("manufacturer2")
            .manufacturerId(2)
            .password("password2")
            .phoneNumber("phoneNumber2");
    }

    public static Manufacturer getManufacturerRandomSampleGenerator() {
        return new Manufacturer()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .glnNumber(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString())
            .manufacturerId(intCount.incrementAndGet())
            .password(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
