package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserMst getUserMstSample1() {
        return new UserMst()
            .id(1L)
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .city("city1")
            .country("country1")
            .createBy(1)
            .email("email1")
            .firstName("firstName1")
            .lastName("lastName1")
            .manufacturerId(1)
            .mobile("mobile1")
            .objectId("objectId1")
            .password("password1")
            .roleId(1)
            .schoolDistrictId(1)
            .state(1)
            .updatedBy(1)
            .userId(1)
            .zipCode("zipCode1");
    }

    public static UserMst getUserMstSample2() {
        return new UserMst()
            .id(2L)
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .city("city2")
            .country("country2")
            .createBy(2)
            .email("email2")
            .firstName("firstName2")
            .lastName("lastName2")
            .manufacturerId(2)
            .mobile("mobile2")
            .objectId("objectId2")
            .password("password2")
            .roleId(2)
            .schoolDistrictId(2)
            .state(2)
            .updatedBy(2)
            .userId(2)
            .zipCode("zipCode2");
    }

    public static UserMst getUserMstRandomSampleGenerator() {
        return new UserMst()
            .id(longCount.incrementAndGet())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .createBy(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .manufacturerId(intCount.incrementAndGet())
            .mobile(UUID.randomUUID().toString())
            .objectId(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .roleId(intCount.incrementAndGet())
            .schoolDistrictId(intCount.incrementAndGet())
            .state(intCount.incrementAndGet())
            .updatedBy(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .zipCode(UUID.randomUUID().toString());
    }
}
